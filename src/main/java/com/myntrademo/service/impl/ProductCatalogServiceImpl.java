package com.myntrademo.service.impl;

import com.myntrademo.constant.CatalogConstants;
import com.myntrademo.dao.BrandDao;
import com.myntrademo.dao.CategoryDao;
import com.myntrademo.dao.ProductDao;
import com.myntrademo.dao.impl.JdbcBrandDao;
import com.myntrademo.dao.impl.JdbcCategoryDao;
import com.myntrademo.dao.impl.JdbcProductDao;
import com.myntrademo.dto.catalog.BrandDto;
import com.myntrademo.dto.catalog.CategoryDto;
import com.myntrademo.dto.catalog.ProductDetailDto;
import com.myntrademo.dto.catalog.ProductFilterRequest;
import com.myntrademo.dto.catalog.ProductListPageDto;
import com.myntrademo.service.ProductCatalogService;
import com.myntrademo.util.ValidationUtil;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ProductCatalogServiceImpl implements ProductCatalogService {

    private static final int REVIEW_LIMIT = 6;
    private static final int SIMILAR_PRODUCT_LIMIT = 18;

    private final ProductDao productDao;
    private final CategoryDao categoryDao;
    private final BrandDao brandDao;

    public ProductCatalogServiceImpl() {
        this.productDao = new JdbcProductDao();
        this.categoryDao = new JdbcCategoryDao();
        this.brandDao = new JdbcBrandDao();
    }

    public ProductCatalogServiceImpl(ProductDao productDao, CategoryDao categoryDao, BrandDao brandDao) {
        this.productDao = productDao;
        this.categoryDao = categoryDao;
        this.brandDao = brandDao;
    }

    @Override
    public ProductListPageDto getProductListPage(ProductFilterRequest filterRequest) throws SQLException {
        normalizeFilter(filterRequest);

        int totalProducts = productDao.countProducts(filterRequest);
        int totalPages = Math.max(1, (int) Math.ceil((double) totalProducts / filterRequest.getPageSize()));

        if (filterRequest.getPage() > totalPages) {
            filterRequest.setPage(totalPages);
        }

        return new ProductListPageDto(
                productDao.findProducts(filterRequest),
                filterRequest.getPage(),
                totalPages,
                totalProducts
        );
    }

    @Override
    public Optional<ProductDetailDto> getProductDetail(Long productId, Long userId) throws SQLException {
        Optional<ProductDetailDto> productDetail = productDao.findProductDetailById(productId);

        if (productDetail.isPresent()) {
            ProductDetailDto detail = productDetail.get();
            detail.setImages(productDao.findImagesByProductId(productId));
            detail.setVariants(productDao.findActiveVariantsByProductId(productId));
            detail.setSpecifications(productDao.findSpecificationsByProductId(productId));
            detail.setReviewSummary(productDao.findReviewSummaryByProductId(productId));
            detail.setReviews(productDao.findApprovedReviewsByProductId(productId, REVIEW_LIMIT));
            detail.setOffers(productDao.findActiveOffersByProductId(productId));
            detail.setServicePromises(productDao.findActiveServicePromisesByProductId(productId));
            detail.setSimilarProducts(productDao.findSimilarProducts(
                    productId,
                    detail.getCategoryId(),
                    detail.getBrandId(),
                    SIMILAR_PRODUCT_LIMIT
            ));
            productDao.findSellerByProductId(productId).ifPresent(detail::setSeller);
            productDao.findDefaultDeliveryAddress(userId).ifPresent(detail::setDeliveryAddress);
        }

        return productDetail;
    }

    @Override
    public List<CategoryDto> getActiveCategories() throws SQLException {
        return categoryDao.findAllActive();
    }

    @Override
    public List<BrandDto> getActiveBrands() throws SQLException {
        return brandDao.findAllActive();
    }

    private void normalizeFilter(ProductFilterRequest filterRequest) {
        if (ValidationUtil.isBlank(filterRequest.getSortBy())) {
            filterRequest.setSortBy(CatalogConstants.DEFAULT_SORT);
        }

        if (filterRequest.getPage() < CatalogConstants.DEFAULT_PAGE) {
            filterRequest.setPage(CatalogConstants.DEFAULT_PAGE);
        }
    }
}
