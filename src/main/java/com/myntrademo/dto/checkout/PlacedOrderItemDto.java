package com.myntrademo.dto.checkout;

import java.math.BigDecimal;

public class PlacedOrderItemDto {

    private Long orderItemId;
    private Long productId;
    private Long variantId;
    private String brandName;
    private String productName;
    private String sizeLabel;
    private String colorLabel;
    private String imageUrl;
    private int quantity;
    private BigDecimal mrpPrice = BigDecimal.ZERO;
    private BigDecimal sellingPrice = BigDecimal.ZERO;
    private BigDecimal itemTotal = BigDecimal.ZERO;

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
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

    public String getBrandName() {
        return brandName;
    }

    public String getDisplayBrandName() {
        return brandName == null || brandName.isBlank() ? "MyntraDemo" : brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
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

    public String getSizeLabel() {
        return sizeLabel;
    }

    public String getDisplaySizeLabel() {
        return sizeLabel == null || sizeLabel.isBlank() ? "Free Size" : sizeLabel;
    }

    public void setSizeLabel(String sizeLabel) {
        this.sizeLabel = sizeLabel;
    }

    public String getColorLabel() {
        return colorLabel;
    }

    public void setColorLabel(String colorLabel) {
        this.colorLabel = colorLabel;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public BigDecimal getMrpPrice() {
        return mrpPrice;
    }

    public void setMrpPrice(BigDecimal mrpPrice) {
        this.mrpPrice = mrpPrice == null ? BigDecimal.ZERO : mrpPrice;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice == null ? BigDecimal.ZERO : sellingPrice;
    }

    public BigDecimal getItemTotal() {
        return itemTotal;
    }

    public void setItemTotal(BigDecimal itemTotal) {
        this.itemTotal = itemTotal == null ? BigDecimal.ZERO : itemTotal;
    }

    public BigDecimal getItemSaving() {
        BigDecimal mrpTotal = mrpPrice.multiply(BigDecimal.valueOf(quantity));
        BigDecimal saving = mrpTotal.subtract(itemTotal);

        if (saving.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO;
        }

        return saving;
    }

    public boolean isItemSavingAvailable() {
        return getItemSaving().compareTo(BigDecimal.ZERO) > 0;
    }
}