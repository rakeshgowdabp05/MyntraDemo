package com.myntrademo.dao.impl;

import com.myntrademo.constant.CatalogConstants;
import com.myntrademo.constant.MessageConstants;
import com.myntrademo.dao.WishlistDao;
import com.myntrademo.dto.wishlist.WishlistItemDto;
import com.myntrademo.util.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcWishlistDao implements WishlistDao {

    private static final String FIND_WISHLIST_ID_SQL =
            "SELECT wishlist_id FROM wishlists WHERE user_id = ? LIMIT 1";

    private static final String INSERT_WISHLIST_SQL =
            "INSERT INTO wishlists (user_id) VALUES (?)";

    private static final String FIND_ACTIVE_PRODUCT_SQL =
            "SELECT product_id FROM products WHERE product_id = ? AND product_status = ? LIMIT 1";

    private static final String FIND_ACTIVE_VARIANT_SQL =
            """
            SELECT
                pv.variant_id,
                pv.product_id,
                pv.selling_price,
                pv.stock_quantity,
                pv.is_active,
                p.product_status
            FROM product_variants pv
            INNER JOIN products p ON pv.product_id = p.product_id
            WHERE pv.variant_id = ?
              AND pv.product_id = ?
            LIMIT 1
            """;

    private static final String FIND_DEFAULT_VARIANT_SQL =
            """
            SELECT
                pv.variant_id,
                pv.product_id,
                pv.selling_price,
                pv.stock_quantity,
                pv.is_active,
                p.product_status
            FROM product_variants pv
            INNER JOIN products p ON pv.product_id = p.product_id
            WHERE pv.product_id = ?
              AND pv.is_active = 1
              AND pv.stock_quantity > 0
              AND p.product_status = ?
            ORDER BY pv.selling_price ASC, pv.variant_id ASC
            LIMIT 1
            """;

    private static final String FIND_EXISTING_WISHLIST_ITEM_SQL =
            """
            SELECT wishlist_item_id
            FROM wishlist_items
            WHERE wishlist_id = ?
              AND product_id = ?
              AND (
                    (variant_id IS NULL AND ? IS NULL)
                    OR variant_id = ?
              )
            LIMIT 1
            """;

    private static final String INSERT_WISHLIST_ITEM_SQL =
            """
            INSERT INTO wishlist_items (wishlist_id, product_id, variant_id)
            VALUES (?, ?, ?)
            """;

    private static final String FIND_WISHLIST_ITEMS_SQL =
            """
            SELECT
                wi.wishlist_item_id,
                wi.product_id,
                wi.variant_id,
                p.product_name,
                b.brand_name,
                c.category_name,
                pv.size,
                pv.color,
                COALESCE(pv.price, p.base_price) AS mrp_price,
                COALESCE(pv.selling_price, p.selling_price) AS selling_price,
                COALESCE(
                    pv.stock_quantity,
                    (
                        SELECT COALESCE(SUM(pv2.stock_quantity), 0)
                        FROM product_variants pv2
                        WHERE pv2.product_id = p.product_id
                          AND pv2.is_active = 1
                    )
                ) AS stock_quantity,
                (
                    SELECT pi.image_url
                    FROM product_images pi
                    WHERE pi.product_id = p.product_id
                    ORDER BY pi.is_primary DESC, pi.sort_order ASC, pi.image_id ASC
                    LIMIT 1
                ) AS image_url
            FROM wishlists w
            INNER JOIN wishlist_items wi ON w.wishlist_id = wi.wishlist_id
            INNER JOIN products p ON wi.product_id = p.product_id
            INNER JOIN brands b ON p.brand_id = b.brand_id
            INNER JOIN categories c ON p.category_id = c.category_id
            LEFT JOIN product_variants pv ON wi.variant_id = pv.variant_id
            WHERE w.user_id = ?
            ORDER BY wi.created_at DESC, wi.wishlist_item_id DESC
            """;

    private static final String DELETE_WISHLIST_ITEM_SQL =
            """
            DELETE wi
            FROM wishlist_items wi
            INNER JOIN wishlists w ON wi.wishlist_id = w.wishlist_id
            WHERE wi.wishlist_item_id = ?
              AND w.user_id = ?
            """;

    private static final String FIND_WISHLIST_ITEM_FOR_USER_SQL =
            """
            SELECT
                wi.wishlist_item_id,
                wi.product_id,
                wi.variant_id
            FROM wishlist_items wi
            INNER JOIN wishlists w ON wi.wishlist_id = w.wishlist_id
            WHERE wi.wishlist_item_id = ?
              AND w.user_id = ?
            LIMIT 1
            """;

    private static final String FIND_CART_ITEM_FOR_USER_SQL =
            """
            SELECT
                ci.cart_item_id,
                ci.product_id,
                ci.variant_id
            FROM cart_items ci
            INNER JOIN carts c ON ci.cart_id = c.cart_id
            WHERE ci.cart_item_id = ?
              AND c.user_id = ?
            LIMIT 1
            """;

    private static final String FIND_CART_ID_BY_USER_ID_SQL =
            "SELECT cart_id FROM carts WHERE user_id = ? LIMIT 1";

    private static final String INSERT_CART_SQL =
            "INSERT INTO carts (user_id) VALUES (?)";

    private static final String FIND_CART_ITEM_SQL =
            """
            SELECT cart_item_id, quantity
            FROM cart_items
            WHERE cart_id = ?
              AND variant_id = ?
            LIMIT 1
            """;

    private static final String INSERT_CART_ITEM_SQL =
            """
            INSERT INTO cart_items
            (cart_id, product_id, variant_id, quantity, price_at_added)
            VALUES (?, ?, ?, ?, ?)
            """;

    private static final String UPDATE_CART_ITEM_QUANTITY_SQL =
            """
            UPDATE cart_items
            SET quantity = ?
            WHERE cart_item_id = ?
            """;

    private static final String DELETE_CART_ITEM_SQL =
            """
            DELETE ci
            FROM cart_items ci
            INNER JOIN carts c ON ci.cart_id = c.cart_id
            WHERE ci.cart_item_id = ?
              AND c.user_id = ?
            """;

    @Override
    public void addItem(Long userId, Long productId, Long variantId) throws SQLException {
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);

            try {
                Long wishlistId = getOrCreateWishlistId(connection, userId);

                validateProduct(connection, productId);

                if (variantId != null) {
                    VariantRecord variant = findVariant(connection, productId, variantId)
                            .orElseThrow(() -> new IllegalArgumentException(MessageConstants.PRODUCT_NOT_AVAILABLE));

                    validateVariant(variant);
                }

                if (wishlistItemExists(connection, wishlistId, productId, variantId)) {
                    throw new IllegalArgumentException(MessageConstants.WISHLIST_ITEM_ALREADY_EXISTS);
                }

                insertWishlistItem(connection, wishlistId, productId, variantId);

                connection.commit();

            } catch (SQLException | RuntimeException exception) {
                connection.rollback();
                throw exception;

            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    @Override
    public List<WishlistItemDto> findWishlistItems(Long userId) throws SQLException {
        List<WishlistItemDto> items = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_WISHLIST_ITEMS_SQL)) {

            statement.setLong(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    items.add(mapWishlistItem(resultSet));
                }
            }
        }

        return items;
    }

    @Override
    public void removeItem(Long userId, Long wishlistItemId) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_WISHLIST_ITEM_SQL)) {

            statement.setLong(1, wishlistItemId);
            statement.setLong(2, userId);

            int updatedRows = statement.executeUpdate();

            if (updatedRows == 0) {
                throw new IllegalArgumentException(MessageConstants.WISHLIST_ITEM_NOT_FOUND);
            }
        }
    }

    @Override
    public void moveWishlistItemToCart(Long userId, Long wishlistItemId) throws SQLException {
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);

            try {
                WishlistItemRecord wishlistItem = findWishlistItemForUser(connection, userId, wishlistItemId)
                        .orElseThrow(() -> new IllegalArgumentException(MessageConstants.WISHLIST_ITEM_NOT_FOUND));

                VariantRecord variant;

                if (wishlistItem.variantId() == null) {
                    variant = findDefaultVariant(connection, wishlistItem.productId())
                            .orElseThrow(() -> new IllegalArgumentException(MessageConstants.PRODUCT_OUT_OF_STOCK));
                } else {
                    variant = findVariant(connection, wishlistItem.productId(), wishlistItem.variantId())
                            .orElseThrow(() -> new IllegalArgumentException(MessageConstants.PRODUCT_NOT_AVAILABLE));
                }

                validateVariant(variant);

                Long cartId = getOrCreateCartId(connection, userId);
                addOrUpdateCartItem(connection, cartId, wishlistItem.productId(), variant);

                deleteWishlistItem(connection, userId, wishlistItemId);

                connection.commit();

            } catch (SQLException | RuntimeException exception) {
                connection.rollback();
                throw exception;

            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    @Override
    public void moveCartItemToWishlist(Long userId, Long cartItemId) throws SQLException {
        try (Connection connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);

            try {
                CartItemRecord cartItem = findCartItemForUser(connection, userId, cartItemId)
                        .orElseThrow(() -> new IllegalArgumentException(MessageConstants.CART_ITEM_NOT_FOUND));

                Long wishlistId = getOrCreateWishlistId(connection, userId);

                if (!wishlistItemExists(connection, wishlistId, cartItem.productId(), cartItem.variantId())) {
                    insertWishlistItem(connection, wishlistId, cartItem.productId(), cartItem.variantId());
                }

                deleteCartItem(connection, userId, cartItemId);

                connection.commit();

            } catch (SQLException | RuntimeException exception) {
                connection.rollback();
                throw exception;

            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    private Long getOrCreateWishlistId(Connection connection, Long userId) throws SQLException {
        Optional<Long> existingId = findWishlistId(connection, userId);

        if (existingId.isPresent()) {
            return existingId.get();
        }

        try (PreparedStatement statement = connection.prepareStatement(
                INSERT_WISHLIST_SQL,
                Statement.RETURN_GENERATED_KEYS
        )) {
            statement.setLong(1, userId);
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                }
            }
        }

        throw new SQLException("Wishlist creation failed.");
    }

    private Optional<Long> findWishlistId(Connection connection, Long userId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(FIND_WISHLIST_ID_SQL)) {
            statement.setLong(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(resultSet.getLong("wishlist_id"));
                }
            }
        }

        return Optional.empty();
    }

    private void validateProduct(Connection connection, Long productId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(FIND_ACTIVE_PRODUCT_SQL)) {
            statement.setLong(1, productId);
            statement.setString(2, CatalogConstants.ACTIVE_PRODUCT_STATUS);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new IllegalArgumentException(MessageConstants.PRODUCT_NOT_AVAILABLE);
                }
            }
        }
    }

    private Optional<VariantRecord> findVariant(
            Connection connection,
            Long productId,
            Long variantId
    ) throws SQLException {

        try (PreparedStatement statement = connection.prepareStatement(FIND_ACTIVE_VARIANT_SQL)) {
            statement.setLong(1, variantId);
            statement.setLong(2, productId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapVariant(resultSet));
                }
            }
        }

        return Optional.empty();
    }

    private Optional<VariantRecord> findDefaultVariant(Connection connection, Long productId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(FIND_DEFAULT_VARIANT_SQL)) {
            statement.setLong(1, productId);
            statement.setString(2, CatalogConstants.ACTIVE_PRODUCT_STATUS);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapVariant(resultSet));
                }
            }
        }

        return Optional.empty();
    }

    private boolean wishlistItemExists(
            Connection connection,
            Long wishlistId,
            Long productId,
            Long variantId
    ) throws SQLException {

        try (PreparedStatement statement = connection.prepareStatement(FIND_EXISTING_WISHLIST_ITEM_SQL)) {
            statement.setLong(1, wishlistId);
            statement.setLong(2, productId);
            setNullableLong(statement, 3, variantId);
            setNullableLong(statement, 4, variantId);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    private void insertWishlistItem(
            Connection connection,
            Long wishlistId,
            Long productId,
            Long variantId
    ) throws SQLException {

        try (PreparedStatement statement = connection.prepareStatement(INSERT_WISHLIST_ITEM_SQL)) {
            statement.setLong(1, wishlistId);
            statement.setLong(2, productId);
            setNullableLong(statement, 3, variantId);
            statement.executeUpdate();
        }
    }

    private Optional<WishlistItemRecord> findWishlistItemForUser(
            Connection connection,
            Long userId,
            Long wishlistItemId
    ) throws SQLException {

        try (PreparedStatement statement = connection.prepareStatement(FIND_WISHLIST_ITEM_FOR_USER_SQL)) {
            statement.setLong(1, wishlistItemId);
            statement.setLong(2, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Long variantId = resultSet.getLong("variant_id");

                    if (resultSet.wasNull()) {
                        variantId = null;
                    }

                    return Optional.of(new WishlistItemRecord(
                            resultSet.getLong("wishlist_item_id"),
                            resultSet.getLong("product_id"),
                            variantId
                    ));
                }
            }
        }

        return Optional.empty();
    }

    private Optional<CartItemRecord> findCartItemForUser(
            Connection connection,
            Long userId,
            Long cartItemId
    ) throws SQLException {

        try (PreparedStatement statement = connection.prepareStatement(FIND_CART_ITEM_FOR_USER_SQL)) {
            statement.setLong(1, cartItemId);
            statement.setLong(2, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(new CartItemRecord(
                            resultSet.getLong("cart_item_id"),
                            resultSet.getLong("product_id"),
                            resultSet.getLong("variant_id")
                    ));
                }
            }
        }

        return Optional.empty();
    }

    private Long getOrCreateCartId(Connection connection, Long userId) throws SQLException {
        Optional<Long> existingCartId = findCartIdByUserId(connection, userId);

        if (existingCartId.isPresent()) {
            return existingCartId.get();
        }

        try (PreparedStatement statement = connection.prepareStatement(
                INSERT_CART_SQL,
                Statement.RETURN_GENERATED_KEYS
        )) {
            statement.setLong(1, userId);
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                }
            }
        }

        throw new SQLException("Cart creation failed.");
    }

    private Optional<Long> findCartIdByUserId(Connection connection, Long userId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(FIND_CART_ID_BY_USER_ID_SQL)) {
            statement.setLong(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(resultSet.getLong("cart_id"));
                }
            }
        }

        return Optional.empty();
    }

    private void addOrUpdateCartItem(
            Connection connection,
            Long cartId,
            Long productId,
            VariantRecord variant
    ) throws SQLException {

        Optional<CartItemQuantityRecord> existingItem = findCartItem(connection, cartId, variant.variantId());

        if (existingItem.isPresent()) {
            int newQuantity = existingItem.get().quantity() + 1;

            if (newQuantity > variant.stockQuantity()) {
                throw new IllegalArgumentException(MessageConstants.PRODUCT_OUT_OF_STOCK);
            }

            try (PreparedStatement statement = connection.prepareStatement(UPDATE_CART_ITEM_QUANTITY_SQL)) {
                statement.setInt(1, newQuantity);
                statement.setLong(2, existingItem.get().cartItemId());
                statement.executeUpdate();
            }

            return;
        }

        try (PreparedStatement statement = connection.prepareStatement(INSERT_CART_ITEM_SQL)) {
            statement.setLong(1, cartId);
            statement.setLong(2, productId);
            statement.setLong(3, variant.variantId());
            statement.setInt(4, 1);
            statement.setBigDecimal(5, variant.sellingPrice());
            statement.executeUpdate();
        }
    }

    private Optional<CartItemQuantityRecord> findCartItem(
            Connection connection,
            Long cartId,
            Long variantId
    ) throws SQLException {

        try (PreparedStatement statement = connection.prepareStatement(FIND_CART_ITEM_SQL)) {
            statement.setLong(1, cartId);
            statement.setLong(2, variantId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(new CartItemQuantityRecord(
                            resultSet.getLong("cart_item_id"),
                            resultSet.getInt("quantity")
                    ));
                }
            }
        }

        return Optional.empty();
    }

    private void deleteWishlistItem(Connection connection, Long userId, Long wishlistItemId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_WISHLIST_ITEM_SQL)) {
            statement.setLong(1, wishlistItemId);
            statement.setLong(2, userId);
            statement.executeUpdate();
        }
    }

    private void deleteCartItem(Connection connection, Long userId, Long cartItemId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_CART_ITEM_SQL)) {
            statement.setLong(1, cartItemId);
            statement.setLong(2, userId);
            statement.executeUpdate();
        }
    }

    private void validateVariant(VariantRecord variant) {
        if (!variant.active()
                || !CatalogConstants.ACTIVE_PRODUCT_STATUS.equals(variant.productStatus())) {
            throw new IllegalArgumentException(MessageConstants.PRODUCT_NOT_AVAILABLE);
        }

        if (variant.stockQuantity() <= 0) {
            throw new IllegalArgumentException(MessageConstants.PRODUCT_OUT_OF_STOCK);
        }
    }

    private VariantRecord mapVariant(ResultSet resultSet) throws SQLException {
        return new VariantRecord(
                resultSet.getLong("variant_id"),
                resultSet.getLong("product_id"),
                resultSet.getBigDecimal("selling_price"),
                resultSet.getInt("stock_quantity"),
                resultSet.getBoolean("is_active"),
                resultSet.getString("product_status")
        );
    }

    private WishlistItemDto mapWishlistItem(ResultSet resultSet) throws SQLException {
        WishlistItemDto item = new WishlistItemDto();

        Long variantId = resultSet.getLong("variant_id");

        if (resultSet.wasNull()) {
            variantId = null;
        }

        item.setWishlistItemId(resultSet.getLong("wishlist_item_id"));
        item.setProductId(resultSet.getLong("product_id"));
        item.setVariantId(variantId);
        item.setProductName(resultSet.getString("product_name"));
        item.setBrandName(resultSet.getString("brand_name"));
        item.setCategoryName(resultSet.getString("category_name"));
        item.setSize(resultSet.getString("size"));
        item.setColor(resultSet.getString("color"));
        item.setImageUrl(resultSet.getString("image_url"));
        item.setStockQuantity(resultSet.getInt("stock_quantity"));
        item.setMrpPrice(resultSet.getBigDecimal("mrp_price"));
        item.setSellingPrice(resultSet.getBigDecimal("selling_price"));

        return item;
    }

    private void setNullableLong(PreparedStatement statement, int index, Long value) throws SQLException {
        if (value == null) {
            statement.setNull(index, Types.BIGINT);
        } else {
            statement.setLong(index, value);
        }
    }

    private record VariantRecord(
            Long variantId,
            Long productId,
            BigDecimal sellingPrice,
            int stockQuantity,
            boolean active,
            String productStatus
    ) {
    }

    private record WishlistItemRecord(Long wishlistItemId, Long productId, Long variantId) {
    }

    private record CartItemRecord(Long cartItemId, Long productId, Long variantId) {
    }

    private record CartItemQuantityRecord(Long cartItemId, int quantity) {
    }
}