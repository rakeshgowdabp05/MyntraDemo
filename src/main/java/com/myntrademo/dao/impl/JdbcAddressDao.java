package com.myntrademo.dao.impl;

import com.myntrademo.dao.AddressDao;
import com.myntrademo.dto.cart.CartAddressDto;
import com.myntrademo.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcAddressDao implements AddressDao {

    private static final String FIND_ACTIVE_ADDRESSES_BY_USER_ID_SQL =
            """
            SELECT
                address_id,
                user_id,
                full_name,
                phone,
                pincode,
                address_line,
                locality,
                city,
                state,
                country,
                address_type,
                is_default
            FROM user_addresses
            WHERE user_id = ?
              AND is_active = 1
            ORDER BY is_default DESC, updated_at DESC, address_id DESC
            """;

    private static final String FIND_DEFAULT_ADDRESS_BY_USER_ID_SQL =
            """
            SELECT
                address_id,
                user_id,
                full_name,
                phone,
                pincode,
                address_line,
                locality,
                city,
                state,
                country,
                address_type,
                is_default
            FROM user_addresses
            WHERE user_id = ?
              AND is_active = 1
              AND is_default = 1
            ORDER BY updated_at DESC, address_id DESC
            LIMIT 1
            """;

    private static final String COUNT_ACTIVE_ADDRESSES_SQL =
            """
            SELECT COUNT(*) AS address_count
            FROM user_addresses
            WHERE user_id = ?
              AND is_active = 1
            """;

    private static final String INSERT_ADDRESS_SQL =
            """
            INSERT INTO user_addresses
            (
                user_id,
                full_name,
                phone,
                pincode,
                address_line,
                locality,
                city,
                state,
                country,
                address_type,
                is_default,
                is_active
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1)
            """;

        private static final String UPDATE_ADDRESS_SQL =
            """
            UPDATE user_addresses
            SET full_name = ?,
                phone = ?,
                pincode = ?,
                address_line = ?,
                locality = ?,
                city = ?,
                state = ?,
                country = ?,
                address_type = ?
            WHERE user_id = ?
              AND address_id = ?
              AND is_active = 1
            """;

    private static final String CLEAR_DEFAULT_ADDRESS_SQL =
            """
            UPDATE user_addresses
            SET is_default = 0
            WHERE user_id = ?
              AND is_active = 1
            """;

    private static final String SET_DEFAULT_ADDRESS_SQL =
            """
            UPDATE user_addresses
            SET is_default = 1
            WHERE user_id = ?
              AND address_id = ?
              AND is_active = 1
            """;

    private static final String SOFT_DELETE_ADDRESS_SQL =
            """
            UPDATE user_addresses
            SET is_active = 0,
                is_default = 0
            WHERE user_id = ?
              AND address_id = ?
              AND is_active = 1
            """;

    @Override
    public List<CartAddressDto> findActiveAddressesByUserId(Long userId) throws SQLException {
        List<CartAddressDto> addresses = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ACTIVE_ADDRESSES_BY_USER_ID_SQL)) {

            statement.setLong(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    addresses.add(mapAddress(resultSet));
                }
            }
        }

        return addresses;
    }

    @Override
    public Optional<CartAddressDto> findDefaultAddressByUserId(Long userId) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_DEFAULT_ADDRESS_BY_USER_ID_SQL)) {

            statement.setLong(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapAddress(resultSet));
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public Long createAddress(Long userId, CartAddressDto address) throws SQLException {
        Connection connection = null;

        try {
            connection = DBConnection.getConnection();
            connection.setAutoCommit(false);

            boolean makeDefault = address.isDefaultAddress() || countActiveAddresses(connection, userId) == 0;

            if (makeDefault) {
                clearDefaultAddress(connection, userId);
            }

            Long addressId = insertAddress(connection, userId, address, makeDefault);

            connection.commit();
            return addressId;

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
    public void updateAddress(Long userId, CartAddressDto address) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_ADDRESS_SQL)) {

            statement.setString(1, address.getFullName());
            statement.setString(2, address.getPhone());
            statement.setString(3, address.getPincode());
            statement.setString(4, address.getAddressLine());
            statement.setString(5, address.getLocality());
            statement.setString(6, address.getCity());
            statement.setString(7, address.getState());
            statement.setString(8, address.getCountry());
            statement.setString(9, address.getAddressType());
            statement.setLong(10, userId);
            statement.setLong(11, address.getAddressId());

            int updatedRows = statement.executeUpdate();

            if (updatedRows == 0) {
                throw new IllegalArgumentException("Address not found.");
            }
        }
    }

    @Override
    public void setDefaultAddress(Long userId, Long addressId) throws SQLException {
        Connection connection = null;

        try {
            connection = DBConnection.getConnection();
            connection.setAutoCommit(false);

            clearDefaultAddress(connection, userId);

            try (PreparedStatement statement = connection.prepareStatement(SET_DEFAULT_ADDRESS_SQL)) {
                statement.setLong(1, userId);
                statement.setLong(2, addressId);

                int updatedRows = statement.executeUpdate();

                if (updatedRows == 0) {
                    throw new IllegalArgumentException("Address not found.");
                }
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
    public void deactivateAddress(Long userId, Long addressId) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SOFT_DELETE_ADDRESS_SQL)) {

            statement.setLong(1, userId);
            statement.setLong(2, addressId);

            int updatedRows = statement.executeUpdate();

            if (updatedRows == 0) {
                throw new IllegalArgumentException("Address not found.");
            }
        }
    }

    private int countActiveAddresses(Connection connection, Long userId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(COUNT_ACTIVE_ADDRESSES_SQL)) {
            statement.setLong(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("address_count");
                }
            }
        }

        return 0;
    }

    private void clearDefaultAddress(Connection connection, Long userId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(CLEAR_DEFAULT_ADDRESS_SQL)) {
            statement.setLong(1, userId);
            statement.executeUpdate();
        }
    }

    private Long insertAddress(
            Connection connection,
            Long userId,
            CartAddressDto address,
            boolean makeDefault
    ) throws SQLException {

        try (PreparedStatement statement = connection.prepareStatement(
                INSERT_ADDRESS_SQL,
                Statement.RETURN_GENERATED_KEYS
        )) {
            statement.setLong(1, userId);
            statement.setString(2, address.getFullName());
            statement.setString(3, address.getPhone());
            statement.setString(4, address.getPincode());
            statement.setString(5, address.getAddressLine());
            statement.setString(6, address.getLocality());
            statement.setString(7, address.getCity());
            statement.setString(8, address.getState());
            statement.setString(9, address.getCountry());
            statement.setString(10, address.getAddressType());
            statement.setBoolean(11, makeDefault);

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                }
            }
        }

        throw new SQLException("Address creation failed. No generated key returned.");
    }

    private CartAddressDto mapAddress(ResultSet resultSet) throws SQLException {
        CartAddressDto address = new CartAddressDto();

        address.setAddressId(resultSet.getLong("address_id"));
        address.setUserId(resultSet.getLong("user_id"));
        address.setFullName(resultSet.getString("full_name"));
        address.setPhone(resultSet.getString("phone"));
        address.setPincode(resultSet.getString("pincode"));
        address.setAddressLine(resultSet.getString("address_line"));
        address.setLocality(resultSet.getString("locality"));
        address.setCity(resultSet.getString("city"));
        address.setState(resultSet.getString("state"));
        address.setCountry(resultSet.getString("country"));
        address.setAddressType(resultSet.getString("address_type"));
        address.setDefaultAddress(resultSet.getBoolean("is_default"));

        return address;
    }
}