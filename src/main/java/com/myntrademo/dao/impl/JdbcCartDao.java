package com.myntrademo.dao.impl;

import com.myntrademo.constant.CatalogConstants;
import com.myntrademo.constant.MessageConstants;
import com.myntrademo.dao.CartDao;
import com.myntrademo.dto.cart.AddToCartRequest;
import com.myntrademo.dto.cart.CartItemDto;
import com.myntrademo.util.DBConnection;
import com.myntrademo.dto.cart.RecommendedProductDto;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcCartDao implements CartDao {

    private static final String FIND_CART_ID_BY_USER_ID_SQL =
            "SELECT cart_id FROM carts WHERE user_id = ? LIMIT 1";

    private static final String INSERT_CART_SQL =
            "INSERT INTO carts (user_id) VALUES (?)";

    private static final String FIND_VARIANT_FOR_CART_SQL =
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

    private static final String FIND_CART_ITEM_FOR_USER_SQL =
            """
            SELECT
                ci.cart_item_id,
                ci.quantity,
                pv.stock_quantity,
                pv.is_active,
                p.product_status
            FROM cart_items ci
            INNER JOIN carts c ON ci.cart_id = c.cart_id
            INNER JOIN product_variants pv ON ci.variant_id = pv.variant_id
            INNER JOIN products p ON ci.product_id = p.product_id
            WHERE ci.cart_item_id = ?
              AND c.user_id = ?
            LIMIT 1
            """;

    private static final String FIND_CART_ITEMS_SQL =
        """
        SELECT
            ci.cart_item_id,
            ci.product_id,
            ci.variant_id,
            ci.quantity,
            ci.price_at_added,
            p.product_name,
            b.brand_name,
            pv.size,
            pv.color,
            pv.price AS mrp_price,
            pv.stock_quantity,
            (
                SELECT pi.image_url
                FROM product_images pi
                WHERE pi.product_id = p.product_id
                ORDER BY pi.is_primary DESC, pi.sort_order ASC, pi.image_id ASC
                LIMIT 1
            ) AS image_url
        FROM carts c
        INNER JOIN cart_items ci ON c.cart_id = ci.cart_id
        INNER JOIN products p ON ci.product_id = p.product_id
        INNER JOIN brands b ON p.brand_id = b.brand_id
        INNER JOIN product_variants pv ON ci.variant_id = pv.variant_id
        WHERE c.user_id = ?
        ORDER BY ci.updated_at DESC, ci.cart_item_id DESC
        """;

        private static final String FIND_RECOMMENDED_PRODUCTS_SQL =
        """
        SELECT
            p.product_id,
            p.product_name,
            b.brand_name,
            c.category_name,
            COALESCE(MIN(pv.price), p.base_price) AS mrp_price,
            COALESCE(MIN(pv.selling_price), p.selling_price) AS selling_price,
            (
                SELECT pi.image_url
                FROM product_images pi
                WHERE pi.product_id = p.product_id
                ORDER BY pi.is_primary DESC, pi.sort_order ASC, pi.image_id ASC
                LIMIT 1
            ) AS image_url
        FROM products p
        INNER JOIN brands b ON p.brand_id = b.brand_id
        INNER JOIN categories c ON p.category_id = c.category_id
        INNER JOIN product_variants pv ON p.product_id = pv.product_id
        WHERE p.product_status = ?
          AND pv.is_active = 1
          AND pv.stock_quantity > 0
          AND p.product_id NOT IN (
              SELECT ci.product_id
              FROM carts cart
              INNER JOIN cart_items ci ON cart.cart_id = ci.cart_id
              WHERE cart.user_id = ?
          )
        GROUP BY
            p.product_id,
            p.product_name,
            b.brand_name,
            c.category_name,
            p.base_price,
            p.selling_price,
            p.category_id,
            p.brand_id,
            p.created_at
        ORDER BY
            CASE
                WHEN p.category_id IN (
                    SELECT cp.category_id
                    FROM carts cart2
                    INNER JOIN cart_items ci2 ON cart2.cart_id = ci2.cart_id
                    INNER JOIN products cp ON ci2.product_id = cp.product_id
                    WHERE cart2.user_id = ?
                ) THEN 1
                WHEN p.brand_id IN (
                    SELECT bp.brand_id
                    FROM carts cart3
                    INNER JOIN cart_items ci3 ON cart3.cart_id = ci3.cart_id
                    INNER JOIN products bp ON ci3.product_id = bp.product_id
                    WHERE cart3.user_id = ?
                ) THEN 2
                ELSE 3
            END,
            p.created_at DESC,
            p.product_id DESC
        LIMIT ?
        """;

    @Override
    public void addItem(Long userId, AddToCartRequest request) throws SQLException {
        Connection connection = null;

        try {
            connection = DBConnection.getConnection();
            connection.setAutoCommit(false);

            Long cartId = getOrCreateCartId(connection, userId);

            VariantRecord variantRecord = findVariantForCart(
                    connection,
                    request.getVariantId(),
                    request.getProductId()
            ).orElseThrow(() -> new IllegalArgumentException(MessageConstants.PRODUCT_NOT_AVAILABLE));

            validateVariantAvailability(variantRecord);

            Optional<CartItemRecord> existingItem = findCartItem(
                    connection,
                    cartId,
                    request.getVariantId()
            );

            if (existingItem.isPresent()) {
                CartItemRecord item = existingItem.get();
                int newQuantity = item.quantity() + request.getQuantity();

                if (newQuantity > variantRecord.stockQuantity()) {
                    throw new IllegalArgumentException(MessageConstants.PRODUCT_OUT_OF_STOCK);
                }

                updateQuantity(connection, item.cartItemId(), newQuantity);
            } else {
                if (request.getQuantity() > variantRecord.stockQuantity()) {
                    throw new IllegalArgumentException(MessageConstants.PRODUCT_OUT_OF_STOCK);
                }

                insertCartItem(connection, cartId, request, variantRecord.sellingPrice());
            }

            connection.commit();

        } catch (SQLException | RuntimeException exception) {
            if (connection != null) {
                connection.rollback();
            }

            throw exception;

        } finally {
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
    }

    @Override
    public List<CartItemDto> findCartItems(Long userId) throws SQLException {
        List<CartItemDto> items = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_CART_ITEMS_SQL)) {

            statement.setLong(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    items.add(mapCartItem(resultSet));
                }
            }
        }

        return items;
    }

    @Override
