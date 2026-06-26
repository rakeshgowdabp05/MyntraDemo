package com.myntrademo.dao;

import com.myntrademo.dto.catalog.ProductAddressDto;
import com.myntrademo.dto.catalog.ProductCardDto;
import com.myntrademo.dto.catalog.ProductDetailDto;
import com.myntrademo.dto.catalog.ProductFilterRequest;
import com.myntrademo.dto.catalog.ProductImageDto;
import com.myntrademo.dto.catalog.ProductOfferDto;
import com.myntrademo.dto.catalog.ProductReviewDto;
import com.myntrademo.dto.catalog.ProductReviewSummaryDto;
import com.myntrademo.dto.catalog.ProductSellerDto;
import com.myntrademo.dto.catalog.ProductServicePromiseDto;
import com.myntrademo.dto.catalog.ProductSpecificationDto;
import com.myntrademo.dto.catalog.ProductVariantDto;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ProductDao {

    List<ProductCardDto> findProducts(ProductFilterRequest filterRequest) throws SQLException;

    int countProducts(ProductFilterRequest filterRequest) throws SQLException;

    Optional<ProductDetailDto> findProductDetailById(Long productId) throws SQLException;

    List<ProductImageDto> findImagesByProductId(Long productId) throws SQLException;

    List<ProductVariantDto> findActiveVariantsByProductId(Long productId) throws SQLException;

    List<ProductSpecificationDto> findSpecificationsByProductId(Long productId) throws SQLException;

    ProductReviewSummaryDto findReviewSummaryByProductId(Long productId) throws SQLException;

    List<ProductReviewDto> findApprovedReviewsByProductId(Long productId, int limit) throws SQLException;

    List<ProductCardDto> findSimilarProducts(Long productId, Long categoryId, Long brandId, int limit) throws SQLException;

    List<ProductOfferDto> findActiveOffersByProductId(Long productId) throws SQLException;

    List<ProductServicePromiseDto> findActiveServicePromisesByProductId(Long productId) throws SQLException;

    Optional<ProductSellerDto> findSellerByProductId(Long productId) throws SQLException;

    Optional<ProductAddressDto> findDefaultDeliveryAddress(Long userId) throws SQLException;
}
