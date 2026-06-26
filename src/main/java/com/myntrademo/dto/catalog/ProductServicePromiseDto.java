package com.myntrademo.dto.catalog;

public class ProductServicePromiseDto {

    private String promiseTitle;
    private String promiseSubtitle;
    private String iconKey;

    public ProductServicePromiseDto() {
    }

    public ProductServicePromiseDto(String promiseTitle, String promiseSubtitle, String iconKey) {
        this.promiseTitle = promiseTitle;
        this.promiseSubtitle = promiseSubtitle;
        this.iconKey = iconKey;
    }

    public String getPromiseTitle() {
        return promiseTitle;
    }

    public void setPromiseTitle(String promiseTitle) {
        this.promiseTitle = promiseTitle;
    }

    public String getPromiseSubtitle() {
        return promiseSubtitle;
    }

    public void setPromiseSubtitle(String promiseSubtitle) {
        this.promiseSubtitle = promiseSubtitle;
    }

    public String getIconKey() {
        return iconKey;
    }

    public void setIconKey(String iconKey) {
        this.iconKey = iconKey;
    }
}
