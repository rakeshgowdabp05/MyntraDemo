package com.myntrademo.dto.catalog;

import com.myntrademo.constant.CatalogConstants;

public class ProductFilterRequest {

    private String search;
    private Long categoryId;
    private Long brandId;
    private String sortBy = CatalogConstants.DEFAULT_SORT;
    private int page = CatalogConstants.DEFAULT_PAGE;
    private int pageSize = CatalogConstants.DEFAULT_PAGE_SIZE;

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = Math.max(page, CatalogConstants.DEFAULT_PAGE);
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getOffset() {
        return (page - 1) * pageSize;
    }
}