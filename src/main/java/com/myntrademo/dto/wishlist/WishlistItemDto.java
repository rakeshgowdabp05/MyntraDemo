package com.myntrademo.dto.wishlist;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class WishlistItemDto {

    private Long wishlistItemId;
    private Long productId;
    private Long variantId;
    private String productName;
    private String brandName;
    private String categoryName;
    private String size;
    private String color;
    private String imageUrl;
    private int stockQuantity;
    private BigDecimal mrpPrice = BigDecimal.ZERO;
    private BigDecimal sellingPrice = BigDecimal.ZERO;

    public Long getWishlistItemId() {
        return wishlistItemId;
    }

    public void setWishlistItemId(Long wishlistItemId) {
        this.wishlistItemId = wishlistItemId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getVariantId() {
        return variantId;
    }

    public void setVariantId(Long variantId) {
        this.variantId = variantId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public BigDecimal getMrpPrice() {
        return mrpPrice;
    }

    public void setMrpPrice(BigDecimal mrpPrice) {
        if (mrpPrice != null) {
            this.mrpPrice = mrpPrice;
        }
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        if (sellingPrice != null) {
            this.sellingPrice = sellingPrice;
        }
    }

    public String getDisplaySize() {
        if (size == null || size.isBlank()) {
            return "Select on product page";
        }

        return size;
    }

    public boolean isImageAvailable() {
        return imageUrl != null && !imageUrl.isBlank();
    }

    public boolean isInStock() {
        return stockQuantity > 0;
    }

    public boolean isDiscountAvailable() {
        return getDiscountAmount().compareTo(BigDecimal.ZERO) > 0;
    }

    public BigDecimal getDiscountAmount() {
        BigDecimal discount = mrpPrice.subtract(sellingPrice);

        if (discount.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        return discount;
    }

    public BigDecimal getDiscountPercent() {
        if (mrpPrice.compareTo(BigDecimal.ZERO) <= 0 || !isDiscountAvailable()) {
            return BigDecimal.ZERO;
        }

        return getDiscountAmount()
                .multiply(BigDecimal.valueOf(100))
                .divide(mrpPrice, 0, RoundingMode.HALF_UP);
    }
}