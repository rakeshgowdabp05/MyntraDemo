package com.myntrademo.dao.impl;

import com.myntrademo.dao.RoleDao;
import com.myntrademo.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class JdbcRoleDao implements RoleDao {

    private static final String FIND_ROLE_ID_BY_NAME_SQL =
            "SELECT role_id FROM roles WHERE role_name = ?";

    @Override
    public Optional<Long> findRoleIdByName(String roleName) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ROLE_ID_BY_NAME_SQL)) {

            statement.setString(1, roleName);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(resultSet.getLong("role_id"));
                }
            }
        }

        return Optional.empty();
    }
}