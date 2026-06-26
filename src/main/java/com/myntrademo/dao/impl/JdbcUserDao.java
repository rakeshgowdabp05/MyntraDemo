package com.myntrademo.dao.impl;

import com.myntrademo.dao.UserDao;
import com.myntrademo.model.User;
import com.myntrademo.util.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class JdbcUserDao implements UserDao {

    private static final String EXISTS_BY_EMAIL_SQL =
            "SELECT user_id FROM users WHERE email = ? LIMIT 1";

    private static final String EXISTS_BY_PHONE_SQL =
            "SELECT user_id FROM users WHERE phone = ? LIMIT 1";

    private static final String EXISTS_BY_PHONE_FOR_OTHER_USER_SQL =
            """
            SELECT user_id
            FROM users
            WHERE phone = ?
              AND user_id <> ?
            LIMIT 1
            """;

    private static final String INSERT_USER_SQL =
            """
            INSERT INTO users
            (role_id, full_name, email, phone, password_hash, is_active)
            VALUES (?, ?, ?, ?, ?, TRUE)
            """;

    private static final String INSERT_CART_SQL =
            "INSERT INTO carts (user_id) VALUES (?)";

    private static final String INSERT_WISHLIST_SQL =
            "INSERT INTO wishlists (user_id) VALUES (?)";

    private static final String FIND_BY_EMAIL_FOR_LOGIN_SQL =
            """
            SELECT
                u.user_id,
                u.role_id,
                r.role_name,
                u.full_name,
                u.email,
                u.phone,
                u.password_hash,
                u.profile_image,
                u.gender,
                u.date_of_birth,
                u.is_active,
                u.is_email_verified,
                u.is_phone_verified,
                u.last_login_at,
                u.created_at
            FROM users u
            INNER JOIN roles r ON u.role_id = r.role_id
            WHERE u.email = ?
            LIMIT 1
            """;

    private static final String FIND_BY_ID_SQL =
            """
            SELECT
                u.user_id,
                u.role_id,
                r.role_name,
                u.full_name,
                u.email,
                u.phone,
                u.password_hash,
                u.profile_image,
                u.gender,
                u.date_of_birth,
                u.is_active,
                u.is_email_verified,
                u.is_phone_verified,
                u.last_login_at,
                u.created_at
            FROM users u
            INNER JOIN roles r ON u.role_id = r.role_id
            WHERE u.user_id = ?
              AND u.is_active = TRUE
            LIMIT 1
            """;

    private static final String UPDATE_PROFILE_SQL =
            """
            UPDATE users
            SET full_name = ?,
                phone = ?,
                gender = ?,
                date_of_birth = ?
            WHERE user_id = ?
              AND is_active = TRUE
            """;

    private static final String UPDATE_LAST_LOGIN_SQL =
            "UPDATE users SET last_login_at = CURRENT_TIMESTAMP WHERE user_id = ?";

    @Override
    public boolean existsByEmail(String email) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(EXISTS_BY_EMAIL_SQL)) {

            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    @Override
    public boolean existsByPhone(String phone) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(EXISTS_BY_PHONE_SQL)) {

            statement.setString(1, phone);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    @Override
    public boolean existsByPhoneForOtherUser(String phone, Long userId) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(EXISTS_BY_PHONE_FOR_OTHER_USER_SQL)) {

            statement.setString(1, phone);
            statement.setLong(2, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    @Override
    public Long createCustomer(User user) throws SQLException {
        Connection connection = null;

        try {
            connection = DBConnection.getConnection();
            connection.setAutoCommit(false);

            Long userId = insertUser(connection, user);
            insertCart(connection, userId);
            insertWishlist(connection, userId);

            connection.commit();
            return userId;

        } catch (SQLException exception) {
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
    public Optional<User> findByEmailForLogin(String email) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_EMAIL_FOR_LOGIN_SQL)) {

            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapUser(resultSet));
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<User> findById(Long userId) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_SQL)) {

            statement.setLong(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapUser(resultSet));
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public void updateProfile(User user) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_PROFILE_SQL)) {

            statement.setString(1, user.getFullName());
            statement.setString(2, user.getPhone());
            statement.setString(3, user.getGender());

            if (user.getDateOfBirth() == null) {
                statement.setDate(4, null);
            } else {
                statement.setDate(4, Date.valueOf(user.getDateOfBirth()));
            }

            statement.setLong(5, user.getUserId());

            int updatedRows = statement.executeUpdate();

            if (updatedRows == 0) {
                throw new IllegalArgumentException("Profile not found.");
            }
        }
    }

    @Override
    public void updateLastLogin(Long userId) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_LAST_LOGIN_SQL)) {

            statement.setLong(1, userId);
            statement.executeUpdate();
        }
    }

    private Long insertUser(Connection connection, User user) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                INSERT_USER_SQL,
                Statement.RETURN_GENERATED_KEYS
        )) {
            statement.setLong(1, user.getRoleId());
            statement.setString(2, user.getFullName());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPhone());
            statement.setString(5, user.getPasswordHash());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                }
            }
        }

        throw new SQLException("User creation failed. No generated key returned.");
    }

    private void insertCart(Connection connection, Long userId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_CART_SQL)) {
            statement.setLong(1, userId);
            statement.executeUpdate();
        }
    }

    private void insertWishlist(Connection connection, Long userId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_WISHLIST_SQL)) {
            statement.setLong(1, userId);
            statement.executeUpdate();
        }
    }

    private User mapUser(ResultSet resultSet) throws SQLException {
        User user = new User();

        user.setUserId(resultSet.getLong("user_id"));
        user.setRoleId(resultSet.getLong("role_id"));
        user.setRoleName(resultSet.getString("role_name"));
        user.setFullName(resultSet.getString("full_name"));
        user.setEmail(resultSet.getString("email"));
        user.setPhone(resultSet.getString("phone"));
        user.setPasswordHash(resultSet.getString("password_hash"));
        user.setProfileImage(resultSet.getString("profile_image"));
        user.setGender(resultSet.getString("gender"));
        user.setActive(resultSet.getBoolean("is_active"));
        user.setEmailVerified(resultSet.getBoolean("is_email_verified"));
        user.setPhoneVerified(resultSet.getBoolean("is_phone_verified"));

        Date dateOfBirth = resultSet.getDate("date_of_birth");
        if (dateOfBirth != null) {
            user.setDateOfBirth(dateOfBirth.toLocalDate());
        }

        if (resultSet.getTimestamp("last_login_at") != null) {
            user.setLastLoginAt(resultSet.getTimestamp("last_login_at").toLocalDateTime());
        }

        if (resultSet.getTimestamp("created_at") != null) {
            user.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
        }

        return user;
    }
}