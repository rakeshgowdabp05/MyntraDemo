package com.myntrademo.service.impl;

import com.myntrademo.dto.checkout.CustomerOrderCardDto;
import com.myntrademo.service.CustomerOrdersService;
import com.myntrademo.util.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CustomerOrdersServiceImpl implements CustomerOrdersService {

    private static final String STATUS_ALL = "ALL";
    private static final List<String> ALLOWED_STATUS_FILTERS = List.of(
            STATUS_ALL,
            "PLACED",
            "CANCELLED",
            "DELIVERED"
    );

    @Override
    public List<CustomerOrderCardDto> getOrdersForUser(Long userId, String statusFilter, String searchText)
            throws SQLException {

        String cleanStatus = cleanStatus(statusFilter);
        String cleanSearch = cleanSearch(searchText);

        StringBuilder sql = new StringBuilder("""
                SELECT
                    o.order_id,
                    o.order_number,
                    o.order_status,
                    o.payment_status,
                    o.payment_method_code,
                    o.total_items,
                    o.payable_amount,
                    o.estimated_delivery_date,
                    o.created_at,
                    oi.product_id,
                    oi.brand_name,
                    oi.product_name,
                    oi.size_label,
                    oi.image_url,
                    oi.quantity
                FROM checkout_orders o
                LEFT JOIN checkout_order_items oi
                    ON oi.order_item_id = (
                        SELECT MIN(inner_oi.order_item_id)
                        FROM checkout_order_items inner_oi
                        WHERE inner_oi.order_id = o.order_id
                    )
                WHERE o.user_id = ?
                """);

        List<Object> params = new ArrayList<>();
        params.add(userId);

        if (!STATUS_ALL.equals(cleanStatus)) {
            sql.append(" AND o.order_status = ? ");
            params.add(cleanStatus);
        }

        if (!cleanSearch.isBlank()) {
            sql.append("""
                    AND (
                        LOWER(o.order_number) LIKE ?
                        OR LOWER(oi.brand_name) LIKE ?
                        OR LOWER(oi.product_name) LIKE ?
                    )
                    """);

            String searchPattern = "%" + cleanSearch.toLowerCase() + "%";
            params.add(searchPattern);
            params.add(searchPattern);
            params.add(searchPattern);
        }

        sql.append(" ORDER BY o.created_at DESC, o.order_id DESC ");

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql.toString())
        ) {
            bindParams(statement, params);

            try (ResultSet resultSet = statement.executeQuery()) {
                List<CustomerOrderCardDto> orders = new ArrayList<>();

                while (resultSet.next()) {
                    orders.add(mapOrder(resultSet));
                }

                return orders;
            }
        }
    }

    private CustomerOrderCardDto mapOrder(ResultSet resultSet) throws SQLException {
        CustomerOrderCardDto order = new CustomerOrderCardDto();

        order.setOrderId(resultSet.getLong("order_id"));
        order.setOrderNumber(resultSet.getString("order_number"));
        order.setOrderStatus(resultSet.getString("order_status"));
        order.setPaymentStatus(resultSet.getString("payment_status"));
        order.setPaymentMethodCode(resultSet.getString("payment_method_code"));
        order.setTotalItems(resultSet.getInt("total_items"));
        order.setPayableAmount(resultSet.getBigDecimal("payable_amount"));

        Date estimatedDate = resultSet.getDate("estimated_delivery_date");
        if (estimatedDate != null) {
            order.setEstimatedDeliveryDate(estimatedDate.toLocalDate());
        }

        Timestamp createdAt = resultSet.getTimestamp("created_at");
        if (createdAt != null) {
            order.setCreatedAt(createdAt.toLocalDateTime());
        }

        long productId = resultSet.getLong("product_id");
        if (!resultSet.wasNull()) {
            order.setProductId(productId);
        }

        order.setBrandName(resultSet.getString("brand_name"));
        order.setProductName(resultSet.getString("product_name"));
        order.setSizeLabel(resultSet.getString("size_label"));
        order.setImageUrl(resultSet.getString("image_url"));
        order.setQuantity(resultSet.getInt("quantity"));

        return order;
    }

    private void bindParams(PreparedStatement statement, List<Object> params) throws SQLException {
        for (int index = 0; index < params.size(); index++) {
            Object value = params.get(index);

            if (value instanceof Long longValue) {
                statement.setLong(index + 1, longValue);
            } else {
                statement.setString(index + 1, String.valueOf(value));
            }
        }
    }

    private String cleanStatus(String statusFilter) {
        if (statusFilter == null || statusFilter.isBlank()) {
            return STATUS_ALL;
        }

        String cleaned = statusFilter.trim().toUpperCase();

        if (!ALLOWED_STATUS_FILTERS.contains(cleaned)) {
            return STATUS_ALL;
        }

        return cleaned;
    }

    private String cleanSearch(String searchText) {
        if (searchText == null) {
            return "";
        }

        return searchText.trim();
    }
}