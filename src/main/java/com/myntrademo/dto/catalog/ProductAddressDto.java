package com.myntrademo.dto.catalog;

public class ProductAddressDto {

    private String fullName;
    private String pincode;
    private String addressLine;

    public ProductAddressDto() {
    }

    public ProductAddressDto(String fullName, String pincode, String addressLine) {
        this.fullName = fullName;
        this.pincode = pincode;
        this.addressLine = addressLine;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public boolean hasPincode() {
        return pincode != null && !pincode.isBlank();
    }
}
