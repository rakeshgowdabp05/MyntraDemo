<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="currency" value="${empty currencySymbol ? '₹' : currencySymbol}" />

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Online Shopping for Fashion & Lifestyle - MyntraDemo</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="stylesheet" href="${contextPath}/assets/css/common/base.css">
    <link rel="stylesheet" href="${contextPath}/assets/css/common/toast.css">
    <link rel="stylesheet" href="${contextPath}/assets/css/home/home.css">
</head>
<body>

<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/toast.jsp" %>

<main class="home-page">
    <c:choose>
        <c:when test="${not empty homePage}">

            <c:if test="${homePage.heroProductsAvailable}">
                <section class="home-hero" data-home-hero>
                    <button type="button" class="hero-arrow left" data-hero-prev aria-label="Previous banner">‹</button>

                    <div class="hero-window">
                        <div class="hero-track" data-hero-track>
                            <c:forEach var="product" items="${homePage.heroProducts}" varStatus="status">
                                <article class="hero-slide">
                                    <a href="${contextPath}/product?id=${product.productId}" class="hero-card">
                                        <div class="hero-copy">
                                            <span>${product.displayCategoryName}</span>
                                            <h1>${product.displayBrandName}</h1>
                                            <p>${product.displayProductName}</p>

                                            <c:choose>
                                                <c:when test="${product.discountAvailable}">
                                                    <strong>Up To ${product.discountPercent}% Off</strong>
                                                </c:when>
                                                <c:otherwise>
                                                    <strong>New Season Styles</strong>
                                                </c:otherwise>
                                            </c:choose>

                                            <em>SHOP NOW</em>
                                        </div>

                                        <div class="hero-image">
                                            <c:choose>
                                                <c:when test="${product.imageAvailable}">
                                                    <c:choose>
                                                        <c:when test="${fn:startsWith(product.imageUrl, 'http://') or fn:startsWith(product.imageUrl, 'https://')}">
                                                            <img src="${product.imageUrl}" alt="${product.displayBrandName} ${product.displayProductName}">
                                                        </c:when>
                                                        <c:otherwise>
                                                            <img src="${contextPath}${product.imageUrl}" alt="${product.displayBrandName} ${product.displayProductName}">
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:when>
                                                <c:otherwise>
                                                    <span>${product.displayBrandName}</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </a>
                                </article>
                            </c:forEach>
                        </div>
                    </div>

                    <button type="button" class="hero-arrow right" data-hero-next aria-label="Next banner">›</button>

                    <div class="hero-dots">
                        <c:forEach var="product" items="${homePage.heroProducts}" varStatus="status">
                            <button type="button" class="${status.first ? 'is-active' : ''}" data-hero-dot="${status.index}"></button>
                        </c:forEach>
                    </div>
                </section>
            </c:if>

            <c:if test="${homePage.categoriesAvailable}">
                <section class="home-section">
                    <div class="home-section-title">
                        <h2>Shop By Category</h2>
                    </div>

                    <div class="category-grid">
                        <c:forEach var="category" items="${homePage.categories}">
                            <a href="${contextPath}/products?categoryId=${category.categoryId}" class="category-card">
                                <div class="category-image">
                                    <c:choose>
                                        <c:when test="${category.imageAvailable}">
                                            <c:choose>
                                                <c:when test="${fn:startsWith(category.imageUrl, 'http://') or fn:startsWith(category.imageUrl, 'https://')}">
                                                    <img src="${category.imageUrl}" alt="${category.displayCategoryName}">
                                                </c:when>
                                                <c:otherwise>
                                                    <img src="${contextPath}${category.imageUrl}" alt="${category.displayCategoryName}">
                                                </c:otherwise>
                                            </c:choose>
                                        </c:when>
                                        <c:otherwise>
                                            <span>${category.displayCategoryName}</span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>

                                <strong>${category.displayCategoryName}</strong>
                                <span>${category.productCountLabel}</span>
                            </a>
                        </c:forEach>
                    </div>
                </section>
            </c:if>

            <c:if test="${homePage.dealProductsAvailable}">
                <section class="home-section deal-section">
                    <div class="home-section-title">
                        <h2>Biggest Deals On Top Styles</h2>
                    </div>

                    <div class="deal-grid">
                        <c:forEach var="product" items="${homePage.dealProducts}">
                            <a href="${contextPath}/product?id=${product.productId}" class="deal-card">
                                <div class="deal-image">
                                    <c:choose>
                                        <c:when test="${product.imageAvailable}">
                                            <c:choose>
                                                <c:when test="${fn:startsWith(product.imageUrl, 'http://') or fn:startsWith(product.imageUrl, 'https://')}">
                                                    <img src="${product.imageUrl}" alt="${product.displayBrandName} ${product.displayProductName}">
                                                </c:when>
                                                <c:otherwise>
                                                    <img src="${contextPath}${product.imageUrl}" alt="${product.displayBrandName} ${product.displayProductName}">
                                                </c:otherwise>
                                            </c:choose>
                                        </c:when>
                                        <c:otherwise>
                                            <span>${product.displayBrandName}</span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>

                                <div class="deal-copy">
                                    <strong>${product.displayBrandName}</strong>
                                    <p>${product.displayProductName}</p>

                                    <div class="deal-price">
                                        <b>${currency}<fmt:formatNumber value="${product.sellingPrice}" maxFractionDigits="0" groupingUsed="false" /></b>

                                        <c:if test="${product.discountAvailable}">
                                            <span>${currency}<fmt:formatNumber value="${product.basePrice}" maxFractionDigits="0" groupingUsed="false" /></span>
                                            <em>${product.discountPercent}% OFF</em>
                                        </c:if>
                                    </div>
                                </div>
                            </a>
                        </c:forEach>
                    </div>
                </section>
            </c:if>

            <c:if test="${homePage.brandsAvailable}">
                <section class="home-section">
                    <div class="home-section-title">
                        <h2>Explore Top Brands</h2>
                    </div>

                    <div class="brand-row">
                        <c:forEach var="brand" items="${homePage.brands}">
                            <a href="${contextPath}/products?brandId=${brand.brandId}" class="brand-card">
                                <div>
                                    <c:choose>
                                        <c:when test="${brand.imageAvailable}">
                                            <c:choose>
                                                <c:when test="${fn:startsWith(brand.imageUrl, 'http://') or fn:startsWith(brand.imageUrl, 'https://')}">
                                                    <img src="${brand.imageUrl}" alt="${brand.displayBrandName}">
                                                </c:when>
                                                <c:otherwise>
                                                    <img src="${contextPath}${brand.imageUrl}" alt="${brand.displayBrandName}">
                                                </c:otherwise>
                                            </c:choose>
                                        </c:when>
                                        <c:otherwise>
                                            <span>${brand.displayBrandName}</span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>

                                <strong>${brand.displayBrandName}</strong>
                                <em>${brand.productCountLabel}</em>
                            </a>
                        </c:forEach>
                    </div>
                </section>
            </c:if>

            <c:if test="${homePage.newArrivalsAvailable}">
                <section class="home-section">
                    <div class="home-section-title">
                        <h2>New Arrivals</h2>
                    </div>

                    <div class="product-strip">
                        <c:forEach var="product" items="${homePage.newArrivals}">
                            <a href="${contextPath}/product?id=${product.productId}" class="home-product-card">
                                <div class="home-product-image">
                                    <c:choose>
                                        <c:when test="${product.imageAvailable}">
                                            <c:choose>
                                                <c:when test="${fn:startsWith(product.imageUrl, 'http://') or fn:startsWith(product.imageUrl, 'https://')}">
                                                    <img src="${product.imageUrl}" alt="${product.displayBrandName} ${product.displayProductName}">
                                                </c:when>
                                                <c:otherwise>
                                                    <img src="${contextPath}${product.imageUrl}" alt="${product.displayBrandName} ${product.displayProductName}">
                                                </c:otherwise>
                                            </c:choose>
                                        </c:when>
                                        <c:otherwise>
                                            <span>${product.displayBrandName}</span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>

                                <strong>${product.displayBrandName}</strong>
                                <p>${product.displayProductName}</p>

                                <div>
                                    <b>${currency}<fmt:formatNumber value="${product.sellingPrice}" maxFractionDigits="0" groupingUsed="false" /></b>

                                    <c:if test="${product.discountAvailable}">
                                        <span>${currency}<fmt:formatNumber value="${product.basePrice}" maxFractionDigits="0" groupingUsed="false" /></span>
                                    </c:if>
                                </div>
                            </a>
                        </c:forEach>
                    </div>
                </section>
            </c:if>

            <c:if test="${homePage.trendingProductsAvailable}">
                <section class="home-section">
                    <div class="home-section-title">
                        <h2>Trending Now</h2>
                    </div>

                    <div class="product-strip">
                        <c:forEach var="product" items="${homePage.trendingProducts}">
                            <a href="${contextPath}/product?id=${product.productId}" class="home-product-card">
                                <div class="home-product-image">
                                    <c:choose>
                                        <c:when test="${product.imageAvailable}">
                                            <c:choose>
                                                <c:when test="${fn:startsWith(product.imageUrl, 'http://') or fn:startsWith(product.imageUrl, 'https://')}">
                                                    <img src="${product.imageUrl}" alt="${product.displayBrandName} ${product.displayProductName}">
                                                </c:when>
                                                <c:otherwise>
                                                    <img src="${contextPath}${product.imageUrl}" alt="${product.displayBrandName} ${product.displayProductName}">
                                                </c:otherwise>
                                            </c:choose>
                                        </c:when>
                                        <c:otherwise>
                                            <span>${product.displayBrandName}</span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>

                                <strong>${product.displayBrandName}</strong>
                                <p>${product.displayProductName}</p>

                                <div>
                                    <b>${currency}<fmt:formatNumber value="${product.sellingPrice}" maxFractionDigits="0" groupingUsed="false" /></b>

                                    <c:if test="${product.discountAvailable}">
                                        <span>${currency}<fmt:formatNumber value="${product.basePrice}" maxFractionDigits="0" groupingUsed="false" /></span>
                                    </c:if>
                                </div>
                            </a>
                        </c:forEach>
                    </div>
                </section>
            </c:if>

        </c:when>

        <c:otherwise>
            <section class="home-empty">
                <h1>No active products found</h1>
                <p>Add active products with images to show the homepage.</p>
                <a href="${contextPath}/products">GO TO PRODUCTS</a>
            </section>
        </c:otherwise>
    </c:choose>
</main>

<script src="${contextPath}/assets/js/common/toast.js"></script>
<script src="${contextPath}/assets/js/home/home.js"></script>
</body>
</html>