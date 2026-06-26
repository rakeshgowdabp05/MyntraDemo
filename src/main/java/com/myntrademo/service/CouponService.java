package com.myntrademo.service;

import com.myntrademo.dto.cart.CouponDto;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface CouponService {

    List<CouponDto> getAvailableCoupons(BigDecimal cartSubtotal) throws SQLException;

    Optional<CouponDto> getCouponById(Long couponId) throws SQLException;
}