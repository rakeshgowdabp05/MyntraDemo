package com.myntrademo.dao.impl;

import com.myntrademo.constant.CatalogConstants;
import com.myntrademo.dao.ProductDao;
import com.myntrademo.dto.catalog.ProductAddressDto;
import com.myntrademo.dto.catalog.ProductCardDto;
import com.myntrademo.dto.catalog.ProductDetailDto;
import com.myntrademo.dto.catalog.ProductFilterRequest;
import com.myntrademo.dto.catalog.ProductImageDto;
import com.myntrademo.dto.catalog.ProductOfferDto;
import com.myntrademo.dto.catalog.ProductReviewDto;
import com.myntrademo.dto.catalog.ProductReviewSummaryDto;
import com.myntrademo.dto.catalog.ProductSellerDto;
import com.myntrademo.dto.catalog.ProductServicePromiseDto;
import com.myntrademo.dto.catalog.ProductSpecificationDto;
import com.myntrademo.dto.catalog.ProductVariantDto;
import com.myntrademo.util.DBConnection;
import com.myntrademo.util.ValidationUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class JdbcProductDao implements ProductDao {

    private static final String PRODUCT_CARD_SELECT =
            """
            SELECT
                p.product_id,
                p.product_name,
                p.base_price,
                p.selling_price,
                c.category_name,
                b.brand_name,
                (
                    SELECT pi.image_url
                    FROM product_images pi
                    WHERE pi.product_id = p.product_id
                    ORDER BY pi.is_primary DESC, pi.sort_order ASC, pi.image_id ASC
                    LIMIT 1
                ) AS primary_image_url
            FROM products p
            INNER JOIN categories c ON p.category_id = c.category_id
            INNER JOIN brands b ON p.brand_id = b.brand_id
            """;

    private static final String COUNT_SELECT =
            """
            SELECT COUNT(*) AS total_count
            FROM products p
            INNER JOIN categories c ON p.category_id = c.category_id
            INNER JOIN brands b ON p.brand_id = b.brand_id
            """;

    private static final String PRODUCT_DETAIL_SQL =
            """
            SELECT
                p.product_id,
                p.category_id,
                p.brand_id,
                p.product_name,
                p.slug,
                p.short_description,
                COALESCE(p.long_description, p.short_description) AS description,
                p.base_price,
                p.selling_price,
                c.category_name,
                b.brand_name
            FROM products p
            INNER JOIN categories c ON p.category_id = c.category_id
            INNER JOIN brands b ON p.brand_id = b.brand_id
            WHERE p.product_id = ?
              AND p.product_status = ?
              AND c.is_active = TRUE
              AND b.is_active = TRUE
            LIMIT 1
            """;

    private static final String PRODUCT_IMAGES_SQL =
            """
            SELECT image_url, alt_text, is_primary
            FROM product_images
            WHERE product_id = ?
            ORDER BY is_primary DESC, sort_order ASC, image_id ASC
            """;

    private static final String PRODUCT_VARIANTS_SQL =
            """
            SELECT
                variant_id,
                sku,
                size,
                color,
                price,
                selling_price,
                stock_quantity,
                is_active
            FROM product_variants
            WHERE product_id = ?
              AND is_active = TRUE
            ORDER BY
                CASE
                    WHEN size REGEXP '^[0-9]+$' THEN CAST(size AS UNSIGNED)
                    WHEN size = 'XS' THEN 100
                    WHEN size = 'S' THEN 101
                    WHEN size = 'M' THEN 102
                    WHEN size = 'L' THEN 103
                    WHEN size = 'XL' THEN 104
                    WHEN size = 'XXL' THEN 105
                    WHEN size = 'Free Size' THEN 106
                    ELSE 200
                END,
                variant_id ASC
            """;

    private static final String PRODUCT_SPECS_SQL =
            """
            SELECT spec_name, spec_value
            FROM product_specifications
            WHERE product_id = ?
            ORDER BY sort_order ASC, spec_id ASC
            """;

    private static final String PRODUCT_REVIEW_SUMMARY_SQL =
            """
            SELECT
                COALESCE(ROUND(AVG(rating), 1), 0) AS average_rating,
                COUNT(*) AS total_reviews,
                SUM(CASE WHEN rating = 5 THEN 1 ELSE 0 END) AS five_star_count,
                SUM(CASE WHEN rating = 4 THEN 1 ELSE 0 END) AS four_star_count,
                SUM(CASE WHEN rating = 3 THEN 1 ELSE 0 END) AS three_star_count,
                SUM(CASE WHEN rating = 2 THEN 1 ELSE 0 END) AS two_star_count,
                SUM(CASE WHEN rating = 1 THEN 1 ELSE 0 END) AS one_star_count
            FROM product_reviews
            WHERE product_id = ?
              AND is_approved = TRUE
            """;

    private static final String PRODUCT_REVIEWS_SQL =
            """
            SELECT
                pr.rating,
                pr.review_title,
                pr.review_text,
                pr.is_verified_purchase,
                DATE_FORMAT(pr.created_at, '%d %b %Y') AS review_date,
                u.full_name AS reviewer_name
            FROM product_reviews pr
            INNER JOIN users u ON pr.user_id = u.user_id
            WHERE pr.product_id = ?
              AND pr.is_approved = TRUE
            ORDER BY pr.created_at DESC, pr.review_id DESC
            LIMIT ?
            """;

    private static final String PRODUCT_OFFERS_SQL =
            """
            SELECT offer_title, offer_description, terms_text
            FROM product_offers
            WHERE is_active = TRUE
              AND (product_id IS NULL OR product_id = ?)
              AND (start_date IS NULL OR start_date <= CURRENT_TIMESTAMP)
              AND (end_date IS NULL OR end_date >= CURRENT_TIMESTAMP)
            ORDER BY CASE WHEN product_id = ? THEN 0 ELSE 1 END, sort_order ASC, offer_id ASC
            """;

    private static final String PRODUCT_SERVICE_PROMISES_SQL =
            """
            SELECT promise_title, promise_subtitle, icon_key
            FROM product_service_promises
            WHERE is_active = TRUE
              AND (product_id IS NULL OR product_id = ?)
            ORDER BY CASE WHEN product_id = ? THEN 0 ELSE 1 END, sort_order ASC, promise_id ASC
            """;

    private static final String PRODUCT_SELLER_SQL =
            """
            SELECT
                seller_name,
                delivery_min_days,
                delivery_max_days,
                return_policy,
                exchange_policy,
                cod_available,
                original_product_text
            FROM product_seller_details
            WHERE product_id = ?
              AND is_active = TRUE
            LIMIT 1
            """;

    private static final String DEFAULT_ADDRESS_SQL =
            """
            SELECT
                full_name,
                pincode,
                CONCAT_WS(', ', address_line1, address_line2, city, state) AS address_line
            FROM addresses
            WHERE user_id = ?
            ORDER BY is_default DESC, updated_at DESC, address_id DESC
            LIMIT 1
            """;

    private static final String SIMILAR_PRODUCTS_SQL =
            """
            SELECT
                p.product_id,
                p.product_name,
                p.base_price,
                p.selling_price,
                c.category_name,
                b.brand_name,
                (
                    SELECT pi.image_url
                    FROM product_images pi
                    WHERE pi.product_id = p.product_id
                    ORDER BY pi.is_primary DESC, pi.sort_order ASC, pi.image_id ASC
                    LIMIT 1
                ) AS primary_image_url
            FROM products p
            INNER JOIN categories c ON p.category_id = c.category_id
            INNER JOIN brands b ON p.brand_id = b.brand_id
            WHERE p.product_status = ?
              AND c.is_active = TRUE
              AND b.is_active = TRUE
              AND p.product_id <> ?
              AND (p.category_id = ? OR p.brand_id = ?)
            ORDER BY
                CASE WHEN p.brand_id = ? THEN 0 ELSE 1 END,
                p.created_at DESC,
                p.product_id DESC
            LIMIT ?
            """;

    @Override
    public List<ProductCardDto> findProducts(ProductFilterRequest filterRequest) throws SQLException {
        List<ProductCardDto> products = new ArrayList<>();

        String sql = PRODUCT_CARD_SELECT
                + buildWhereClause(filterRequest)
                + buildSortClause(filterRequest.getSortBy())
                + " LIMIT ? OFFSET ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            int index = bindFilterParameters(statement, filterRequest);
            statement.setInt(index++, filterRequest.getPageSize());
            statement.setInt(index, filterRequest.getOffset());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    products.add(mapProductCard(resultSet));
                }
            }
        }

        return products;
    }

    @Override
    public int countProducts(ProductFilterRequest filterRequest) throws SQLException {
        String sql = COUNT_SELECT + buildWhereClause(filterRequest);

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            bindFilterParameters(statement, filterRequest);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("total_count");
                }
            }
        }

        return 0;
    }

    @Override
    public Optional<ProductDetailDto> findProductDetailById(Long productId) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(PRODUCT_DETAIL_SQL)) {

            statement.setLong(1, productId);
            statement.setString(2, CatalogConstants.ACTIVE_PRODUCT_STATUS);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapProductDetail(resultSet));
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public List<ProductImageDto> findImagesByProductId(Long productId) throws SQLException {
        List<ProductImageDto> images = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(PRODUCT_IMAGES_SQL)) {

            statement.setLong(1, productId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    images.add(new ProductImageDto(
                            resultSet.getString("image_url"),
                            resultSet.getString("alt_text"),
                            resultSet.getBoolean("is_primary")
                    ));
                }
            }
        }

        return images;
    }

    @Override
    public List<ProductVariantDto> findActiveVariantsByProductId(Long productId) throws SQLException {
        List<ProductVariantDto> variants = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(PRODUCT_VARIANTS_SQL)) {

            statement.setLong(1, productId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    variants.add(mapProductVariant(resultSet));
                }
            }
        }

        return variants;
    }

    @Override
    public List<ProductSpecificationDto> findSpecificationsByProductId(Long productId) throws SQLException {
        List<ProductSpecificationDto> specifications = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(PRODUCT_SPECS_SQL)) {

            statement.setLong(1, productId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    specifications.add(new ProductSpecificationDto(
                            resultSet.getString("spec_name"),
                            resultSet.getString("spec_value")
                    ));
                }
            }
        }

        return specifications;
    }

    @Override
    public ProductReviewSummaryDto findReviewSummaryByProductId(Long productId) throws SQLException {
        ProductReviewSummaryDto summary = new ProductReviewSummaryDto();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(PRODUCT_REVIEW_SUMMARY_SQL)) {

            statement.setLong(1, productId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    summary.setAverageRating(resultSet.getBigDecimal("average_rating"));
                    summary.setTotalReviews(resultSet.getInt("total_reviews"));
                    summary.setFiveStarCount(resultSet.getInt("five_star_count"));
                    summary.setFourStarCount(resultSet.getInt("four_star_count"));
                    summary.setThreeStarCount(resultSet.getInt("three_star_count"));
                    summary.setTwoStarCount(resultSet.getInt("two_star_count"));
                    summary.setOneStarCount(resultSet.getInt("one_star_count"));
                }
            }
        }

        return summary;
    }

    @Override
    public List<ProductReviewDto> findApprovedReviewsByProductId(Long productId, int limit) throws SQLException {
        List<ProductReviewDto> reviews = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(PRODUCT_REVIEWS_SQL)) {

            statement.setLong(1, productId);
            statement.setInt(2, limit);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    ProductReviewDto review = new ProductReviewDto();
                    review.setRating(resultSet.getInt("rating"));
                    review.setReviewTitle(resultSet.getString("review_title"));
                    review.setReviewText(resultSet.getString("review_text"));
                    review.setReviewerName(resultSet.getString("reviewer_name"));
                    review.setReviewDate(resultSet.getString("review_date"));
                    review.setVerifiedPurchase(resultSet.getBoolean("is_verified_purchase"));
                    reviews.add(review);
                }
            }
        }

        return reviews;
    }

    @Override
    public List<ProductCardDto> findSimilarProducts(Long productId, Long categoryId, Long brandId, int limit)
            throws SQLException {

        List<ProductCardDto> products = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SIMILAR_PRODUCTS_SQL)) {

            statement.setString(1, CatalogConstants.ACTIVE_PRODUCT_STATUS);
            statement.setLong(2, productId);
            statement.setLong(3, categoryId);
            statement.setLong(4, brandId);
            statement.setLong(5, brandId);
            statement.setInt(6, limit);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    products.add(mapProductCard(resultSet));
                }
            }
        }

        return products;
    }

    @Override
    public List<ProductOfferDto> findActiveOffersByProductId(Long productId) throws SQLException {
        List<ProductOfferDto> offers = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(PRODUCT_OFFERS_SQL)) {

            statement.setLong(1, productId);
            statement.setLong(2, productId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    offers.add(new ProductOfferDto(
                            resultSet.getString("offer_title"),
                            resultSet.getString("offer_description"),
                            resultSet.getString("terms_text")
                    ));
                }
            }
        }

        return offers;
    }

    @Override
    public List<ProductServicePromiseDto> findActiveServicePromisesByProductId(Long productId) throws SQLException {
        List<ProductServicePromiseDto> promises = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(PRODUCT_SERVICE_PROMISES_SQL)) {

            statement.setLong(1, productId);
            statement.setLong(2, productId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    promises.add(new ProductServicePromiseDto(
                            resultSet.getString("promise_title"),
                            resultSet.getString("promise_subtitle"),
                            resultSet.getString("icon_key")
                    ));
                }
            }
        }

        return promises;
    }

    @Override
    public Optional<ProductSellerDto> findSellerByProductId(Long productId) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(PRODUCT_SELLER_SQL)) {

            statement.setLong(1, productId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    ProductSellerDto seller = new ProductSellerDto();
                    seller.setSellerName(resultSet.getString("seller_name"));
                    seller.setDeliveryMinDays(resultSet.getInt("delivery_min_days"));
                    seller.setDeliveryMaxDays(resultSet.getInt("delivery_max_days"));
                    seller.setReturnPolicy(resultSet.getString("return_policy"));
                    seller.setExchangePolicy(resultSet.getString("exchange_policy"));
                    seller.setCodAvailable(resultSet.getBoolean("cod_available"));
                    seller.setOriginalProductText(resultSet.getString("original_product_text"));
                    return Optional.of(seller);
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<ProductAddressDto> findDefaultDeliveryAddress(Long userId) throws SQLException {
        if (userId == null) {
            return Optional.empty();
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(DEFAULT_ADDRESS_SQL)) {

            statement.setLong(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(new ProductAddressDto(
                            resultSet.getString("full_name"),
                            resultSet.getString("pincode"),
                            resultSet.getString("address_line")
                    ));
                }
            }
        }

        return Optional.empty();
    }

    private String buildWhereClause(ProductFilterRequest filterRequest) {
        StringBuilder where = new StringBuilder();

        where.append(" WHERE p.product_status = ? AND c.is_active = TRUE AND b.is_active = TRUE ");

        if (!ValidationUtil.isBlank(filterRequest.getSearch())) {
            where.append(" AND (p.product_name LIKE ? OR b.brand_name LIKE ? OR c.category_name LIKE ?) ");
        }

        if (filterRequest.getCategoryId() != null) {
            where.append(" AND p.category_id = ? ");
        }

        if (filterRequest.getBrandId() != null) {
            where.append(" AND p.brand_id = ? ");
        }

        return where.toString();
    }

    private int bindFilterParameters(PreparedStatement statement, ProductFilterRequest filterRequest)
            throws SQLException {

        int index = 1;

        statement.setString(index++, CatalogConstants.ACTIVE_PRODUCT_STATUS);

        if (!ValidationUtil.isBlank(filterRequest.getSearch())) {
            String searchPattern = "%" + filterRequest.getSearch().trim() + "%";
            statement.setString(index++, searchPattern);
            statement.setString(index++, searchPattern);
            statement.setString(index++, searchPattern);
        }

        if (filterRequest.getCategoryId() != null) {
            statement.setLong(index++, filterRequest.getCategoryId());
        }

        if (filterRequest.getBrandId() != null) {
            statement.setLong(index++, filterRequest.getBrandId());
        }

        return index;
    }

    private String buildSortClause(String sortBy) {
        Set<String> allowedSorts = new HashSet<>();
        allowedSorts.add(CatalogConstants.SORT_NEWEST);
        allowedSorts.add(CatalogConstants.SORT_PRICE_LOW_HIGH);
        allowedSorts.add(CatalogConstants.SORT_PRICE_HIGH_LOW);
        allowedSorts.add(CatalogConstants.SORT_NAME_ASC);

        String safeSort = allowedSorts.contains(sortBy) ? sortBy : CatalogConstants.DEFAULT_SORT;

        return switch (safeSort) {
            case CatalogConstants.SORT_PRICE_LOW_HIGH -> " ORDER BY p.selling_price ASC, p.product_id DESC ";
            case CatalogConstants.SORT_PRICE_HIGH_LOW -> " ORDER BY p.selling_price DESC, p.product_id DESC ";
            case CatalogConstants.SORT_NAME_ASC -> " ORDER BY p.product_name ASC ";
            default -> " ORDER BY p.created_at DESC, p.product_id DESC ";
        };
    }

    private ProductCardDto mapProductCard(ResultSet resultSet) throws SQLException {
        ProductCardDto product = new ProductCardDto();

        BigDecimal basePrice = resultSet.getBigDecimal("base_price");
        BigDecimal sellingPrice = resultSet.getBigDecimal("selling_price");

        product.setProductId(resultSet.getLong("product_id"));
        product.setProductName(resultSet.getString("product_name"));
        product.setBrandName(resultSet.getString("brand_name"));
        product.setCategoryName(resultSet.getString("category_name"));
        product.setBasePrice(basePrice);
        product.setSellingPrice(sellingPrice);
        product.setDiscountPercent(calculateDiscountPercent(basePrice, sellingPrice));
        product.setPrimaryImageUrl(resultSet.getString("primary_image_url"));

        return product;
    }

    private ProductDetailDto mapProductDetail(ResultSet resultSet) throws SQLException {
        ProductDetailDto product = new ProductDetailDto();

        BigDecimal basePrice = resultSet.getBigDecimal("base_price");
        BigDecimal sellingPrice = resultSet.getBigDecimal("selling_price");

        product.setProductId(resultSet.getLong("product_id"));
        product.setCategoryId(resultSet.getLong("category_id"));
        product.setBrandId(resultSet.getLong("brand_id"));
        product.setProductName(resultSet.getString("product_name"));
        product.setSlug(resultSet.getString("slug"));
        product.setShortDescription(resultSet.getString("short_description"));
        product.setDescription(resultSet.getString("description"));
        product.setBrandName(resultSet.getString("brand_name"));
        product.setCategoryName(resultSet.getString("category_name"));
        product.setBasePrice(basePrice);
        product.setSellingPrice(sellingPrice);
        product.setDiscountPercent(calculateDiscountPercent(basePrice, sellingPrice));

        return product;
    }

    private ProductVariantDto mapProductVariant(ResultSet resultSet) throws SQLException {
        ProductVariantDto variant = new ProductVariantDto();

        variant.setVariantId(resultSet.getLong("variant_id"));
        variant.setSku(resultSet.getString("sku"));
        variant.setSize(resultSet.getString("size"));
        variant.setColor(resultSet.getString("color"));
        variant.setPrice(resultSet.getBigDecimal("price"));
        variant.setSellingPrice(resultSet.getBigDecimal("selling_price"));
        variant.setStockQuantity(resultSet.getInt("stock_quantity"));
        variant.setActive(resultSet.getBoolean("is_active"));

        return variant;
    }

    private Integer calculateDiscountPercent(BigDecimal basePrice, BigDecimal sellingPrice) {
        if (basePrice == null || sellingPrice == null || basePrice.compareTo(BigDecimal.ZERO) <= 0) {
            return 0;
        }

        if (basePrice.compareTo(sellingPrice) <= 0) {
            return 0;
        }

        return basePrice
                .subtract(sellingPrice)
                .multiply(BigDecimal.valueOf(100))
                .divide(basePrice, 0, RoundingMode.HALF_UP)
                .intValue();
    }
}
