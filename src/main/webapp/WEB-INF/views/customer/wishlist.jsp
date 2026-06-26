<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="hasWishlistItems" value="${not empty wishlistPage and not empty wishlistPage.items}" />

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Wishlist - MyntraDemo</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="stylesheet" href="${contextPath}/assets/css/common/base.css">
    <link rel="stylesheet" href="${contextPath}/assets/css/common/toast.css">
    <link rel="stylesheet" href="${contextPath}/assets/css/wishlist/wishlist.css">
</head>
<body>

<%@ include file="/WEB-INF/views/common/toast.jsp" %>
<%@ include file="/WEB-INF/views/common/header.jsp" %>

<main class="wishlist-page">
    <section class="wishlist-title-row">
        <h1>My Wishlist</h1>

        <c:choose>
            <c:when test="${not empty wishlistPage}">
                <span>${wishlistPage.totalItems} items</span>
            </c:when>
            <c:otherwise>
                <span>0 items</span>
            </c:otherwise>
        </c:choose>
    </section>

    <c:if test="${not empty errorMessage}">
        <div class="wishlist-alert">${errorMessage}</div>
    </c:if>

    <c:choose>
        <c:when test="${not hasWishlistItems}">
            <section class="wishlist-empty">
                <div class="wishlist-empty-icon">
                    <svg viewBox="0 0 24 24" aria-hidden="true">
                        <path d="M12 20.4C11.7 20.4 11.4 20.3 11.2 20.1C5.7 15.2 3 12.7 3 8.9C3 5.8 5.4 3.4 8.5 3.4C10.2 3.4 11.9 4.2 13 5.5C14.1 4.2 15.8 3.4 17.5 3.4C20.6 3.4 23 5.8 23 8.9C23 12.7 20.3 15.2 14.8 20.1C14.6 20.3 14.3 20.4 14 20.4H12Z"></path>
                    </svg>
                </div>

                <h2>Your wishlist is empty</h2>
                <p>Add items that you like. Review them anytime and easily move them to bag.</p>
                <a href="${contextPath}/products">Continue Shopping</a>
            </section>
        </c:when>

        <c:otherwise>
            <section class="wishlist-grid" aria-label="Wishlist items">
                <c:forEach var="item" items="${wishlistPage.items}">
                    <article class="wishlist-card">
                        <a href="${contextPath}/product?id=${item.productId}" class="wishlist-product-link">
                            <div class="wishlist-image-box">
                                <c:choose>
                                    <c:when test="${item.imageAvailable}">
                                        <c:choose>
                                            <c:when test="${fn:startsWith(item.imageUrl, 'http://') or fn:startsWith(item.imageUrl, 'https://')}">
                                                <img src="${item.imageUrl}" alt="${item.brandName} ${item.productName}">
                                            </c:when>
                                            <c:otherwise>
                                                <img src="${contextPath}${item.imageUrl}" alt="${item.brandName} ${item.productName}">
                                            </c:otherwise>
                                        </c:choose>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="wishlist-image-placeholder">Image coming soon</div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </a>

                        <form method="post" action="${contextPath}/wishlist/remove" class="wishlist-remove-form">
                            <input type="hidden" name="wishlistItemId" value="${item.wishlistItemId}">
                            <button type="submit" aria-label="Remove item">×</button>
                        </form>

                        <div class="wishlist-info">
                            <a href="${contextPath}/product?id=${item.productId}" class="wishlist-name">
                                ${item.brandName} ${item.productName}
                            </a>

                            <div class="wishlist-price-row">
                                <strong>Rs.${item.sellingPrice}</strong>

                                <c:if test="${item.discountAvailable}">
                                    <span>Rs.${item.mrpPrice}</span>
                                    <em>(${item.discountPercent}% OFF)</em>
                                </c:if>
                            </div>
                        </div>

                        <form method="post" action="${contextPath}/wishlist/move-to-cart" class="wishlist-move-form">
                            <input type="hidden" name="wishlistItemId" value="${item.wishlistItemId}">
                            <button type="submit" <c:if test="${not item.inStock}">disabled</c:if>>
                                MOVE TO BAG
                            </button>
                        </form>
                    </article>
                </c:forEach>
            </section>
        </c:otherwise>
    </c:choose>
</main>

<script src="${contextPath}/assets/js/common/toast.js"></script>
</body>
</html>