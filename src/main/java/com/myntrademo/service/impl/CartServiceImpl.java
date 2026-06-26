package com.myntrademo.service.impl;

import com.myntrademo.constant.MessageConstants;
import com.myntrademo.dao.CartDao;
import com.myntrademo.dao.impl.JdbcCartDao;
import com.myntrademo.dto.cart.AddToCartRequest;
import com.myntrademo.dto.cart.CartAddressDto;
import com.myntrademo.dto.cart.CartItemDto;
import com.myntrademo.dto.cart.CartPageDto;
import com.myntrademo.dto.cart.CouponDto;
import com.myntrademo.dto.cart.RecommendedProductDto;
import com.myntrademo.service.AddressService;
import com.myntrademo.service.CartService;
import com.myntrademo.service.CouponService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CartServiceImpl implements CartService {

    private static final int RECOMMENDED_PRODUCT_LIMIT = 15;

    private final CartDao cartDao;
    private final AddressService addressService;
    private final CouponService couponService;

    public CartServiceImpl() {
        this.cartDao = new JdbcCartDao();
        this.addressService = new AddressServiceImpl();
        this.couponService = new CouponServiceImpl();
    }

    public CartServiceImpl(CartDao cartDao) {
        this.cartDao = cartDao;
        this.addressService = new AddressServiceImpl();
        this.couponService = new CouponServiceImpl();
    }

    @Override
    public void addToCart(Long userId, AddToCartRequest request) throws SQLException {
        validateUser(userId);
        validateAddToCartRequest(request);

        cartDao.addItem(userId, request);
    }

    @Override
    public CartPageDto getCartPage(Long userId) throws SQLException {
        return getCartPage(userId, null);
    }

    @Override
    public CartPageDto getCartPage(Long userId, Long appliedCouponId) throws SQLException {
        validateUser(userId);

        List<CartItemDto> cartItems = cartDao.findCartItems(userId);
        List<RecommendedProductDto> recommendedProducts = cartDao.findRecommendedProducts(
                userId,
                RECOMMENDED_PRODUCT_LIMIT
        );

        CartPageDto cartPage = new CartPageDto(cartItems, recommendedProducts);

        loadDefaultAddressSafely(userId, cartPage);
        loadCouponsSafely(cartPage);
        applyCouponSafely(appliedCouponId, cartPage);

        return cartPage;
    }

    @Override
    public void updateQuantity(Long userId, Long cartItemId, int quantity) throws SQLException {
        validateUser(userId);

        if (cartItemId == null) {
            throw new IllegalArgumentException(MessageConstants.CART_ITEM_NOT_FOUND);
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException(MessageConstants.INVALID_CART_QUANTITY);
        }

        cartDao.updateItemQuantity(userId, cartItemId, quantity);
    }

    @Override
    public void removeItem(Long userId, Long cartItemId) throws SQLException {
        validateUser(userId);

        if (cartItemId == null) {
            throw new IllegalArgumentException(MessageConstants.CART_ITEM_NOT_FOUND);
        }

        cartDao.removeItem(userId, cartItemId);
    }

    private void loadDefaultAddressSafely(Long userId, CartPageDto cartPage) {
        try {
            Optional<CartAddressDto> defaultAddress = addressService.getDefaultAddress(userId);
            defaultAddress.ifPresent(cartPage::setDefaultAddress);
        } catch (SQLException | RuntimeException exception) {
            exception.printStackTrace();
        }
    }

    private void loadCouponsSafely(CartPageDto cartPage) {
        try {
            List<CouponDto> availableCoupons = couponService.getAvailableCoupons(cartPage.getSubtotal());
            cartPage.setAvailableCoupons(availableCoupons);
        } catch (SQLException | RuntimeException exception) {
            exception.printStackTrace();
        }
    }

    private void applyCouponSafely(Long appliedCouponId, CartPageDto cartPage) {
        if (appliedCouponId == null) {
            return;
        }

        try {
            couponService.getCouponById(appliedCouponId)
                    .filter(coupon -> coupon.isApplicable(cartPage.getSubtotal()))
                    .ifPresent(cartPage::setAppliedCoupon);
        } catch (SQLException | RuntimeException exception) {
            exception.printStackTrace();
        }
    }

    private void validateUser(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException(MessageConstants.AUTH_REQUIRED);
        }
    }

    private void validateAddToCartRequest(AddToCartRequest request) {
        if (request == null || request.getProductId() == null || request.getVariantId() == null) {
            throw new IllegalArgumentException(MessageConstants.SELECT_PRODUCT_VARIANT);
        }

        if (request.getQuantity() <= 0) {
            throw new IllegalArgumentException(MessageConstants.INVALID_CART_QUANTITY);
        }
    }
}