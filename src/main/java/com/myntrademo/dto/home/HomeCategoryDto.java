package com.myntrademo.dto.home;

public class HomeCategoryDto {

    private Long categoryId;
    private String categoryName;
    private String imageUrl;
    private int productCount;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
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

    public int getProductCount() {
        return productCount;
    }

    public String getProductCountLabel() {
        if (productCount == 1) {
            return "1 style";
        }

        return productCount + " styles";
    }

    public void setProductCount(int productCount) {
        this.productCount = Math.max(productCount, 0);
    }
}