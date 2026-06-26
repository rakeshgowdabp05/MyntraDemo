package com.myntrademo.dto.catalog;

public class ProductSellerDto {

    private String sellerName;
    private Integer deliveryMinDays;
    private Integer deliveryMaxDays;
    private String returnPolicy;
    private String exchangePolicy;
    private boolean codAvailable;
    private String originalProductText;

    public ProductSellerDto() {
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public Integer getDeliveryMinDays() {
        return deliveryMinDays;
    }

    public void setDeliveryMinDays(Integer deliveryMinDays) {
        this.deliveryMinDays = deliveryMinDays;
    }

    public Integer getDeliveryMaxDays() {
        return deliveryMaxDays;
    }

    public void setDeliveryMaxDays(Integer deliveryMaxDays) {
        this.deliveryMaxDays = deliveryMaxDays;
    }

    public String getReturnPolicy() {
        return returnPolicy;
    }

    public void setReturnPolicy(String returnPolicy) {
        this.returnPolicy = returnPolicy;
    }

    public String getExchangePolicy() {
        return exchangePolicy;
    }

    public void setExchangePolicy(String exchangePolicy) {
        this.exchangePolicy = exchangePolicy;
    }

    public boolean isCodAvailable() {
        return codAvailable;
    }

    public void setCodAvailable(boolean codAvailable) {
        this.codAvailable = codAvailable;
    }

    public String getOriginalProductText() {
        return originalProductText;
    }

    public void setOriginalProductText(String originalProductText) {
        this.originalProductText = originalProductText;
    }

    public boolean hasSellerName() {
        return sellerName != null && !sellerName.isBlank();
    }
}
