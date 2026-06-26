package com.myntrademo.dao.impl;

import com.myntrademo.constant.CheckoutOrderStatusConstants;
import com.myntrademo.dao.CheckoutOrderDao;
import com.myntrademo.dto.cart.CartAddressDto;
import com.myntrademo.dto.cart.CartItemDto;
import com.myntrademo.dto.cart.CartPageDto;
import com.myntrademo.dto.checkout.PlaceOrderRequest;
import com.myntrademo.dto.checkout.PlacedOrderDto;
import com.myntrademo.dto.checkout.PlacedOrderItemDto;
import com.myntrademo.util.DBConnection;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class JdbcCheckoutOrderDao implements CheckoutOrderDao {

    private static final String ORDER_NUMBER_PREFIX_KEY = "ORDER_NUMBER_PREFIX";
    private static final String ESTIMATED_DELIVERY_DAYS_KEY = "ESTIMATED_DELIVERY_DAYS";
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    @Override
    public PlacedOrderDto createOrder(PlaceOrderRequest request) throws SQLException {
        Connection connection = null;

        try {
            connection = DBConnection.getConnection();
            connection.setAutoCommit(false);

            String orderNumber = generateOrderNumber(connection);
            LocalDate estimatedDeliveryDate = resolveEstimatedDeliveryDate(connection);
            BigDecimal payableAmount = calculatePayableAmount(request);

            Long orderId = insertOrder(connection, request, orderNumber, estimatedDeliveryDate, payableAmount);
            insertOrderItems(connection, orderId, request.getCartPage());
            insertPayment(connection, orderId, request, payableAmount);
            clearCart(connection, request.getUserId());

            connection.commit();

            return findOrderForUser(request.getUserId(), orderId);

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
    public PlacedOrderDto findOrderForUser(Long userId, Long orderId) throws SQLException {
        String sql = """
                SELECT *
                FROM checkout_orders
                WHERE user_id = ?
                  AND order_id = ?
                LIMIT 1
                """;

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setLong(1, userId);
            statement.setLong(2, orderId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    PlacedOrderDto order = mapOrder(resultSet);
                    order.setItems(findItems(connection, order.getOrderId()));
                    return order;
                }
            }
        }

        return null;
    }

    @Override
    public PlacedOrderDto findOrderForUserByOrderNumber(Long userId, String orderNumber) throws SQLException {
        String sql = """
                SELECT *
                FROM checkout_orders
                WHERE user_id = ?
                  AND order_number = ?
                LIMIT 1
                """;

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setLong(1, userId);
            statement.setString(2, orderNumber);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    PlacedOrderDto order = mapOrder(resultSet);
                    order.setItems(findItems(connection, order.getOrderId()));
                    return order;
                }
            }
        }

        return null;
    }

    private Long insertOrder(
            Connection connection,
            PlaceOrderRequest request,
            String orderNumber,
            LocalDate estimatedDeliveryDate,
            BigDecimal payableAmount
    ) throws SQLException {
        String sql = """
                INSERT INTO checkout_orders
                (
                    order_number,
                    user_id,
                    address_id,
                    customer_name,
                    customer_phone,
                    delivery_pincode,
                    delivery_address_text,
                    estimated_delivery_date,
                    total_items,
                    total_mrp,
                    total_discount,
                    subtotal,
                    coupon_code,
                    coupon_discount,
                    gift_package_enabled,
                    gift_package_fee,
                    donation_amount,
                    gift_card_code,
                    gift_card_discount,
                    payment_method_code,
                    payment_option_code,
                    payment_fee,
                    payable_amount,
                    order_status,
                    payment_status
                )
                VALUES
                (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        CartPageDto cartPage = request.getCartPage();
        CartAddressDto address = request.getDeliveryAddress();

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            int index = 1;

            statement.setString(index++, orderNumber);
            statement.setLong(index++, request.getUserId());
            statement.setLong(index++, address.getAddressId());
            statement.setString(index++, safe(address.getFullName()));
            statement.setString(index++, safe(address.getPhone()));
            statement.setString(index++, safe(address.getPincode()));
            statement.setString(index++, safe(address.getDisplayAddress()));
            statement.setDate(index++, Date.valueOf(estimatedDeliveryDate));
            statement.setInt(index++, cartPage.getTotalItems());
            statement.setBigDecimal(index++, cartPage.getTotalMrp());
            statement.setBigDecimal(index++, cartPage.getTotalDiscount());
            statement.setBigDecimal(index++, cartPage.getSubtotal());

            if (cartPage.isAppliedCouponPresent()) {
                statement.setString(index++, cartPage.getAppliedCoupon().getCouponCode());
            } else {
                statement.setString(index++, null);
            }

            statement.setBigDecimal(index++, cartPage.getCouponDiscount());
            statement.setBoolean(index++, cartPage.isGiftPackageEnabled());
            statement.setBigDecimal(index++, cartPage.getGiftPackageFee());
            statement.setBigDecimal(index++, cartPage.getDonationAmount());
            statement.setString(index++, emptyToNull(request.getGiftCardCode()));
            statement.setBigDecimal(index++, request.getGiftCardDiscount());
            statement.setString(index++, request.getPaymentMethodCode());
            statement.setString(index++, emptyToNull(request.getPaymentOptionCode()));
            statement.setBigDecimal(index++, request.getPaymentFee());
            statement.setBigDecimal(index++, payableAmount);
            statement.setString(index++, CheckoutOrderStatusConstants.ORDER_PLACED);
            statement.setString(index, resolvePaymentStatus(request.getPaymentMethodCode()));

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                }
            }
        }

        throw new SQLException("Order ID was not generated.");
    }

    private void insertOrderItems(Connection connection, Long orderId, CartPageDto cartPage) throws SQLException {
        String sql = """
                INSERT INTO checkout_order_items
                (
                    order_id,
                    product_id,
                    variant_id,
                    brand_name,
                    product_name,
                    size_label,
                    color_label,
                    image_url,
                    quantity,
                    mrp_price,
                    selling_price,
                    item_total
                )
                VALUES
                (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (CartItemDto item : cartPage.getItems()) {
                int index = 1;

                statement.setLong(index++, orderId);
                statement.setLong(index++, item.getProductId());

                if (item.getVariantId() == null) {
                    statement.setObject(index++, null);
                } else {
                    statement.setLong(index++, item.getVariantId());
                }

                statement.setString(index++, safe(item.getBrandName()));
                statement.setString(index++, safe(item.getProductName()));
                statement.setString(index++, safe(item.getDisplaySize()));
                statement.setString(index++, safe(item.getDisplayColor()));
                statement.setString(index++, safe(item.getImageUrl()));
                statement.setInt(index++, item.getQuantity());
                statement.setBigDecimal(index++, item.getMrpPrice());
                statement.setBigDecimal(index++, item.getPriceAtAdded());
                statement.setBigDecimal(index, item.getItemTotal());

                statement.addBatch();
            }

            statement.executeBatch();
        }
    }

    private void insertPayment(
            Connection connection,
            Long orderId,
            PlaceOrderRequest request,
            BigDecimal payableAmount
    ) throws SQLException {
        String sql = """
                INSERT INTO checkout_order_payments
                (
                    order_id,
                    user_id,
                    payment_method_code,
                    payment_option_code,
                    payment_amount,
                    payment_status
                )
                VALUES
                (?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, orderId);
            statement.setLong(2, request.getUserId());
            statement.setString(3, request.getPaymentMethodCode());
            statement.setString(4, emptyToNull(request.getPaymentOptionCode()));
            statement.setBigDecimal(5, payableAmount);
            statement.setString(6, resolvePaymentStatus(request.getPaymentMethodCode()));
            statement.executeUpdate();
        }
    }

    private void clearCart(Connection connection, Long userId) throws SQLException {
        String sql = """
                DELETE ci
                FROM cart_items ci
                INNER JOIN carts c ON c.cart_id = ci.cart_id
                WHERE c.user_id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            statement.executeUpdate();
        }
    }

    private java.util.List<PlacedOrderItemDto> findItems(Connection connection, Long orderId) throws SQLException {
        String sql = """
                SELECT *
                FROM checkout_order_items
                WHERE order_id = ?
                ORDER BY order_item_id ASC
                """;

        java.util.List<PlacedOrderItemDto> items = new java.util.ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, orderId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    PlacedOrderItemDto item = new PlacedOrderItemDto();
                    item.setOrderItemId(resultSet.getLong("order_item_id"));
                    item.setProductId(resultSet.getLong("product_id"));

                    long variantId = resultSet.getLong("variant_id");
                    if (!resultSet.wasNull()) {
                        item.setVariantId(variantId);
                    }

                    item.setBrandName(resultSet.getString("brand_name"));
                    item.setProductName(resultSet.getString("product_name"));
                    item.setSizeLabel(resultSet.getString("size_label"));
                    item.setColorLabel(resultSet.getString("color_label"));
                    item.setImageUrl(resultSet.getString("image_url"));
                    item.setQuantity(resultSet.getInt("quantity"));
                    item.setMrpPrice(resultSet.getBigDecimal("mrp_price"));
                    item.setSellingPrice(resultSet.getBigDecimal("selling_price"));
                    item.setItemTotal(resultSet.getBigDecimal("item_total"));
                    items.add(item);
                }
            }
        }

        return items;
    }

    private PlacedOrderDto mapOrder(ResultSet resultSet) throws SQLException {
        PlacedOrderDto order = new PlacedOrderDto();

        order.setOrderId(resultSet.getLong("order_id"));
        order.setOrderNumber(resultSet.getString("order_number"));
        order.setUserId(resultSet.getLong("user_id"));
        order.setCustomerName(resultSet.getString("customer_name"));
        order.setCustomerPhone(resultSet.getString("customer_phone"));
        order.setDeliveryPincode(resultSet.getString("delivery_pincode"));
        order.setDeliveryAddressText(resultSet.getString("delivery_address_text"));
        order.setTotalItems(resultSet.getInt("total_items"));
        order.setTotalMrp(resultSet.getBigDecimal("total_mrp"));
        order.setTotalDiscount(resultSet.getBigDecimal("total_discount"));
        order.setCouponDiscount(resultSet.getBigDecimal("coupon_discount"));
        order.setGiftPackageFee(resultSet.getBigDecimal("gift_package_fee"));
        order.setDonationAmount(resultSet.getBigDecimal("donation_amount"));
        order.setGiftCardDiscount(resultSet.getBigDecimal("gift_card_discount"));
        order.setPaymentFee(resultSet.getBigDecimal("payment_fee"));
        order.setPayableAmount(resultSet.getBigDecimal("payable_amount"));
        order.setOrderStatus(resultSet.getString("order_status"));
        order.setPaymentStatus(resultSet.getString("payment_status"));
        order.setPaymentMethodCode(resultSet.getString("payment_method_code"));
        order.setPaymentOptionCode(resultSet.getString("payment_option_code"));

        Date estimatedDate = resultSet.getDate("estimated_delivery_date");
        if (estimatedDate != null) {
            order.setEstimatedDeliveryDate(estimatedDate.toLocalDate());
        }

        if (resultSet.getTimestamp("created_at") != null) {
            order.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
        }

        return order;
    }

    private BigDecimal calculatePayableAmount(PlaceOrderRequest request) {
        BigDecimal payable = request.getCartPage()
                .getPayableAmount()
                .add(request.getPaymentFee())
                .subtract(request.getGiftCardDiscount());

        if (payable.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO;
        }

        return payable;
    }

    private String generateOrderNumber(Connection connection) throws SQLException {
        String prefix = getSettingValue(connection, ORDER_NUMBER_PREFIX_KEY);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        int randomNumber = SECURE_RANDOM.nextInt(900000) + 100000;

        return prefix + timestamp + randomNumber;
    }

    private LocalDate resolveEstimatedDeliveryDate(Connection connection) throws SQLException {
        String daysText = getSettingValue(connection, ESTIMATED_DELIVERY_DAYS_KEY);
        int days = Integer.parseInt(daysText);
        return LocalDate.now().plusDays(days);
    }

    private String getSettingValue(Connection connection, String settingKey) throws SQLException {
        String sql = """
                SELECT setting_value
                FROM checkout_order_settings
                WHERE setting_key = ?
                  AND active = TRUE
                LIMIT 1
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, settingKey);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("setting_value");
                }
            }
        }

        throw new SQLException("Missing checkout order setting: " + settingKey);
    }

    private String resolvePaymentStatus(String paymentMethodCode) {
        if ("CASH_ON_DELIVERY".equals(paymentMethodCode)) {
            return CheckoutOrderStatusConstants.PAYMENT_PENDING;
        }

        return CheckoutOrderStatusConstants.PAYMENT_PAID;
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    private String emptyToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}