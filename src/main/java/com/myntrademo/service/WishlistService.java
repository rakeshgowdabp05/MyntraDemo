package com.myntrademo.service;

import com.myntrademo.dto.wishlist.WishlistPageDto;

import java.sql.SQLException;

public interface WishlistService {

    void addToWishlist(Long userId, Long productId, Long variantId) throws SQLException;

    WishlistPageDto getWishlistPage(Long userId) throws SQLException;

    void removeItem(Long userId, Long wishlistItemId) throws SQLException;

    void moveWishlistItemToCart(Long userId, Long wishlistItemId) throws SQLException;

    void moveCartItemToWishlist(Long userId, Long cartItemId) throws SQLException;
}