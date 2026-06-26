package com.myntrademo.dto.catalog;

import java.math.BigDecimal;

public class ProductVariantDto {

    private Long variantId;
    private String sku;
    private String size;
    private String color;
    private BigDecimal price;
    private BigDecimal sellingPrice;
    private Integer stockQuantity;
    private boolean active;

    public ProductVariantDto() {
    }

    public Long getVariantId() {
        return variantId;
    }

    public void setVariantId(Long variantId) {
        this.variantId = variantId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getDisplaySize() {
        if (size == null || size.isBlank()) {
            return "Free Size";
        }

        return size;
    }

    public String getDisplayColor() {
        if (color == null || color.isBlank()) {
            return "Default";
        }

        return color;
    }

    public boolean hasColor() {
        return color != null && !color.isBlank();
    }

    public boolean isAvailable() {
        return active && stockQuantity != null && stockQuantity > 0;
    }
}
