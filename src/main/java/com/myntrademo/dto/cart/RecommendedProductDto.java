package com.myntrademo.dto.cart;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class RecommendedProductDto {

    private Long productId;
    private String productName;
    private String brandName;
    private String categoryName;
    private String imageUrl;
    private BigDecimal mrpPrice = BigDecimal.ZERO;
    private BigDecimal sellingPrice = BigDecimal.ZERO;

    public RecommendedProductDto() {
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public boolean isImageAvailable() {
        return imageUrl != null && !imageUrl.isBlank();
    }

    public boolean isDiscounted() {
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
        if (mrpPrice.compareTo(BigDecimal.ZERO) <= 0 || !isDiscounted()) {
            return BigDecimal.ZERO;
        }

        return getDiscountAmount()
                .multiply(BigDecimal.valueOf(100))
                .divide(mrpPrice, 0, RoundingMode.HALF_UP);
    }
}