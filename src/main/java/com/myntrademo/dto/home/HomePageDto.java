package com.myntrademo.dto.home;

import java.util.ArrayList;
import java.util.List;

public class HomePageDto {

    private List<HomeProductDto> heroProducts = new ArrayList<>();
    private List<HomeCategoryDto> categories = new ArrayList<>();
    private List<HomeProductDto> dealProducts = new ArrayList<>();
    private List<HomeProductDto> newArrivals = new ArrayList<>();
    private List<HomeProductDto> trendingProducts = new ArrayList<>();
    private List<HomeBrandDto> brands = new ArrayList<>();

    public List<HomeProductDto> getHeroProducts() {
        return heroProducts;
    }

    public void setHeroProducts(List<HomeProductDto> heroProducts) {
        this.heroProducts = heroProducts == null ? new ArrayList<>() : heroProducts;
    }

    public boolean isHeroProductsAvailable() {
        return heroProducts != null && !heroProducts.isEmpty();
    }

    public List<HomeCategoryDto> getCategories() {
        return categories;
    }

    public void setCategories(List<HomeCategoryDto> categories) {
        this.categories = categories == null ? new ArrayList<>() : categories;
    }

    public boolean isCategoriesAvailable() {
        return categories != null && !categories.isEmpty();
    }

    public List<HomeProductDto> getDealProducts() {
        return dealProducts;
    }

    public void setDealProducts(List<HomeProductDto> dealProducts) {
        this.dealProducts = dealProducts == null ? new ArrayList<>() : dealProducts;
    }

    public boolean isDealProductsAvailable() {
        return dealProducts != null && !dealProducts.isEmpty();
    }

    public List<HomeProductDto> getNewArrivals() {
        return newArrivals;
    }

    public void setNewArrivals(List<HomeProductDto> newArrivals) {
        this.newArrivals = newArrivals == null ? new ArrayList<>() : newArrivals;
    }

    public boolean isNewArrivalsAvailable() {
        return newArrivals != null && !newArrivals.isEmpty();
    }

    public List<HomeProductDto> getTrendingProducts() {
        return trendingProducts;
    }

    public void setTrendingProducts(List<HomeProductDto> trendingProducts) {
        this.trendingProducts = trendingProducts == null ? new ArrayList<>() : trendingProducts;
    }

    public boolean isTrendingProductsAvailable() {
        return trendingProducts != null && !trendingProducts.isEmpty();
    }

    public List<HomeBrandDto> getBrands() {
        return brands;
    }

    public void setBrands(List<HomeBrandDto> brands) {
        this.brands = brands == null ? new ArrayList<>() : brands;
    }

    public boolean isBrandsAvailable() {
        return brands != null && !brands.isEmpty();
    }
}