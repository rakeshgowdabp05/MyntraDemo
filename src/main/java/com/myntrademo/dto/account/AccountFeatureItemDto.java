package com.myntrademo.dto.account;

public class AccountFeatureItemDto {

    private String title;
    private String subtitle;
    private String detail;
    private String amount;
    private String status;
    private String badge;

    public AccountFeatureItemDto() {
    }

    public AccountFeatureItemDto(String title, String subtitle, String detail, String amount, String status, String badge) {
        this.title = title;
        this.subtitle = subtitle;
        this.detail = detail;
        this.amount = amount;
        this.status = status;
        this.badge = badge;
    }

    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle == null ? "" : subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getDetail() {
        return detail == null ? "" : detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getAmount() {
        return amount == null ? "" : amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status == null ? "" : status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBadge() {
        return badge == null ? "" : badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public boolean isAmountAvailable() {
        return amount != null && !amount.isBlank();
    }

    public boolean isDetailAvailable() {
        return detail != null && !detail.isBlank();
    }

    public boolean isBadgeAvailable() {
        return badge != null && !badge.isBlank();
    }

    public boolean isStatusAvailable() {
        return status != null && !status.isBlank();
    }
}