package com.myntrademo.dto.cart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class CartPageDto {

    private List<CartItemDto> items = new ArrayList<>();
    private List<RecommendedProductDto> recommendedProducts = new ArrayList<>();
    private List<CouponDto> availableCoupons = new ArrayList<>();

    private CartAddressDto defaultAddress;
    private CouponDto appliedCoupon;

    private boolean giftPackageEnabled;
    private BigDecimal giftPackageFee = BigDecimal.ZERO;
    private BigDecimal donationAmount = BigDecimal.ZERO;

    private int totalItems;
    private BigDecimal totalMrp = BigDecimal.ZERO;
    private BigDecimal totalDiscount = BigDecimal.ZERO;
    private BigDecimal subtotal = BigDecimal.ZERO;
    private BigDecimal couponDiscount = BigDecimal.ZERO;
    private BigDecimal payableAmount = BigDecimal.ZERO;

    public CartPageDto() {
    }

    public CartPageDto(List<CartItemDto> items, List<RecommendedProductDto> recommendedProducts) {
        setItems(items);
        setRecommendedProducts(recommendedProducts);
    }

    public List<CartItemDto> getItems() {
        return items;
    }

    public void setItems(List<CartItemDto> items) {
        if (items == null) {
            this.items = new ArrayList<>();
        } else {
            this.items = items;
        }

        calculateTotals();
    }

    public List<RecommendedProductDto> getRecommendedProducts() {
        return recommendedProducts;
    }

    public void setRecommendedProducts(List<RecommendedProductDto> recommendedProducts) {
        if (recommendedProducts == null) {
            this.recommendedProducts = new ArrayList<>();
        } else {
            this.recommendedProducts = recommendedProducts;
        }
    }

    public List<CouponDto> getAvailableCoupons() {
        return availableCoupons;
    }

    public void setAvailableCoupons(List<CouponDto> availableCoupons) {
        if (availableCoupons == null) {
            this.availableCoupons = new ArrayList<>();
        } else {
            this.availableCoupons = availableCoupons;
        }
    }

    public CartAddressDto getDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(CartAddressDto defaultAddress) {
        this.defaultAddress = defaultAddress;
    }

    public CouponDto getAppliedCoupon() {
        return appliedCoupon;
    }

    public void setAppliedCoupon(CouponDto appliedCoupon) {
        this.appliedCoupon = appliedCoupon;
        calculateCouponDiscount();
    }

    public boolean isGiftPackageEnabled() {
        return giftPackageEnabled;
    }

    public void setGiftPackageEnabled(boolean giftPackageEnabled) {
        this.giftPackageEnabled = giftPackageEnabled;

        if (!giftPackageEnabled) {
            this.giftPackageFee = BigDecimal.ZERO;
        }

        calculatePayableAmount();
    }

    public BigDecimal getGiftPackageFee() {
        return giftPackageFee;
    }

    public void setGiftPackageFee(BigDecimal giftPackageFee) {
        if (giftPackageFee == null || giftPackageFee.compareTo(BigDecimal.ZERO) < 0) {
            this.giftPackageFee = BigDecimal.ZERO;
        } else {
            this.giftPackageFee = giftPackageFee;
        }

        calculatePayableAmount();
    }

    public BigDecimal getDonationAmount() {
        return donationAmount;
    }

    public void setDonationAmount(BigDecimal donationAmount) {
        if (donationAmount == null || donationAmount.compareTo(BigDecimal.ZERO) < 0) {
            this.donationAmount = BigDecimal.ZERO;
        } else {
            this.donationAmount = donationAmount;
        }

        calculatePayableAmount();
    }

    public List<String> getRecommendationCategories() {
        Set<String> categories = new LinkedHashSet<>();

        for (RecommendedProductDto product : recommendedProducts) {
            if (product.getCategoryName() != null && !product.getCategoryName().isBlank()) {
                categories.add(product.getCategoryName());
            }
        }

        return new ArrayList<>(categories);
    }

    public int getTotalItems() {
        return totalItems;
    }

    public BigDecimal getTotalMrp() {
        return totalMrp;
    }

    public BigDecimal getTotalDiscount() {
        return totalDiscount;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public BigDecimal getCouponDiscount() {
        return couponDiscount;
    }

    public BigDecimal getPayableAmount() {
        return payableAmount;
    }

    public boolean isEmpty() {
        return totalItems <= 0;
    }

    public boolean isDiscountAvailable() {
        return totalDiscount.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isCouponDiscountAvailable() {
        return couponDiscount.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isGiftPackageFeeAvailable() {
        return giftPackageEnabled && giftPackageFee.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isDonationAvailable() {
        return donationAmount.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isRecommendedProductsAvailable() {
        return recommendedProducts != null && !recommendedProducts.isEmpty();
    }

    public boolean isAvailableCouponsPresent() {
        return availableCoupons != null && !availableCoupons.isEmpty();
    }

    public boolean isAppliedCouponPresent() {
        return appliedCoupon != null && isCouponDiscountAvailable();
    }

    public boolean isDefaultAddressAvailable() {
        return defaultAddress != null;
    }

    private void calculateTotals() {
        totalItems = 0;
        totalMrp = BigDecimal.ZERO;
        totalDiscount = BigDecimal.ZERO;
        subtotal = BigDecimal.ZERO;

        for (CartItemDto item : items) {
            totalItems += item.getQuantity();
            totalMrp = totalMrp.add(item.getItemMrpTotal());
            totalDiscount = totalDiscount.add(item.getItemDiscountTotal());
            subtotal = subtotal.add(item.getItemTotal());
        }

        calculateCouponDiscount();
    }

    private void calculateCouponDiscount() {
        if (appliedCoupon == null) {
            couponDiscount = BigDecimal.ZERO;
            calculatePayableAmount();
            return;
        }

        couponDiscount = appliedCoupon.calculateDiscount(subtotal);
        calculatePayableAmount();
    }

    private void calculatePayableAmount() {
        BigDecimal total = subtotal.subtract(couponDiscount);

        if (giftPackageEnabled) {
            total = total.add(giftPackageFee);
        }

        total = total.add(donationAmount);

        if (total.compareTo(BigDecimal.ZERO) < 0) {
            payableAmount = BigDecimal.ZERO;
        } else {
            payableAmount = total;
        }
    }
}