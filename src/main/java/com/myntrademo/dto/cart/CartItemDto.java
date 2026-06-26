package com.myntrademo.dto.cart;

import java.math.BigDecimal;

public class CartItemDto {

    private Long cartItemId;
    private Long productId;
    private Long variantId;
    private String productName;
    private String brandName;
    private String size;
    private String color;
    private String imageUrl;
    private int quantity;
    private int stockQuantity;
    private BigDecimal mrpPrice = BigDecimal.ZERO;
    private BigDecimal priceAtAdded = BigDecimal.ZERO;

    public CartItemDto() {
    }

    public Long getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(Long cartItemId) {
        this.cartItemId = cartItemId;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

    public BigDecimal getPriceAtAdded() {
        return priceAtAdded;
    }

    public void setPriceAtAdded(BigDecimal priceAtAdded) {
        if (priceAtAdded != null) {
            this.priceAtAdded = priceAtAdded;
        }
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

    public boolean hasImage() {
        return imageUrl != null && !imageUrl.isBlank();
    }

    public BigDecimal getItemTotal() {
        return priceAtAdded.multiply(BigDecimal.valueOf(quantity));
    }

    public BigDecimal getItemMrpTotal() {
        return mrpPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public BigDecimal getItemDiscountTotal() {
        BigDecimal discount = mrpPrice.subtract(priceAtAdded);

        if (discount.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        return discount.multiply(BigDecimal.valueOf(quantity));
    }

    public boolean hasDiscount() {
        return getItemDiscountTotal().compareTo(BigDecimal.ZERO) > 0;
    }
}