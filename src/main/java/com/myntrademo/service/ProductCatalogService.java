package com.myntrademo.service;

import com.myntrademo.dto.catalog.BrandDto;
import com.myntrademo.dto.catalog.CategoryDto;
import com.myntrademo.dto.catalog.ProductDetailDto;
import com.myntrademo.dto.catalog.ProductFilterRequest;
import com.myntrademo.dto.catalog.ProductListPageDto;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ProductCatalogService {

    ProductListPageDto getProductListPage(ProductFilterRequest filterRequest) throws SQLException;

    Optional<ProductDetailDto> getProductDetail(Long productId, Long userId) throws SQLException;

    List<CategoryDto> getActiveCategories() throws SQLException;

    List<BrandDto> getActiveBrands() throws SQLException;
}
