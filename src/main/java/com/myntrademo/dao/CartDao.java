package com.myntrademo.dao;

import com.myntrademo.dto.cart.AddToCartRequest;
import com.myntrademo.dto.cart.CartItemDto;
import com.myntrademo.dto.cart.RecommendedProductDto;

import java.sql.SQLException;
import java.util.List;

public interface CartDao {

    void addItem(Long userId, AddToCartRequest request) throws SQLException;

    List<CartItemDto> findCartItems(Long userId) throws SQLException;

    List<RecommendedProductDto> findRecommendedProducts(Long userId, int limit) throws SQLException;

    void updateItemQuantity(Long userId, Long cartItemId, int quantity) throws SQLException;

    void removeItem(Long userId, Long cartItemId) throws SQLException;
}