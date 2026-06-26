package com.myntrademo.dto.checkout;

import java.math.BigDecimal;
import java.time.LocalDate;

public class GiftCardDto {

    private String giftCardCode;
    private String giftCardTitle;
    private BigDecimal balanceAmount = BigDecimal.ZERO;
    private BigDecimal minimumOrderAmount = BigDecimal.ZERO;
    private LocalDate expiresOn;

    public String getGiftCardCode() {
        return giftCardCode;
    }

    public void setGiftCardCode(String giftCardCode) {
        this.giftCardCode = giftCardCode;
    }

    public String getGiftCardTitle() {
        return giftCardTitle;
    }

    public void setGiftCardTitle(String giftCardTitle) {
        this.giftCardTitle = giftCardTitle;
    }

    public BigDecimal getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(BigDecimal balanceAmount) {
        this.balanceAmount = balanceAmount == null ? BigDecimal.ZERO : balanceAmount;
    }

    public BigDecimal getMinimumOrderAmount() {
        return minimumOrderAmount;
    }

    public void setMinimumOrderAmount(BigDecimal minimumOrderAmount) {
        this.minimumOrderAmount = minimumOrderAmount == null ? BigDecimal.ZERO : minimumOrderAmount;
    }

    public LocalDate getExpiresOn() {
        return expiresOn;
    }

    public void setExpiresOn(LocalDate expiresOn) {
        this.expiresOn = expiresOn;
    }
}