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
    <title>${productDetail.brandName} ${productDetail.productName} - MyntraDemo</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="stylesheet" href="${contextPath}/assets/css/common/base.css">
    <link rel="stylesheet" href="${contextPath}/assets/css/common/toast.css">
    <link rel="stylesheet" href="${contextPath}/assets/css/catalog/product-detail.css">
</head>
<body>

<%@ include file="/WEB-INF/views/common/toast.jsp" %>
<%@ include file="/WEB-INF/views/common/header.jsp" %>

<c:choose>
    <c:when test="${empty productDetail}">
        <main class="pd-page">
            <section class="md-empty-state">
                <h2>Product not available</h2>
                <p>The product you are looking for is not available now.</p>
                <a href="${contextPath}/products" class="md-btn md-btn-primary">VIEW PRODUCTS</a>
            </section>
        </main>
    </c:when>

    <c:otherwise>
        <c:set var="hasMoreColors" value="false" />
        <c:forEach var="variant" items="${productDetail.variants}">
            <c:if test="${variant.hasColor() and variant.displayColor ne 'Default'}">
                <c:set var="hasMoreColors" value="true" />
            </c:if>
        </c:forEach>

        <main class="pd-page">
            <nav class="pd-breadcrumb" aria-label="Breadcrumb">
                <a href="${contextPath}/products">Home</a>
                <span>/</span>
                <a href="${contextPath}/products?categoryId=${productDetail.categoryId}">${productDetail.categoryName}</a>
                <span>/</span>
                <strong>${productDetail.brandName} ${productDetail.productName}</strong>
            </nav>

            <section class="pd-layout">
                <section class="pd-gallery" aria-label="Product images">
                    <c:choose>
                        <c:when test="${productDetail.hasImages()}">
                            <c:forEach var="image" items="${productDetail.images}">
                                <article class="pd-image-card">
                                    <c:choose>
                                        <c:when test="${fn:startsWith(image.imageUrl, 'http://') or fn:startsWith(image.imageUrl, 'https://')}">
                                            <img src="${image.imageUrl}" alt="${image.altText}">
                                        </c:when>
                                        <c:otherwise>
                                            <img src="${contextPath}${image.imageUrl}" alt="${image.altText}">
                                        </c:otherwise>
                                    </c:choose>
                                </article>
                            </c:forEach>
                        </c:when>

                        <c:otherwise>
                            <article class="pd-image-card">
                                <div class="pd-image-placeholder">Image coming soon</div>
                            </article>
                        </c:otherwise>
                    </c:choose>
                </section>

                <aside class="pd-buy-panel">
                    <section class="pd-top-info">
                        <h1>${productDetail.brandName}</h1>
                        <p>${productDetail.productName}</p>

                        <c:if test="${productDetail.hasReviewSummary()}">
                            <a href="#ratings" class="pd-rating-pill">
                                <strong>${productDetail.reviewSummary.averageRating}</strong>
                                <span>★</span>
                                <em>|</em>
                                <small>${productDetail.reviewSummary.totalReviews} Ratings</small>
                            </a>
                        </c:if>
                    </section>

                    <div class="pd-line"></div>

                    <section class="pd-price-block">
                        <div class="pd-price-main">
                            <strong>
                                ${currency}<fmt:formatNumber value="${productDetail.sellingPrice}" maxFractionDigits="0" groupingUsed="false" />
                            </strong>

                            <c:if test="${productDetail.hasDiscount()}">
                                <span>
                                    MRP ${currency}<fmt:formatNumber value="${productDetail.basePrice}" maxFractionDigits="0" groupingUsed="false" />
                                </span>
                                <em>(${productDetail.discountPercent}% OFF)</em>
                            </c:if>
                        </div>

                        <p>inclusive of all taxes</p>
                    </section>

                    <c:if test="${hasMoreColors}">
                        <section class="pd-colors-block">
                            <h2>MORE COLORS</h2>

                            <div class="pd-color-options">
                                <c:forEach var="variant" items="${productDetail.variants}">
                                    <c:if test="${variant.hasColor() and variant.displayColor ne 'Default'}">
                                        <button
                                                type="button"
                                                class="pd-color-choice"
                                                data-variant-id="${variant.variantId}"
                                                title="${variant.displayColor}">
                                            ${variant.displayColor}
                                        </button>
                                    </c:if>
                                </c:forEach>
                            </div>
                        </section>
                    </c:if>

                    <section class="pd-size-block">
                        <div class="pd-section-title-row">
                            <h2>SELECT SIZE</h2>

                            <c:if test="${productDetail.hasSpecifications()}">
                                <a href="#specifications">SIZE CHART ›</a>
                            </c:if>
                        </div>

                        <div class="pd-size-options" id="pdSizeOptions">
                            <c:choose>
                                <c:when test="${productDetail.hasVariants()}">
                                    <c:forEach var="variant" items="${productDetail.variants}">
                                        <button
                                                type="button"
                                                class="pd-size-choice ${variant.available ? '' : 'is-disabled'}"
                                                data-variant-id="${variant.variantId}"
                                                data-stock="${variant.stockQuantity}"
                                                ${variant.available ? '' : 'disabled'}>
                                            ${variant.displaySize}
                                        </button>
                                    </c:forEach>
                                </c:when>

                                <c:otherwise>
                                    <button type="button" class="pd-size-choice is-disabled" disabled>Unavailable</button>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </section>

                    <section class="pd-action-row">
                        <form
                                method="post"
                                action="${contextPath}/cart/add"
                                id="pdAddToCartForm"
                                class="pd-action-form pd-cart-action-form">

                            <input type="hidden" name="productId" value="${productDetail.productId}">
                            <input type="hidden" name="variantId" id="pdSelectedVariantId">
                            <input type="hidden" name="quantity" value="1">

                            <button type="submit" class="pd-add-bag-btn" id="pdAddBagButton" disabled>
                                <span class="pd-bag-icon" aria-hidden="true"></span>
                                <span class="pd-add-bag-label">SELECT SIZE</span>
                            </button>
                        </form>

                        <form
                                method="post"
                                action="${contextPath}/wishlist/add"
                                class="pd-action-form pd-wishlist-action-form">

                            <input type="hidden" name="productId" value="${productDetail.productId}">
                            <input type="hidden" name="redirectTo" value="/product?id=${productDetail.productId}">

                            <button type="submit" class="pd-wishlist-btn">
                                <span class="pd-heart-icon" aria-hidden="true">♡</span>
                                <span>WISHLIST</span>
                            </button>
                        </form>
                    </section>

                    <c:if test="${productDetail.hasDeliveryAddress() or productDetail.hasServicePromises() or productDetail.hasSeller()}">
                        <div class="pd-line"></div>

                        <section class="pd-delivery-block">
                            <h2>DELIVERY OPTIONS</h2>

                            <c:if test="${productDetail.hasDeliveryAddress()}">
                                <div class="pd-address-card">
                                    <span>Deliver to</span>
                                    <strong>${productDetail.deliveryAddress.pincode}</strong>

                                    <c:if test="${not empty productDetail.deliveryAddress.addressLine}">
                                        <p>${productDetail.deliveryAddress.addressLine}</p>
                                    </c:if>
                                </div>
                            </c:if>

                            <c:if test="${productDetail.hasSeller()}">
                                <div class="pd-seller-service">
                                    <c:if test="${not empty productDetail.seller.deliveryMinDays and not empty productDetail.seller.deliveryMaxDays}">
                                        <p>Delivery in ${productDetail.seller.deliveryMinDays} - ${productDetail.seller.deliveryMaxDays} days</p>
                                    </c:if>

                                    <c:if test="${not empty productDetail.seller.returnPolicy}">
                                        <p>${productDetail.seller.returnPolicy}</p>
                                    </c:if>

                                    <c:if test="${not empty productDetail.seller.exchangePolicy}">
                                        <p>${productDetail.seller.exchangePolicy}</p>
                                    </c:if>

                                    <c:if test="${productDetail.seller.codAvailable}">
                                        <p>Cash on delivery available</p>
                                    </c:if>
                                </div>
                            </c:if>

                            <c:if test="${productDetail.hasServicePromises()}">
                                <div class="pd-promises">
                                    <c:forEach var="promise" items="${productDetail.servicePromises}">
                                        <article class="pd-promise-card">
                                            <span></span>
                                            <div>
                                                <strong>${promise.promiseTitle}</strong>

                                                <c:if test="${not empty promise.promiseSubtitle}">
                                                    <p>${promise.promiseSubtitle}</p>
                                                </c:if>
                                            </div>
                                        </article>
                                    </c:forEach>
                                </div>
                            </c:if>
                        </section>
                    </c:if>

                    <c:if test="${productDetail.hasOffers()}">
                        <div class="pd-line"></div>

                        <section class="pd-offers-block">
                            <h2>BEST OFFERS</h2>

                            <div class="pd-offer-list">
                                <c:forEach var="offer" items="${productDetail.offers}">
                                    <article class="pd-offer-card">
                                        <strong>${offer.offerTitle}</strong>

                                        <c:if test="${not empty offer.offerDescription}">
                                            <p>${offer.offerDescription}</p>
                                        </c:if>

                                        <c:if test="${not empty offer.termsText}">
                                            <span>${offer.termsText}</span>
                                        </c:if>
                                    </article>
                                </c:forEach>
                            </div>
                        </section>
                    </c:if>

                    <div class="pd-line"></div>

                    <section class="pd-product-details" id="specifications">
                        <h2>PRODUCT DETAILS</h2>

                        <c:if test="${not empty productDetail.description}">
                            <p>${productDetail.description}</p>
                        </c:if>

                        <div class="pd-product-meta">
                            <p>Product Code: <strong>${productDetail.productId}</strong></p>

                            <c:if test="${productDetail.hasSeller()}">
                                <p>Seller: <strong>${productDetail.seller.sellerName}</strong></p>

                                <c:if test="${not empty productDetail.seller.originalProductText}">
                                    <p>${productDetail.seller.originalProductText}</p>
                                </c:if>
                            </c:if>
                        </div>

                        <c:if test="${productDetail.hasSpecifications()}">
                            <h3>Specifications</h3>

                            <div class="pd-spec-grid">
                                <c:forEach var="spec" items="${productDetail.specifications}">
                                    <div>
                                        <span>${spec.specName}</span>
                                        <strong>${spec.specValue}</strong>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:if>
                    </section>

                    <c:if test="${productDetail.hasReviewSummary()}">
                        <div class="pd-line"></div>

                        <section class="pd-ratings-block" id="ratings">
                            <h2>RATINGS</h2>

                            <div class="pd-rating-summary">
                                <div class="pd-rating-score">
                                    <strong>${productDetail.reviewSummary.averageRating}</strong>
                                    <span>★</span>
                                    <p>${productDetail.reviewSummary.totalReviews} Verified Buyers</p>
                                </div>

                                <div class="pd-rating-bars">
                                    <div>
                                        <span>5 ★</span>
                                        <b><i data-width="${productDetail.reviewSummary.fiveStarPercent}"></i></b>
                                    </div>
                                    <div>
                                        <span>4 ★</span>
                                        <b><i data-width="${productDetail.reviewSummary.fourStarPercent}"></i></b>
                                    </div>
                                    <div>
                                        <span>3 ★</span>
                                        <b><i data-width="${productDetail.reviewSummary.threeStarPercent}"></i></b>
                                    </div>
                                    <div>
                                        <span>2 ★</span>
                                        <b><i data-width="${productDetail.reviewSummary.twoStarPercent}"></i></b>
                                    </div>
                                    <div>
                                        <span>1 ★</span>
                                        <b><i data-width="${productDetail.reviewSummary.oneStarPercent}"></i></b>
                                    </div>
                                </div>
                            </div>
                        </section>
                    </c:if>

                    <c:if test="${productDetail.hasReviews()}">
                        <section class="pd-reviews-block">
                            <h2>Customer Reviews (${productDetail.reviewSummary.totalReviews})</h2>

                            <c:forEach var="review" items="${productDetail.reviews}">
                                <article class="pd-review-card">
                                    <div class="pd-review-rating">${review.rating}★</div>

                                    <div class="pd-review-content">
                                        <c:if test="${not empty review.reviewTitle}">
                                            <h3>${review.reviewTitle}</h3>
                                        </c:if>

                                        <p>${review.reviewText}</p>

                                        <footer>
                                            <span>${review.reviewerName}</span>
                                            <span>|</span>
                                            <span>${review.reviewDate}</span>

                                            <c:if test="${review.verifiedPurchase}">
                                                <strong>Verified Buyer</strong>
                                            </c:if>
                                        </footer>
                                    </div>
                                </article>
                            </c:forEach>
                        </section>
                    </c:if>
                </aside>
            </section>

            <c:if test="${productDetail.hasSimilarProducts()}">
                <section class="pd-similar-section">
                    <h2>SIMILAR PRODUCTS</h2>

                    <div class="pd-similar-grid">
                        <c:forEach var="product" items="${productDetail.similarProducts}">
                            <a href="${contextPath}/product?id=${product.productId}" class="pd-similar-card">
                                <div class="pd-similar-image">
                                    <c:choose>
                                        <c:when test="${product.hasImage() and (fn:startsWith(product.primaryImageUrl, 'http://') or fn:startsWith(product.primaryImageUrl, 'https://'))}">
                                            <img src="${product.primaryImageUrl}" alt="${product.brandName} ${product.productName}">
                                        </c:when>
                                        <c:when test="${product.hasImage()}">
                                            <img src="${contextPath}${product.primaryImageUrl}" alt="${product.brandName} ${product.productName}">
                                        </c:when>
                                        <c:otherwise>
                                            <div class="pd-image-placeholder">Image coming soon</div>
                                        </c:otherwise>
                                    </c:choose>
                                </div>

                                <div class="pd-similar-info">
                                    <h3>${product.brandName}</h3>
                                    <p>${product.productName}</p>

                                    <div class="pd-similar-price">
                                        <strong>
                                            ${currency}<fmt:formatNumber value="${product.sellingPrice}" maxFractionDigits="0" groupingUsed="false" />
                                        </strong>

                                        <c:if test="${product.hasDiscount()}">
                                            <span>
                                                ${currency}<fmt:formatNumber value="${product.basePrice}" maxFractionDigits="0" groupingUsed="false" />
                                            </span>
                                            <em>(${product.discountPercent}% OFF)</em>
                                        </c:if>
                                    </div>
                                </div>
                            </a>
                        </c:forEach>
                    </div>
                </section>
            </c:if>
        </main>

        <script>
            (function () {
                "use strict";

                const selectedVariantInput = document.getElementById("pdSelectedVariantId");
                const addBagButton = document.getElementById("pdAddBagButton");
                const addBagLabel = addBagButton ? addBagButton.querySelector(".pd-add-bag-label") : null;
                const addToCartForm = document.getElementById("pdAddToCartForm");
                const allSizeButtons = Array.from(document.querySelectorAll(".pd-size-choice"));
                const availableSizeButtons = allSizeButtons.filter(function (button) {
                    return !button.disabled && !button.classList.contains("is-disabled") && button.dataset.variantId;
                });

                function setAddBagText(text) {
                    if (addBagLabel) {
                        addBagLabel.textContent = text;
                    }
                }

                function selectSize(button) {
                    if (!button || !selectedVariantInput || !addBagButton) {
                        return;
                    }

                    allSizeButtons.forEach(function (item) {
                        item.classList.remove("is-selected");
                    });

                    button.classList.add("is-selected");
                    selectedVariantInput.value = button.dataset.variantId || "";
                    addBagButton.disabled = !selectedVariantInput.value;
                    setAddBagText(selectedVariantInput.value ? "ADD TO BAG" : "SELECT SIZE");
                }

                availableSizeButtons.forEach(function (button) {
                    button.addEventListener("click", function () {
                        selectSize(button);
                    });
                });

                if (availableSizeButtons.length === 1) {
                    selectSize(availableSizeButtons[0]);
                }

                if (addToCartForm) {
                    addToCartForm.addEventListener("submit", function (event) {
                        if (!selectedVariantInput || !selectedVariantInput.value) {
                            event.preventDefault();
                            setAddBagText("SELECT SIZE");
                            availableSizeButtons.forEach(function (button) {
                                button.classList.add("needs-selection");
                                window.setTimeout(function () {
                                    button.classList.remove("needs-selection");
                                }, 700);
                            });
                        }
                    });
                }

                document.querySelectorAll(".pd-rating-bars i").forEach(function (bar) {
                    const width = Number(bar.dataset.width || 0);
                    bar.style.width = Math.max(0, Math.min(width, 100)) + "%";
                });
            })();
        </script>

        <script src="${contextPath}/assets/js/common/toast.js"></script>
    </c:otherwise>
</c:choose>

</body>
</html>