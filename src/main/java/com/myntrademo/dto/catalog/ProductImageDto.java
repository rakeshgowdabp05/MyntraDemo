package com.myntrademo.dto.catalog;

public class ProductImageDto {

    private String imageUrl;
    private String altText;
    private boolean primary;

    public ProductImageDto() {
    }

    public ProductImageDto(String imageUrl, String altText, boolean primary) {
        this.imageUrl = imageUrl;
        this.altText = altText;
        this.primary = primary;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getAltText() {
        return altText;
    }

    public boolean isPrimary() {
        return primary;
    }
}