package com.myntrademo.dao;

import com.myntrademo.dto.wishlist.WishlistItemDto;

import java.sql.SQLException;
import java.util.List;

public interface WishlistDao {

    void addItem(Long userId, Long productId, Long variantId) throws SQLException;

    List<WishlistItemDto> findWishlistItems(Long userId) throws SQLException;

    void removeItem(Long userId, Long wishlistItemId) throws SQLException;

    void moveWishlistItemToCart(Long userId, Long wishlistItemId) throws SQLException;

    void moveCartItemToWishlist(Long userId, Long cartItemId) throws SQLException;
}