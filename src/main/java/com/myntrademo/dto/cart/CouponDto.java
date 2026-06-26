package com.myntrademo.dto.cart;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class CouponDto {

    private Long couponId;
    private String couponCode;
    private String couponTitle;
    private String couponDescription;
    private String discountType;
    private BigDecimal discountValue = BigDecimal.ZERO;
    private BigDecimal maxDiscountAmount;
    private BigDecimal minimumOrderAmount = BigDecimal.ZERO;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean active;

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getCouponTitle() {
        return couponTitle;
    }

    public void setCouponTitle(String couponTitle) {
        this.couponTitle = couponTitle;
    }

    public String getCouponDescription() {
        return couponDescription;
    }

    public void setCouponDescription(String couponDescription) {
        this.couponDescription = couponDescription;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        if (discountValue != null) {
            this.discountValue = discountValue;
        }
    }

    public BigDecimal getMaxDiscountAmount() {
        return maxDiscountAmount;
    }

    public void setMaxDiscountAmount(BigDecimal maxDiscountAmount) {
        this.maxDiscountAmount = maxDiscountAmount;
    }

    public BigDecimal getMinimumOrderAmount() {
        return minimumOrderAmount;
    }

    public void setMinimumOrderAmount(BigDecimal minimumOrderAmount) {
        if (minimumOrderAmount != null) {
            this.minimumOrderAmount = minimumOrderAmount;
        }
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isApplicable(BigDecimal cartSubtotal) {
        if (!active || cartSubtotal == null) {
            return false;
        }

        LocalDate today = LocalDate.now();

        if (startDate != null && today.isBefore(startDate)) {
            return false;
        }

        if (endDate != null && today.isAfter(endDate)) {
            return false;
        }

        return cartSubtotal.compareTo(minimumOrderAmount) >= 0;
    }

    public BigDecimal calculateDiscount(BigDecimal cartSubtotal) {
        if (!isApplicable(cartSubtotal)) {
            return BigDecimal.ZERO;
        }

        BigDecimal discount;

        if ("PERCENTAGE".equalsIgnoreCase(discountType)) {
            discount = cartSubtotal
                    .multiply(discountValue)
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        } else {
            discount = discountValue;
        }

        if (maxDiscountAmount != null && discount.compareTo(maxDiscountAmount) > 0) {
            discount = maxDiscountAmount;
        }

        if (discount.compareTo(cartSubtotal) > 0) {
            return cartSubtotal;
        }

        return discount;
    }
}