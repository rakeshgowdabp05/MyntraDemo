package com.myntrademo.service.impl;

import com.myntrademo.dao.CouponDao;
import com.myntrademo.dao.impl.JdbcCouponDao;
import com.myntrademo.dto.cart.CouponDto;
import com.myntrademo.service.CouponService;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CouponServiceImpl implements CouponService {

    private final CouponDao couponDao;

    public CouponServiceImpl() {
        this.couponDao = new JdbcCouponDao();
    }

    public CouponServiceImpl(CouponDao couponDao) {
        this.couponDao = couponDao;
    }

    @Override
    public List<CouponDto> getAvailableCoupons(BigDecimal cartSubtotal) throws SQLException {
        return couponDao.findAvailableCoupons(cartSubtotal);
    }

    @Override
    public Optional<CouponDto> getCouponById(Long couponId) throws SQLException {
        return couponDao.findById(couponId);
    }
}