package com.myntrademo.dao;

import com.myntrademo.dto.cart.CouponDto;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface CouponDao {

    List<CouponDto> findAvailableCoupons(BigDecimal cartSubtotal) throws SQLException;

    Optional<CouponDto> findById(Long couponId) throws SQLException;
}