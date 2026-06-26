<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${pageTitle} - MyntraDemo</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="stylesheet" href="${contextPath}/assets/css/common/base.css">
    <link rel="stylesheet" href="${contextPath}/assets/css/common/toast.css">
    <link rel="stylesheet" href="${contextPath}/assets/css/catalog/products.css">
</head>
<body>

<%@ include file="/WEB-INF/views/common/toast.jsp" %>
<%@ include file="/WEB-INF/views/common/header.jsp" %>

<main class="plp-page">
    <section class="plp-top">
        <nav class="plp-breadcrumb" aria-label="Breadcrumb">
            <a href="${contextPath}/home">Home</a>
            <span>/</span>
            <strong>Products</strong>
        </nav>

        <div class="plp-title-row">
            <div>
                <h1>
                    <c:choose>
                        <c:when test="${not empty pageTitle}">
                            ${pageTitle}
                        </c:when>
                        <c:otherwise>
                            All Products
                        </c:otherwise>
                    </c:choose>
                </h1>

                <c:if test="${not empty productPage}">
                    <p>${productPage.totalProducts} item(s)</p>
                </c:if>
            </div>

            <a class="plp-clear-link" href="${contextPath}/products">Clear All</a>
        </div>
    </section>

    <c:if test="${not empty errorMessage}">
        <section class="md-container">
            <div class="md-alert md-alert-error">${errorMessage}</div>
        </section>
    </c:if>

    <section class="plp-layout">
        <aside class="plp-sidebar">
            <form method="get" action="${contextPath}/products" class="plp-filter-form">
                <input type="hidden" name="search" value="${filter.search}">

                <div class="plp-filter-heading">
                    <h2>FILTERS</h2>
                    <a href="${contextPath}/products">CLEAR</a>
                </div>

                <section class="plp-filter-section">
                    <h3>Categories</h3>

                    <label class="plp-filter-option">
                        <input type="radio" name="categoryId" value="" <c:if test="${empty filter.categoryId}">checked</c:if>>
                        <span>All Categories</span>
                    </label>

                    <c:forEach var="category" items="${categories}">
                        <label class="plp-filter-option">
                            <input
                                    type="radio"
                                    name="categoryId"
                                    value="${category.categoryId}"
                                    <c:if test="${filter.categoryId eq category.categoryId}">checked</c:if>>
                            <span>${category.categoryName}</span>
                        </label>
                    </c:forEach>
                </section>

                <section class="plp-filter-section">
                    <h3>Brands</h3>

                    <label class="plp-filter-option">
                        <input type="radio" name="brandId" value="" <c:if test="${empty filter.brandId}">checked</c:if>>
                        <span>All Brands</span>
                    </label>

                    <c:forEach var="brand" items="${brands}">
                        <label class="plp-filter-option">
                            <input
                                    type="radio"
                                    name="brandId"
                                    value="${brand.brandId}"
                                    <c:if test="${filter.brandId eq brand.brandId}">checked</c:if>>
                            <span>${brand.brandName}</span>
                        </label>
                    </c:forEach>
                </section>

                <input type="hidden" name="sortBy" value="${filter.sortBy}">
                <input type="hidden" name="page" value="1">

                <button type="submit" class="plp-apply-btn">APPLY FILTERS</button>
            </form>
        </aside>

        <section class="plp-products-area">
            <form method="get" action="${contextPath}/products" class="plp-toolbar">
                <input type="hidden" name="search" value="${filter.search}">
                <input type="hidden" name="categoryId" value="${filter.categoryId}">
                <input type="hidden" name="brandId" value="${filter.brandId}">
                <input type="hidden" name="page" value="1">

                <div class="plp-toolbar-left">
                    <button type="button">Bundles</button>
                    <button type="button">Country of Origin</button>
                    <button type="button">Size</button>
                </div>

                <label class="plp-sort">
                    <span>Sort by:</span>
                    <select name="sortBy" onchange="this.form.submit()">
                        <option value="newest" <c:if test="${filter.sortBy eq 'newest'}">selected</c:if>>Newest First</option>
                        <option value="price_low_high" <c:if test="${filter.sortBy eq 'price_low_high'}">selected</c:if>>Price: Low to High</option>
                        <option value="price_high_low" <c:if test="${filter.sortBy eq 'price_high_low'}">selected</c:if>>Price: High to Low</option>
                        <option value="name_asc" <c:if test="${filter.sortBy eq 'name_asc'}">selected</c:if>>Product Name</option>
                    </select>
                </label>
            </form>

            <c:choose>
                <c:when test="${not empty productPage and productPage.hasProducts()}">
                    <section class="plp-grid" aria-label="Products">
                        <c:forEach var="product" items="${productPage.products}">
                            <article class="plp-card">
                                <a class="plp-card-link" href="${contextPath}/product?id=${product.productId}">
                                    <div class="plp-image-wrap">
                                        <c:choose>
                                            <c:when test="${product.hasImage()}">
                                                <c:choose>
                                                    <c:when test="${fn:startsWith(product.primaryImageUrl, 'http://') or fn:startsWith(product.primaryImageUrl, 'https://')}">
                                                        <img src="${product.primaryImageUrl}" alt="${product.brandName} ${product.productName}">
                                                    </c:when>
                                                    <c:otherwise>
                                                        <img src="${contextPath}${product.primaryImageUrl}" alt="${product.brandName} ${product.productName}">
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="plp-image-placeholder">Image coming soon</div>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>

                                    <div class="plp-card-info">
                                        <h2>${product.brandName}</h2>
                                        <p>${product.productName}</p>

                                        <div class="plp-card-price">
                                            <strong>${currencySymbol}${product.sellingPrice}</strong>

                                            <c:if test="${product.hasDiscount()}">
                                                <span>${currencySymbol}${product.basePrice}</span>
                                                <em>(${product.discountPercent}% OFF)</em>
                                            </c:if>
                                        </div>
                                    </div>
                                </a>

                                <form method="post" action="${contextPath}/wishlist/add" class="plp-wishlist-form">
                                    <input type="hidden" name="productId" value="${product.productId}">
                                    <input type="hidden" name="redirectTo" value="/products">

                                    <button type="submit" aria-label="Add ${product.productName} to wishlist">
                                        <svg viewBox="0 0 24 24" aria-hidden="true">
                                            <path d="M12.1 20.4 10.9 19C6.4 14.9 3.5 12.2 3.5 8.9A4.7 4.7 0 0 1 8.2 4.2c1.5 0 3 .7 3.9 1.8a5 5 0 0 1 3.9-1.8 4.7 4.7 0 0 1 4.7 4.7c0 3.3-2.9 6-7.4 10.1l-1.2 1.4Z"/>
                                        </svg>
                                        <span>Wishlist</span>
                                    </button>
                                </form>
                            </article>
                        </c:forEach>
                    </section>

                    <c:if test="${productPage.totalPages gt 1}">
                        <nav class="plp-pagination" aria-label="Product pages">
                            <c:choose>
                                <c:when test="${productPage.hasPreviousPage()}">
                                    <a href="${contextPath}/products?search=${filter.search}&categoryId=${filter.categoryId}&brandId=${filter.brandId}&sortBy=${filter.sortBy}&page=${productPage.currentPage - 1}">
                                        Previous
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    <span class="is-disabled">Previous</span>
                                </c:otherwise>
                            </c:choose>

                            <strong>Page ${productPage.currentPage} of ${productPage.totalPages}</strong>

                            <c:choose>
                                <c:when test="${productPage.hasNextPage()}">
                                    <a href="${contextPath}/products?search=${filter.search}&categoryId=${filter.categoryId}&brandId=${filter.brandId}&sortBy=${filter.sortBy}&page=${productPage.currentPage + 1}">
                                        Next
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    <span class="is-disabled">Next</span>
                                </c:otherwise>
                            </c:choose>
                        </nav>
                    </c:if>
                </c:when>

                <c:otherwise>
                    <section class="md-empty-state">
                        <h2>No products found</h2>
                        <p>Try clearing filters or searching with a different keyword.</p>
                        <a href="${contextPath}/products" class="md-btn md-btn-primary">VIEW PRODUCTS</a>
                    </section>
                </c:otherwise>
            </c:choose>
        </section>
    </section>
</main>

<script src="${contextPath}/assets/js/common/toast.js"></script>
</body>
</html>