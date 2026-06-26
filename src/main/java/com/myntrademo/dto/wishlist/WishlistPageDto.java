package com.myntrademo.dto.wishlist;

import java.util.ArrayList;
import java.util.List;

public class WishlistPageDto {

    private List<WishlistItemDto> items = new ArrayList<>();

    public WishlistPageDto() {
    }

    public WishlistPageDto(List<WishlistItemDto> items) {
        setItems(items);
    }

    public List<WishlistItemDto> getItems() {
        return items;
    }

    public void setItems(List<WishlistItemDto> items) {
        if (items == null) {
            this.items = new ArrayList<>();
        } else {
            this.items = items;
        }
    }

    public int getTotalItems() {
        return items.size();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}