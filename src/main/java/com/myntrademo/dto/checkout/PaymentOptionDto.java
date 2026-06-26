package com.myntrademo.dto.checkout;

import java.math.BigDecimal;

public class PaymentOptionDto {

    private Long optionId;
    private String methodCode;
    private String optionCode;
    private String optionLabel;
    private String optionSubtitle;
    private String optionNotice;
    private String logoText;
    private BigDecimal minOrderAmount;
    private boolean disabled;
    private int sortOrder;

    public Long getOptionId() {
        return optionId;
    }

    public void setOptionId(Long optionId) {
        this.optionId = optionId;
    }

    public String getMethodCode() {
        return methodCode;
    }

    public void setMethodCode(String methodCode) {
        this.methodCode = methodCode;
    }

    public String getOptionCode() {
        return optionCode;
    }

    public void setOptionCode(String optionCode) {
        this.optionCode = optionCode;
    }

    public String getOptionLabel() {
        return optionLabel;
    }

    public void setOptionLabel(String optionLabel) {
        this.optionLabel = optionLabel;
    }

    public String getOptionSubtitle() {
        return optionSubtitle;
    }

    public void setOptionSubtitle(String optionSubtitle) {
        this.optionSubtitle = optionSubtitle;
    }

    public boolean isSubtitleAvailable() {
        return optionSubtitle != null && !optionSubtitle.isBlank();
    }

    public String getOptionNotice() {
        return optionNotice;
    }

    public void setOptionNotice(String optionNotice) {
        this.optionNotice = optionNotice;
    }

    public boolean isNoticeAvailable() {
        return optionNotice != null && !optionNotice.isBlank();
    }

    public String getLogoText() {
        return logoText;
    }

    public void setLogoText(String logoText) {
        this.logoText = logoText;
    }

    public BigDecimal getMinOrderAmount() {
        return minOrderAmount;
    }

    public void setMinOrderAmount(BigDecimal minOrderAmount) {
        this.minOrderAmount = minOrderAmount;
    }

    public boolean isMinOrderAvailable() {
        return minOrderAmount != null && minOrderAmount.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }
}