package com.myntrademo.service;

import com.myntrademo.dto.cart.AddToCartRequest;
import com.myntrademo.dto.cart.CartPageDto;

import java.sql.SQLException;

public interface CartService {

    void addToCart(Long userId, AddToCartRequest request) throws SQLException;

    CartPageDto getCartPage(Long userId) throws SQLException;

    CartPageDto getCartPage(Long userId, Long appliedCouponId) throws SQLException;

    void updateQuantity(Long userId, Long cartItemId, int quantity) throws SQLException;

    void removeItem(Long userId, Long cartItemId) throws SQLException;
}