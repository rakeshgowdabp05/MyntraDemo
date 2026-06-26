package com.myntrademo.service.impl;

import com.myntrademo.dto.home.HomeBrandDto;
import com.myntrademo.dto.home.HomeCategoryDto;
import com.myntrademo.dto.home.HomePageDto;
import com.myntrademo.dto.home.HomeProductDto;
import com.myntrademo.service.HomePageService;
import com.myntrademo.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HomePageServiceImpl implements HomePageService {

    private static final String ACTIVE_PRODUCT_STATUS = "ACTIVE";

    @Override
    public HomePageDto getHomePage() throws SQLException {
        HomePageDto homePage = new HomePageDto();

        homePage.setHeroProducts(findDiscountProducts(8));
        homePage.setCategories(findCategories(14));
        homePage.setDealProducts(findDiscountProducts(12));
        homePage.setNewArrivals(findNewestProducts(12));
        homePage.setTrendingProducts(findTrendingProducts(12));
        homePage.setBrands(findBrands(10));

        return homePage;
    }

    private List<HomeProductDto> findDiscountProducts(int limit) throws SQLException {
        String sql = """
                SELECT
                    p.product_id,
                    p.product_name,
                    b.brand_name,
                    c.category_name,
                    p.base_price,
                    p.selling_price,
                    CASE
                        WHEN p.base_price > 0 AND p.base_price > p.selling_price
                        THEN ROUND(((p.base_price - p.selling_price) / p.base_price) * 100)
                        ELSE 0
                    END AS discount_percent,
                    (
                        SELECT pi.image_url
                        FROM product_images pi
                        WHERE pi.product_id = p.product_id
                        ORDER BY pi.is_primary DESC, pi.image_id ASC
                        LIMIT 1
                    ) AS image_url
                FROM products p
                LEFT JOIN brands b ON b.brand_id = p.brand_id
                LEFT JOIN categories c ON c.category_id = p.category_id
                WHERE p.product_status = ?
                ORDER BY discount_percent DESC, p.product_id DESC
                LIMIT ?
                """;

        return findProducts(sql, limit);
    }

    private List<HomeProductDto> findNewestProducts(int limit) throws SQLException {
        String sql = """
                SELECT
                    p.product_id,
                    p.product_name,
                    b.brand_name,
                    c.category_name,
                    p.base_price,
                    p.selling_price,
                    CASE
                        WHEN p.base_price > 0 AND p.base_price > p.selling_price
                        THEN ROUND(((p.base_price - p.selling_price) / p.base_price) * 100)
                        ELSE 0
                    END AS discount_percent,
                    (
                        SELECT pi.image_url
                        FROM product_images pi
                        WHERE pi.product_id = p.product_id
                        ORDER BY pi.is_primary DESC, pi.image_id ASC
                        LIMIT 1
                    ) AS image_url
                FROM products p
                LEFT JOIN brands b ON b.brand_id = p.brand_id
                LEFT JOIN categories c ON c.category_id = p.category_id
                WHERE p.product_status = ?
                ORDER BY p.product_id DESC
                LIMIT ?
                """;

        return findProducts(sql, limit);
    }

    private List<HomeProductDto> findTrendingProducts(int limit) throws SQLException {
        String sql = """
                SELECT
                    p.product_id,
                    p.product_name,
                    b.brand_name,
                    c.category_name,
                    p.base_price,
                    p.selling_price,
                    CASE
                        WHEN p.base_price > 0 AND p.base_price > p.selling_price
                        THEN ROUND(((p.base_price - p.selling_price) / p.base_price) * 100)
                        ELSE 0
                    END AS discount_percent,
                    (
                        SELECT pi.image_url
                        FROM product_images pi
                        WHERE pi.product_id = p.product_id
                        ORDER BY pi.is_primary DESC, pi.image_id ASC
                        LIMIT 1
                    ) AS image_url
                FROM products p
                LEFT JOIN brands b ON b.brand_id = p.brand_id
                LEFT JOIN categories c ON c.category_id = p.category_id
                WHERE p.product_status = ?
                ORDER BY p.selling_price ASC, p.product_id DESC
                LIMIT ?
                """;

        return findProducts(sql, limit);
    }

    private List<HomeProductDto> findProducts(String sql, int limit) throws SQLException {
        List<HomeProductDto> products = new ArrayList<>();

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, ACTIVE_PRODUCT_STATUS);
            statement.setInt(2, limit);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    products.add(mapProduct(resultSet));
                }
            }
        }

        return products;
    }

    private List<HomeCategoryDto> findCategories(int limit) throws SQLException {
        String sql = """
                SELECT
                    c.category_id,
                    c.category_name,
                    COUNT(p.product_id) AS product_count,
                    (
                        SELECT pi.image_url
                        FROM products inner_p
                        INNER JOIN product_images pi ON pi.product_id = inner_p.product_id
                        WHERE inner_p.category_id = c.category_id
                          AND inner_p.product_status = ?
                        ORDER BY pi.is_primary DESC, inner_p.product_id DESC, pi.image_id ASC
                        LIMIT 1
                    ) AS image_url
                FROM categories c
                INNER JOIN products p
                    ON p.category_id = c.category_id
                   AND p.product_status = ?
                GROUP BY c.category_id, c.category_name
                ORDER BY product_count DESC, c.category_name ASC
                LIMIT ?
                """;

        List<HomeCategoryDto> categories = new ArrayList<>();

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, ACTIVE_PRODUCT_STATUS);
            statement.setString(2, ACTIVE_PRODUCT_STATUS);
            statement.setInt(3, limit);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    HomeCategoryDto category = new HomeCategoryDto();
                    category.setCategoryId(resultSet.getLong("category_id"));
                    category.setCategoryName(resultSet.getString("category_name"));
                    category.setProductCount(resultSet.getInt("product_count"));
                    category.setImageUrl(resultSet.getString("image_url"));
                    categories.add(category);
                }
            }
        }

        return categories;
    }

    private List<HomeBrandDto> findBrands(int limit) throws SQLException {
        String sql = """
                SELECT
                    b.brand_id,
                    b.brand_name,
                    COUNT(p.product_id) AS product_count,
                    (
                        SELECT pi.image_url
                        FROM products inner_p
                        INNER JOIN product_images pi ON pi.product_id = inner_p.product_id
                        WHERE inner_p.brand_id = b.brand_id
                          AND inner_p.product_status = ?
                        ORDER BY pi.is_primary DESC, inner_p.product_id DESC, pi.image_id ASC
                        LIMIT 1
                    ) AS image_url
                FROM brands b
                INNER JOIN products p
                    ON p.brand_id = b.brand_id
                   AND p.product_status = ?
                GROUP BY b.brand_id, b.brand_name
                ORDER BY product_count DESC, b.brand_name ASC
                LIMIT ?
                """;

        List<HomeBrandDto> brands = new ArrayList<>();

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, ACTIVE_PRODUCT_STATUS);
            statement.setString(2, ACTIVE_PRODUCT_STATUS);
            statement.setInt(3, limit);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    HomeBrandDto brand = new HomeBrandDto();
                    brand.setBrandId(resultSet.getLong("brand_id"));
                    brand.setBrandName(resultSet.getString("brand_name"));
                    brand.setProductCount(resultSet.getInt("product_count"));
                    brand.setImageUrl(resultSet.getString("image_url"));
                    brands.add(brand);
                }
            }
        }

        return brands;
    }

    private HomeProductDto mapProduct(ResultSet resultSet) throws SQLException {
        HomeProductDto product = new HomeProductDto();

        product.setProductId(resultSet.getLong("product_id"));
        product.setProductName(resultSet.getString("product_name"));
        product.setBrandName(resultSet.getString("brand_name"));
        product.setCategoryName(resultSet.getString("category_name"));
        product.setBasePrice(resultSet.getBigDecimal("base_price"));
        product.setSellingPrice(resultSet.getBigDecimal("selling_price"));
        product.setDiscountPercent(resultSet.getInt("discount_percent"));
        product.setImageUrl(resultSet.getString("image_url"));

        return product;
    }
}