package com.myntrademo.dao.impl;

import com.myntrademo.dao.CategoryDao;
import com.myntrademo.dto.catalog.CategoryDto;
import com.myntrademo.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcCategoryDao implements CategoryDao {

    private static final String FIND_ALL_ACTIVE_SQL =
            """
            SELECT category_id, category_name
            FROM categories
            WHERE is_active = TRUE
            ORDER BY category_name ASC
            """;

    @Override
    public List<CategoryDto> findAllActive() throws SQLException {
        List<CategoryDto> categories = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_ACTIVE_SQL);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                categories.add(new CategoryDto(
                        resultSet.getLong("category_id"),
                        resultSet.getString("category_name")
                ));
            }
        }

        return categories;
    }
}