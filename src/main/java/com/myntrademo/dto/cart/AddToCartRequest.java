package com.myntrademo.dto.cart;

public class AddToCartRequest {

    private Long productId;
    private Long variantId;
    private int quantity;

    public AddToCartRequest() {
    }

    public AddToCartRequest(Long productId, Long variantId, int quantity) {
        this.productId = productId;
        this.variantId = variantId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getVariantId() {
        return variantId;
    }

    public int getQuantity() {
        return quantity;
    }
}