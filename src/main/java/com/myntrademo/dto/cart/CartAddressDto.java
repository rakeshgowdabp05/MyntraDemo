package com.myntrademo.dto.cart;

public class CartAddressDto {

    private Long addressId;
    private Long userId;
    private String fullName;
    private String phone;
    private String pincode;
    private String addressLine;
    private String locality;
    private String city;
    private String state;
    private String country;
    private String addressType;
    private boolean defaultAddress;

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public boolean isDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(boolean defaultAddress) {
        this.defaultAddress = defaultAddress;
    }

    public boolean hasPincode() {
        return pincode != null && !pincode.isBlank();
    }

    public String getDisplayAddress() {
        StringBuilder address = new StringBuilder();

        appendPart(address, addressLine);
        appendPart(address, locality);
        appendPart(address, city);
        appendPart(address, state);
        appendPart(address, country);

        return address.toString();
    }

    private void appendPart(StringBuilder builder, String value) {
        if (value == null || value.isBlank()) {
            return;
        }

        if (!builder.isEmpty()) {
            builder.append(", ");
        }

        builder.append(value.trim());
    }
}