package com.myntrademo.dto.checkout;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PaymentMethodDto {

    private Long methodId;
    private String code;
    private String label;
    private String detailTitle;
    private String detailDescription;
    private String tabOfferText;
    private BigDecimal feeAmount = BigDecimal.ZERO;
    private boolean recommended;
    private int sortOrder;
    private List<PaymentOptionDto> options = new ArrayList<>();

    public Long getMethodId() {
        return methodId;
    }

    public void setMethodId(Long methodId) {
        this.methodId = methodId;
    }

    public String getCode() {
        return code;
    }

    public String getCssClass() {
        return code == null ? "" : code.toLowerCase().replace("_", "-");
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDetailTitle() {
        return detailTitle;
    }

    public void setDetailTitle(String detailTitle) {
        this.detailTitle = detailTitle;
    }

    public String getDetailDescription() {
        return detailDescription;
    }

    public void setDetailDescription(String detailDescription) {
        this.detailDescription = detailDescription;
    }

    public String getTabOfferText() {
        return tabOfferText;
    }

    public void setTabOfferText(String tabOfferText) {
        this.tabOfferText = tabOfferText;
    }

    public boolean isOfferAvailable() {
        return tabOfferText != null && !tabOfferText.isBlank();
    }

    public BigDecimal getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(BigDecimal feeAmount) {
        this.feeAmount = feeAmount == null ? BigDecimal.ZERO : feeAmount;
    }

    public boolean isFeeAvailable() {
        return feeAmount.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isRecommended() {
        return recommended;
    }

    public void setRecommended(boolean recommended) {
        this.recommended = recommended;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public List<PaymentOptionDto> getOptions() {
        return options;
    }

    public void setOptions(List<PaymentOptionDto> options) {
        this.options = options == null ? new ArrayList<>() : options;
    }

    public boolean isOptionsAvailable() {
        return options != null && !options.isEmpty();
    }
}