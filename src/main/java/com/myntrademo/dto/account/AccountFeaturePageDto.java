package com.myntrademo.dto.account;

import java.util.ArrayList;
import java.util.List;

public class AccountFeaturePageDto {

    private String title;
    private String subtitle;
    private String emptyTitle;
    private String emptyDescription;
    private String activeMenu;
    private String iconText;
    private List<AccountFeatureItemDto> items = new ArrayList<>();

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

    public String getEmptyTitle() {
        return emptyTitle == null ? "" : emptyTitle;
    }

    public void setEmptyTitle(String emptyTitle) {
        this.emptyTitle = emptyTitle;
    }

    public String getEmptyDescription() {
        return emptyDescription == null ? "" : emptyDescription;
    }

    public void setEmptyDescription(String emptyDescription) {
        this.emptyDescription = emptyDescription;
    }

    public String getActiveMenu() {
        return activeMenu == null ? "" : activeMenu;
    }

    public void setActiveMenu(String activeMenu) {
        this.activeMenu = activeMenu;
    }

    public String getIconText() {
        return iconText == null ? "" : iconText;
    }

    public void setIconText(String iconText) {
        this.iconText = iconText;
    }

    public List<AccountFeatureItemDto> getItems() {
        return items;
    }

    public void setItems(List<AccountFeatureItemDto> items) {
        this.items = items == null ? new ArrayList<>() : items;
    }

    public boolean isEmpty() {
        return items == null || items.isEmpty();
    }

    public int getTotalItems() {
        return items == null ? 0 : items.size();
    }
}