public List<RecommendedProductDto> findRecommendedProducts(Long userId, int limit) throws SQLException {
    List<RecommendedProductDto> products = new ArrayList<>();

    try (Connection connection = DBConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(FIND_RECOMMENDED_PRODUCTS_SQL)) {

        statement.setString(1, CatalogConstants.ACTIVE_PRODUCT_STATUS);
        statement.setLong(2, userId);
        statement.setLong(3, userId);
        statement.setLong(4, userId);
        statement.setInt(5, limit);

        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                products.add(mapRecommendedProduct(resultSet));
            }
        }
    }

    return products;
}

    @Override
    public void updateItemQuantity(Long userId, Long cartItemId, int quantity) throws SQLException {
        try (Connection connection = DBConnection.getConnection()) {
            CartItemStockRecord item = findCartItemForUser(connection, userId, cartItemId)
                    .orElseThrow(() -> new IllegalArgumentException(MessageConstants.CART_ITEM_NOT_FOUND));

            if (!item.active() || !CatalogConstants.ACTIVE_PRODUCT_STATUS.equals(item.productStatus())) {
                throw new IllegalArgumentException(MessageConstants.PRODUCT_NOT_AVAILABLE);
            }

            if (quantity > item.stockQuantity()) {
                throw new IllegalArgumentException(MessageConstants.PRODUCT_OUT_OF_STOCK);
            }

            updateQuantity(connection, cartItemId, quantity);
        }
    }

    @Override
    public void removeItem(Long userId, Long cartItemId) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_CART_ITEM_SQL)) {

            statement.setLong(1, cartItemId);
            statement.setLong(2, userId);

            int updatedRows = statement.executeUpdate();

            if (updatedRows == 0) {
                throw new IllegalArgumentException(MessageConstants.CART_ITEM_NOT_FOUND);
            }
        }
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

        throw new SQLException("Cart creation failed. No generated key returned.");
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

    private Optional<VariantRecord> findVariantForCart(
            Connection connection,
            Long variantId,
            Long productId
    ) throws SQLException {

        try (PreparedStatement statement = connection.prepareStatement(FIND_VARIANT_FOR_CART_SQL)) {
            statement.setLong(1, variantId);
            statement.setLong(2, productId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(new VariantRecord(
                            resultSet.getLong("variant_id"),
                            resultSet.getLong("product_id"),
                            resultSet.getBigDecimal("selling_price"),
                            resultSet.getInt("stock_quantity"),
                            resultSet.getBoolean("is_active"),
                            resultSet.getString("product_status")
                    ));
                }
            }
        }

        return Optional.empty();
    }

    private Optional<CartItemRecord> findCartItem(
            Connection connection,
            Long cartId,
            Long variantId
    ) throws SQLException {

        try (PreparedStatement statement = connection.prepareStatement(FIND_CART_ITEM_SQL)) {
            statement.setLong(1, cartId);
            statement.setLong(2, variantId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(new CartItemRecord(
                            resultSet.getLong("cart_item_id"),
                            resultSet.getInt("quantity")
                    ));
                }
            }
        }

        return Optional.empty();
    }

    private Optional<CartItemStockRecord> findCartItemForUser(
            Connection connection,
            Long userId,
            Long cartItemId
    ) throws SQLException {

        try (PreparedStatement statement = connection.prepareStatement(FIND_CART_ITEM_FOR_USER_SQL)) {
            statement.setLong(1, cartItemId);
            statement.setLong(2, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(new CartItemStockRecord(
                            resultSet.getInt("quantity"),
                            resultSet.getInt("stock_quantity"),
                            resultSet.getBoolean("is_active"),
                            resultSet.getString("product_status")
                    ));
                }
            }
        }

        return Optional.empty();
    }

    private void validateVariantAvailability(VariantRecord variantRecord) {
        if (!variantRecord.active()
                || !CatalogConstants.ACTIVE_PRODUCT_STATUS.equals(variantRecord.productStatus())) {
            throw new IllegalArgumentException(MessageConstants.PRODUCT_NOT_AVAILABLE);
        }

        if (variantRecord.stockQuantity() <= 0) {
            throw new IllegalArgumentException(MessageConstants.PRODUCT_OUT_OF_STOCK);
        }
    }

    private void insertCartItem(
            Connection connection,
            Long cartId,
            AddToCartRequest request,
            BigDecimal sellingPrice
    ) throws SQLException {

        try (PreparedStatement statement = connection.prepareStatement(INSERT_CART_ITEM_SQL)) {
            statement.setLong(1, cartId);
            statement.setLong(2, request.getProductId());
            statement.setLong(3, request.getVariantId());
            statement.setInt(4, request.getQuantity());
            statement.setBigDecimal(5, sellingPrice);
            statement.executeUpdate();
        }
    }

    private void updateQuantity(Connection connection, Long cartItemId, int quantity) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_CART_ITEM_QUANTITY_SQL)) {
            statement.setInt(1, quantity);
            statement.setLong(2, cartItemId);
            statement.executeUpdate();
        }
    }

    private RecommendedProductDto mapRecommendedProduct(ResultSet resultSet) throws SQLException {
    RecommendedProductDto product = new RecommendedProductDto();

    product.setProductId(resultSet.getLong("product_id"));
    product.setProductName(resultSet.getString("product_name"));
    product.setBrandName(resultSet.getString("brand_name"));
    product.setCategoryName(resultSet.getString("category_name"));
    product.setImageUrl(resultSet.getString("image_url"));
    product.setMrpPrice(resultSet.getBigDecimal("mrp_price"));
    product.setSellingPrice(resultSet.getBigDecimal("selling_price"));

    return product;
}

    private CartItemDto mapCartItem(ResultSet resultSet) throws SQLException {
    CartItemDto item = new CartItemDto();

    item.setCartItemId(resultSet.getLong("cart_item_id"));
    item.setProductId(resultSet.getLong("product_id"));
    item.setVariantId(resultSet.getLong("variant_id"));
    item.setProductName(resultSet.getString("product_name"));
    item.setBrandName(resultSet.getString("brand_name"));
    item.setSize(resultSet.getString("size"));
    item.setColor(resultSet.getString("color"));
    item.setImageUrl(resultSet.getString("image_url"));
    item.setQuantity(resultSet.getInt("quantity"));
    item.setStockQuantity(resultSet.getInt("stock_quantity"));
    item.setMrpPrice(resultSet.getBigDecimal("mrp_price"));
    item.setPriceAtAdded(resultSet.getBigDecimal("price_at_added"));

    return item;
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

    private record CartItemRecord(Long cartItemId, int quantity) {
    }

    private record CartItemStockRecord(
            int quantity,
            int stockQuantity,
            boolean active,
            String productStatus
    ) {
    }
}