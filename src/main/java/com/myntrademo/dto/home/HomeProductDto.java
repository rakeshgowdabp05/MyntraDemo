package com.myntrademo.dto.home;

import java.math.BigDecimal;

public class HomeProductDto {

    private Long productId;
    private String productName;
    private String brandName;
    private String categoryName;
    private String imageUrl;
    private BigDecimal basePrice = BigDecimal.ZERO;
    private BigDecimal sellingPrice = BigDecimal.ZERO;
    private int discountPercent;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getDisplayProductName() {
        return productName == null ? "" : productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBrandName() {
        return brandName;
    }

    public String getDisplayBrandName() {
        return brandName == null || brandName.isBlank() ? "MyntraDemo" : brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getDisplayCategoryName() {
        return categoryName == null ? "" : categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isImageAvailable() {
        return imageUrl != null && !imageUrl.isBlank();
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice == null ? BigDecimal.ZERO : basePrice;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice == null ? BigDecimal.ZERO : sellingPrice;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public boolean isDiscountAvailable() {
        return discountPercent > 0 && basePrice.compareTo(sellingPrice) > 0;
    }

    public void setDiscountPercent(int discountPercent) {
        this.discountPercent = Math.max(discountPercent, 0);
    }
}