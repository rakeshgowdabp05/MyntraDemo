package com.myntrademo.dto.catalog;

import java.util.List;

public class ProductListPageDto {

    private List<ProductCardDto> products;
    private int currentPage;
    private int totalPages;
    private int totalProducts;

    public ProductListPageDto(List<ProductCardDto> products, int currentPage, int totalPages, int totalProducts) {
        this.products = products;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalProducts = totalProducts;
    }

    public List<ProductCardDto> getProducts() {
        return products;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalProducts() {
        return totalProducts;
    }

    public boolean hasProducts() {
        return products != null && !products.isEmpty();
    }

    public boolean hasPreviousPage() {
        return currentPage > 1;
    }

    public boolean hasNextPage() {
        return currentPage < totalPages;
    }
}