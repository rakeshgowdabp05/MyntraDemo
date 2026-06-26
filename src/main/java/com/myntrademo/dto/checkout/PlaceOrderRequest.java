package com.myntrademo.dto.checkout;

import com.myntrademo.dto.cart.CartAddressDto;
import com.myntrademo.dto.cart.CartPageDto;

import java.math.BigDecimal;

public class PlaceOrderRequest {

    private Long userId;
    private CartPageDto cartPage;
    private CartAddressDto deliveryAddress;

    private String paymentMethodCode;
    private String paymentOptionCode;
    private BigDecimal paymentFee = BigDecimal.ZERO;

    private String giftCardCode;
    private BigDecimal giftCardDiscount = BigDecimal.ZERO;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public CartPageDto getCartPage() {
        return cartPage;
    }

    public void setCartPage(CartPageDto cartPage) {
        this.cartPage = cartPage;
    }

    public CartAddressDto getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(CartAddressDto deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getPaymentMethodCode() {
        return paymentMethodCode;
    }

    public void setPaymentMethodCode(String paymentMethodCode) {
        this.paymentMethodCode = paymentMethodCode;
    }

    public String getPaymentOptionCode() {
        return paymentOptionCode;
    }

    public void setPaymentOptionCode(String paymentOptionCode) {
        this.paymentOptionCode = paymentOptionCode;
    }

    public BigDecimal getPaymentFee() {
        return paymentFee;
    }

    public void setPaymentFee(BigDecimal paymentFee) {
        this.paymentFee = paymentFee == null ? BigDecimal.ZERO : paymentFee;
    }

    public String getGiftCardCode() {
        return giftCardCode;
    }

    public void setGiftCardCode(String giftCardCode) {
        this.giftCardCode = giftCardCode;
    }

    public BigDecimal getGiftCardDiscount() {
        return giftCardDiscount;
    }

    public void setGiftCardDiscount(BigDecimal giftCardDiscount) {
        this.giftCardDiscount = giftCardDiscount == null ? BigDecimal.ZERO : giftCardDiscount;
    }
}