package com.myntrademo.dto.catalog;

public class ProductOfferDto {

    private String offerTitle;
    private String offerDescription;
    private String termsText;

    public ProductOfferDto() {
    }

    public ProductOfferDto(String offerTitle, String offerDescription, String termsText) {
        this.offerTitle = offerTitle;
        this.offerDescription = offerDescription;
        this.termsText = termsText;
    }

    public String getOfferTitle() {
        return offerTitle;
    }

    public void setOfferTitle(String offerTitle) {
        this.offerTitle = offerTitle;
    }

    public String getOfferDescription() {
        return offerDescription;
    }

    public void setOfferDescription(String offerDescription) {
        this.offerDescription = offerDescription;
    }

    public String getTermsText() {
        return termsText;
    }

    public void setTermsText(String termsText) {
        this.termsText = termsText;
    }
}
