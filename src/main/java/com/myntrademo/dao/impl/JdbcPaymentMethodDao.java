package com.myntrademo.dao.impl;

import com.myntrademo.dao.PaymentMethodDao;
import com.myntrademo.dto.checkout.PaymentMethodDto;
import com.myntrademo.dto.checkout.PaymentOptionDto;
import com.myntrademo.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JdbcPaymentMethodDao implements PaymentMethodDao {

    @Override
    public List<PaymentMethodDto> findActivePaymentMethods() throws SQLException {
        String sql = """
                SELECT
                    method_id,
                    method_code,
                    method_label,
                    detail_title,
                    detail_description,
                    tab_offer_text,
                    fee_amount,
                    recommended,
                    sort_order
                FROM checkout_payment_methods
                WHERE active = TRUE
                ORDER BY sort_order ASC, method_id ASC
                """;

        Map<String, PaymentMethodDto> methodMap = new LinkedHashMap<>();

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                PaymentMethodDto method = new PaymentMethodDto();
                method.setMethodId(resultSet.getLong("method_id"));
                method.setCode(resultSet.getString("method_code"));
                method.setLabel(resultSet.getString("method_label"));
                method.setDetailTitle(resultSet.getString("detail_title"));
                method.setDetailDescription(resultSet.getString("detail_description"));
                method.setTabOfferText(resultSet.getString("tab_offer_text"));
                method.setFeeAmount(resultSet.getBigDecimal("fee_amount"));
                method.setRecommended(resultSet.getBoolean("recommended"));
                method.setSortOrder(resultSet.getInt("sort_order"));

                methodMap.put(method.getCode(), method);
            }
        }

        loadOptions(methodMap);

        return List.copyOf(methodMap.values());
    }

    @Override
    public boolean existsActiveMethod(String methodCode) throws SQLException {
        String sql = """
                SELECT COUNT(*)
                FROM checkout_payment_methods
                WHERE method_code = ?
                  AND active = TRUE
                """;

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, methodCode);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() && resultSet.getInt(1) > 0;
            }
        }
    }

    @Override
    public boolean existsActiveOption(String methodCode, String optionCode) throws SQLException {
        String sql = """
                SELECT COUNT(*)
                FROM checkout_payment_options
                WHERE method_code = ?
                  AND option_code = ?
                  AND active = TRUE
                  AND disabled = FALSE
                """;

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, methodCode);
            statement.setString(2, optionCode);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() && resultSet.getInt(1) > 0;
            }
        }
    }

    @Override
    public PaymentOptionDto findOption(String methodCode, String optionCode) throws SQLException {
        String sql = """
                SELECT
                    option_id,
                    method_code,
                    option_code,
                    option_label,
                    option_subtitle,
                    option_notice,
                    logo_text,
                    min_order_amount,
                    disabled,
                    sort_order
                FROM checkout_payment_options
                WHERE method_code = ?
                  AND option_code = ?
                  AND active = TRUE
                LIMIT 1
                """;

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, methodCode);
            statement.setString(2, optionCode);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapOption(resultSet);
                }
            }
        }

        return null;
    }

    private void loadOptions(Map<String, PaymentMethodDto> methodMap) throws SQLException {
        if (methodMap.isEmpty()) {
            return;
        }

        String sql = """
                SELECT
                    option_id,
                    method_code,
                    option_code,
                    option_label,
                    option_subtitle,
                    option_notice,
                    logo_text,
                    min_order_amount,
                    disabled,
                    sort_order
                FROM checkout_payment_options
                WHERE active = TRUE
                ORDER BY method_code ASC, sort_order ASC, option_id ASC
                """;

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                String methodCode = resultSet.getString("method_code");
                PaymentMethodDto method = methodMap.get(methodCode);

                if (method != null) {
                    method.getOptions().add(mapOption(resultSet));
                }
            }
        }
    }

    private PaymentOptionDto mapOption(ResultSet resultSet) throws SQLException {
        PaymentOptionDto option = new PaymentOptionDto();
        option.setOptionId(resultSet.getLong("option_id"));
        option.setMethodCode(resultSet.getString("method_code"));
        option.setOptionCode(resultSet.getString("option_code"));
        option.setOptionLabel(resultSet.getString("option_label"));
        option.setOptionSubtitle(resultSet.getString("option_subtitle"));
        option.setOptionNotice(resultSet.getString("option_notice"));
        option.setLogoText(resultSet.getString("logo_text"));
        option.setMinOrderAmount(resultSet.getBigDecimal("min_order_amount"));
        option.setDisabled(resultSet.getBoolean("disabled"));
        option.setSortOrder(resultSet.getInt("sort_order"));
        return option;
    }
}