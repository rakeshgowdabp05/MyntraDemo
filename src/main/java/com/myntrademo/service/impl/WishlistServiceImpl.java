package com.myntrademo.service.impl;

import com.myntrademo.constant.MessageConstants;
import com.myntrademo.dao.WishlistDao;
import com.myntrademo.dao.impl.JdbcWishlistDao;
import com.myntrademo.dto.wishlist.WishlistPageDto;
import com.myntrademo.service.WishlistService;

import java.sql.SQLException;

public class WishlistServiceImpl implements WishlistService {

    private final WishlistDao wishlistDao;

    public WishlistServiceImpl() {
        this.wishlistDao = new JdbcWishlistDao();
    }

    public WishlistServiceImpl(WishlistDao wishlistDao) {
        this.wishlistDao = wishlistDao;
    }

    @Override
    public void addToWishlist(Long userId, Long productId, Long variantId) throws SQLException {
        validateUser(userId);

        if (productId == null) {
            throw new IllegalArgumentException(MessageConstants.PRODUCT_NOT_AVAILABLE);
        }

        wishlistDao.addItem(userId, productId, variantId);
    }

    @Override
    public WishlistPageDto getWishlistPage(Long userId) throws SQLException {
        validateUser(userId);
        return new WishlistPageDto(wishlistDao.findWishlistItems(userId));
    }

    @Override
    public void removeItem(Long userId, Long wishlistItemId) throws SQLException {
        validateUser(userId);

        if (wishlistItemId == null) {
            throw new IllegalArgumentException(MessageConstants.WISHLIST_ITEM_NOT_FOUND);
        }

        wishlistDao.removeItem(userId, wishlistItemId);
    }

    @Override
    public void moveWishlistItemToCart(Long userId, Long wishlistItemId) throws SQLException {
        validateUser(userId);

        if (wishlistItemId == null) {
            throw new IllegalArgumentException(MessageConstants.WISHLIST_ITEM_NOT_FOUND);
        }

        wishlistDao.moveWishlistItemToCart(userId, wishlistItemId);
    }

    @Override
    public void moveCartItemToWishlist(Long userId, Long cartItemId) throws SQLException {
        validateUser(userId);

        if (cartItemId == null) {
            throw new IllegalArgumentException(MessageConstants.CART_ITEM_NOT_FOUND);
        }

        wishlistDao.moveCartItemToWishlist(userId, cartItemId);
    }

    private void validateUser(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException(MessageConstants.AUTH_REQUIRED);
        }
    }
}