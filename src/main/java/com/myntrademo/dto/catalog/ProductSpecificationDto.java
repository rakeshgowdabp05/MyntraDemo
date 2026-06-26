package com.myntrademo.dto.catalog;

public class ProductSpecificationDto {

    private String specName;
    private String specValue;

    public ProductSpecificationDto() {
    }

    public ProductSpecificationDto(String specName, String specValue) {
        this.specName = specName;
        this.specValue = specValue;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public String getSpecValue() {
        return specValue;
    }

    public void setSpecValue(String specValue) {
        this.specValue = specValue;
    }
}
