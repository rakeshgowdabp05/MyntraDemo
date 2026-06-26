package com.myntrademo.dto.catalog;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductDetailDto {

    private Long productId;
    private String productName;
    private String slug;
    private String shortDescription;
    private String description;
    private String brandName;
    private String categoryName;
    private Long brandId;
    private Long categoryId;
    private BigDecimal basePrice;
    private BigDecimal sellingPrice;
    private Integer discountPercent;
    private List<ProductImageDto> images = new ArrayList<>();
    private List<ProductVariantDto> variants = new ArrayList<>();
    private List<ProductSpecificationDto> specifications = new ArrayList<>();
    private List<ProductOfferDto> offers = new ArrayList<>();
    private List<ProductServicePromiseDto> servicePromises = new ArrayList<>();
    private List<ProductReviewDto> reviews = new ArrayList<>();
    private List<ProductCardDto> similarProducts = new ArrayList<>();
    private ProductReviewSummaryDto reviewSummary = new ProductReviewSummaryDto();
    private ProductSellerDto seller;
    private ProductAddressDto deliveryAddress;

    public ProductDetailDto() {
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public Integer getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(Integer discountPercent) {
        this.discountPercent = discountPercent;
    }

    public List<ProductImageDto> getImages() {
        return images;
    }

    public void setImages(List<ProductImageDto> images) {
        this.images = images;
    }

    public List<ProductVariantDto> getVariants() {
        return variants;
    }

    public void setVariants(List<ProductVariantDto> variants) {
        this.variants = variants;
    }

    public List<ProductSpecificationDto> getSpecifications() {
        return specifications;
    }

    public void setSpecifications(List<ProductSpecificationDto> specifications) {
        this.specifications = specifications;
    }

    public List<ProductOfferDto> getOffers() {
        return offers;
    }

    public void setOffers(List<ProductOfferDto> offers) {
        this.offers = offers;
    }

    public List<ProductServicePromiseDto> getServicePromises() {
        return servicePromises;
    }

    public void setServicePromises(List<ProductServicePromiseDto> servicePromises) {
        this.servicePromises = servicePromises;
    }

    public List<ProductReviewDto> getReviews() {
        return reviews;
    }

    public void setReviews(List<ProductReviewDto> reviews) {
        this.reviews = reviews;
    }

    public List<ProductCardDto> getSimilarProducts() {
        return similarProducts;
    }

    public void setSimilarProducts(List<ProductCardDto> similarProducts) {
        this.similarProducts = similarProducts;
    }

    public ProductReviewSummaryDto getReviewSummary() {
        return reviewSummary;
    }

    public void setReviewSummary(ProductReviewSummaryDto reviewSummary) {
        this.reviewSummary = reviewSummary;
    }

    public ProductSellerDto getSeller() {
        return seller;
    }

    public void setSeller(ProductSellerDto seller) {
        this.seller = seller;
    }

    public ProductAddressDto getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(ProductAddressDto deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public boolean hasDiscount() {
        return discountPercent != null && discountPercent > 0;
    }

    public boolean hasImages() {
        return images != null && !images.isEmpty();
    }

    public boolean hasVariants() {
        return variants != null && !variants.isEmpty();
    }

    public boolean hasSpecifications() {
        return specifications != null && !specifications.isEmpty();
    }

    public boolean hasOffers() {
        return offers != null && !offers.isEmpty();
    }

    public boolean hasServicePromises() {
        return servicePromises != null && !servicePromises.isEmpty();
    }

    public boolean hasReviews() {
        return reviews != null && !reviews.isEmpty();
    }

    public boolean hasSimilarProducts() {
        return similarProducts != null && !similarProducts.isEmpty();
    }

    public boolean hasSeller() {
        return seller != null && seller.hasSellerName();
    }

    public boolean hasDeliveryAddress() {
        return deliveryAddress != null && deliveryAddress.hasPincode();
    }

    public boolean hasReviewSummary() {
        return reviewSummary != null && reviewSummary.hasReviews();
    }

    public boolean hasColorVariants() {
        if (variants == null) {
            return false;
        }

        return variants.stream().anyMatch(ProductVariantDto::hasColor);
    }
}

