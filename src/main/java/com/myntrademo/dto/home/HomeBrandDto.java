package com.myntrademo.dto.home;

public class HomeBrandDto {

    private Long brandId;
    private String brandName;
    private String imageUrl;
    private int productCount;

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public String getDisplayBrandName() {
        return brandName == null ? "" : brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
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

    public int getProductCount() {
        return productCount;
    }

    public String getProductCountLabel() {
        if (productCount == 1) {
            return "1 product";
        }

        return productCount + " products";
    }

    public void setProductCount(int productCount) {
        this.productCount = Math.max(productCount, 0);
    }
}