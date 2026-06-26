package com.myntrademo.dao.impl;

import com.myntrademo.dao.CouponDao;
import com.myntrademo.dto.cart.CouponDto;
import com.myntrademo.util.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcCouponDao implements CouponDao {

    private static final String FIND_AVAILABLE_COUPONS_SQL =
            """
            SELECT
                coupon_id,
                coupon_code,
                coupon_title,
                coupon_description,
                discount_type,
                discount_value,
                max_discount_amount,
                minimum_order_amount,
                start_date,
                end_date,
                is_active
            FROM coupons
            WHERE is_active = 1
              AND minimum_order_amount <= ?
              AND (start_date IS NULL OR start_date <= CURRENT_DATE)
              AND (end_date IS NULL OR end_date >= CURRENT_DATE)
            ORDER BY sort_order ASC, coupon_id ASC
            """;

    private static final String FIND_COUPON_BY_ID_SQL =
            """
            SELECT
                coupon_id,
                coupon_code,
                coupon_title,
                coupon_description,
                discount_type,
                discount_value,
                max_discount_amount,
                minimum_order_amount,
                start_date,
                end_date,
                is_active
            FROM coupons
            WHERE coupon_id = ?
            LIMIT 1
            """;

    @Override
    public List<CouponDto> findAvailableCoupons(BigDecimal cartSubtotal) throws SQLException {
        List<CouponDto> coupons = new ArrayList<>();
        BigDecimal subtotal = cartSubtotal == null ? BigDecimal.ZERO : cartSubtotal;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_AVAILABLE_COUPONS_SQL)) {

            statement.setBigDecimal(1, subtotal);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    coupons.add(mapCoupon(resultSet));
                }
            }
        }

        return coupons;
    }

    @Override
    public Optional<CouponDto> findById(Long couponId) throws SQLException {
        if (couponId == null) {
            return Optional.empty();
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_COUPON_BY_ID_SQL)) {

            statement.setLong(1, couponId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapCoupon(resultSet));
                }
            }
        }

        return Optional.empty();
    }

    private CouponDto mapCoupon(ResultSet resultSet) throws SQLException {
        CouponDto coupon = new CouponDto();

        coupon.setCouponId(resultSet.getLong("coupon_id"));
        coupon.setCouponCode(resultSet.getString("coupon_code"));
        coupon.setCouponTitle(resultSet.getString("coupon_title"));
        coupon.setCouponDescription(resultSet.getString("coupon_description"));
        coupon.setDiscountType(resultSet.getString("discount_type"));
        coupon.setDiscountValue(resultSet.getBigDecimal("discount_value"));
        coupon.setMaxDiscountAmount(resultSet.getBigDecimal("max_discount_amount"));
        coupon.setMinimumOrderAmount(resultSet.getBigDecimal("minimum_order_amount"));
        coupon.setActive(resultSet.getBoolean("is_active"));

        Date startDate = resultSet.getDate("start_date");
        Date endDate = resultSet.getDate("end_date");

        if (startDate != null) {
            coupon.setStartDate(startDate.toLocalDate());
        }

        if (endDate != null) {
            coupon.setEndDate(endDate.toLocalDate());
        }

        return coupon;
    }
}