package com.myntrademo.dao.impl;

import com.myntrademo.dao.BrandDao;
import com.myntrademo.dto.catalog.BrandDto;
import com.myntrademo.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcBrandDao implements BrandDao {

    private static final String FIND_ALL_ACTIVE_SQL =
            """
            SELECT brand_id, brand_name
            FROM brands
            WHERE is_active = TRUE
            ORDER BY brand_name ASC
            """;

    @Override
    public List<BrandDto> findAllActive() throws SQLException {
        List<BrandDto> brands = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_ACTIVE_SQL);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                brands.add(new BrandDto(
                        resultSet.getLong("brand_id"),
                        resultSet.getString("brand_name")
                ));
            }
        }

        return brands;
    }
}