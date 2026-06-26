package com.myntrademo.dao.impl;

import com.myntrademo.dao.AccountFeatureDao;
import com.myntrademo.dto.account.AccountFeatureItemDto;
import com.myntrademo.util.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class JdbcAccountFeatureDao implements AccountFeatureDao {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy");

    private static final String FIND_GIFT_CARDS_SQL =
            """
            SELECT card_code, balance_amount, expiry_date, status
            FROM user_gift_cards
            WHERE user_id = ?
            ORDER BY created_at DESC
            """;

    private static final String FIND_CREDIT_SQL =
            """
            SELECT transaction_title, transaction_note, credit_amount, transaction_type, created_at
            FROM user_credit_transactions
            WHERE user_id = ?
            ORDER BY created_at DESC
            """;

    private static final String FIND_COUPONS_SQL =
            """
            SELECT coupon_code, coupon_title, coupon_description, discount_text, expiry_date, status
            FROM user_coupons
            WHERE user_id = ?
            ORDER BY created_at DESC
            """;

    private static final String FIND_SAVED_CARDS_SQL =
            """
            SELECT card_brand, bank_name, card_last_four, expiry_month, expiry_year, status
            FROM user_saved_cards
            WHERE user_id = ?
            ORDER BY created_at DESC
            """;

    private static final String FIND_SAVED_VPA_SQL =
            """
            SELECT vpa_address, provider_name, status
            FROM user_saved_vpa
            WHERE user_id = ?
            ORDER BY created_at DESC
            """;

    @Override
    public List<AccountFeatureItemDto> findGiftCards(Long userId) throws SQLException {
        List<AccountFeatureItemDto> items = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_GIFT_CARDS_SQL)) {

            statement.setLong(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String expiry = formatDate(resultSet.getDate("expiry_date"));

                    items.add(new AccountFeatureItemDto(
                            "Gift Card " + maskCode(resultSet.getString("card_code")),
                            expiry.isBlank() ? "No expiry date" : "Expires on " + expiry,
                            resultSet.getString("card_code"),
                            formatMoney(resultSet.getBigDecimal("balance_amount")),
                            resultSet.getString("status"),
                            "Gift Card"
                    ));
                }
            }
        }

        return items;
    }

    @Override
    public List<AccountFeatureItemDto> findCreditTransactions(Long userId) throws SQLException {
        List<AccountFeatureItemDto> items = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_CREDIT_SQL)) {

            statement.setLong(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    items.add(new AccountFeatureItemDto(
                            resultSet.getString("transaction_title"),
                            resultSet.getString("transaction_note"),
                            resultSet.getString("transaction_type"),
                            formatMoney(resultSet.getBigDecimal("credit_amount")),
                            "Completed",
                            "Credit"
                    ));
                }
            }
        }

        return items;
    }

    @Override
    public List<AccountFeatureItemDto> findCoupons(Long userId) throws SQLException {
        List<AccountFeatureItemDto> items = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_COUPONS_SQL)) {

            statement.setLong(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String expiry = formatDate(resultSet.getDate("expiry_date"));
                    String detail = expiry.isBlank() ? resultSet.getString("coupon_code") : "Expires on " + expiry;

                    items.add(new AccountFeatureItemDto(
                            resultSet.getString("coupon_title"),
                            resultSet.getString("coupon_description"),
                            detail,
                            resultSet.getString("discount_text"),
                            resultSet.getString("status"),
                            resultSet.getString("coupon_code")
                    ));
                }
            }
        }

        return items;
    }

    @Override
    public List<AccountFeatureItemDto> findSavedCards(Long userId) throws SQLException {
        List<AccountFeatureItemDto> items = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_SAVED_CARDS_SQL)) {

            statement.setLong(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String expiry = buildExpiry(
                            resultSet.getString("expiry_month"),
                            resultSet.getString("expiry_year")
                    );

                    items.add(new AccountFeatureItemDto(
                            resultSet.getString("card_brand") + " ending " + resultSet.getString("card_last_four"),
                            resultSet.getString("bank_name"),
                            expiry.isBlank() ? "Expiry not available" : "Expires " + expiry,
                            "",
                            resultSet.getString("status"),
                            "Card"
                    ));
                }
            }
        }

        return items;
    }

    @Override
    public List<AccountFeatureItemDto> findSavedVpa(Long userId) throws SQLException {
        List<AccountFeatureItemDto> items = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_SAVED_VPA_SQL)) {

            statement.setLong(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    items.add(new AccountFeatureItemDto(
                            resultSet.getString("vpa_address"),
                            resultSet.getString("provider_name"),
                            "UPI ID",
                            "",
                            resultSet.getString("status"),
                            "UPI"
                    ));
                }
            }
        }

        return items;
    }

    private String formatMoney(BigDecimal amount) {
        if (amount == null) {
            return "₹0";
        }

        return "₹" + amount.stripTrailingZeros().toPlainString();
    }

    private String formatDate(Date date) {
        if (date == null) {
            return "";
        }

        return date.toLocalDate().format(DATE_FORMATTER);
    }

    private String maskCode(String code) {
        if (code == null || code.length() <= 4) {
            return "****";
        }

        return "****" + code.substring(code.length() - 4);
    }

    private String buildExpiry(String month, String year) {
        if (month == null || year == null || month.isBlank() || year.isBlank()) {
            return "";
        }

        return month + "/" + year;
    }
}