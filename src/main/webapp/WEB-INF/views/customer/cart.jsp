<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="currency" value="${empty currencySymbol ? '₹' : currencySymbol}" />
<c:set var="cartAddress" value="${cartPage.defaultAddress}" />
<c:set var="availableCoupons" value="${cartPage.availableCoupons}" />
<c:set var="donationOptions" value="${requestScope.donationOptions}" />
<c:set var="giftPackageFee" value="${requestScope.giftPackageFee}" />

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Bag - MyntraDemo</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="stylesheet" href="${contextPath}/assets/css/common/base.css">
    <link rel="stylesheet" href="${contextPath}/assets/css/common/toast.css">
    <link rel="stylesheet" href="${contextPath}/assets/css/cart/cart.css">
</head>
<body>

<%@ include file="/WEB-INF/views/common/toast.jsp" %>

<header class="checkout-header">
    <a href="${contextPath}/products" class="checkout-logo">MD</a>

    <nav class="checkout-steps" aria-label="Checkout steps">
        <span class="is-active">BAG</span>
        <i></i>
        <span>ADDRESS</span>
        <i></i>
        <span>PAYMENT</span>
    </nav>

    <div class="checkout-secure">
        <span></span>
        <strong>100% SECURE</strong>
    </div>
</header>

<main class="cart-page">
    <c:choose>
        <c:when test="${not empty cartPage and cartPage.totalItems gt 0}">

            <c:set var="outOfStockCount" value="0" />
            <c:forEach var="item" items="${cartPage.items}">
                <c:if test="${item.stockQuantity le 0}">
                    <c:set var="outOfStockCount" value="${outOfStockCount + 1}" />
                </c:if>
            </c:forEach>

            <section class="cart-shell">
                <section class="cart-left">
                    <section class="cart-address-bar">
                        <c:choose>
                            <c:when test="${not empty cartAddress}">
                                <div>
                                    <span>Deliver to:</span>
                                    <strong>${cartAddress.fullName}, ${cartAddress.pincode}</strong>
                                    <p>${cartAddress.displayAddress}</p>
                                </div>

                                <a href="${contextPath}/address">CHANGE ADDRESS</a>
                            </c:when>

                            <c:otherwise>
                                <div>
                                    <span>Delivery address</span>
                                    <strong>Add address to continue checkout</strong>
                                </div>

                                <a href="${contextPath}/address">ADD ADDRESS</a>
                            </c:otherwise>
                        </c:choose>
                    </section>

                    <c:if test="${not empty availableCoupons}">
                        <section class="cart-offers-box" id="cartCoupons">
                            <div class="cart-offers-title">
                                <span class="cart-offer-wheel-icon" aria-hidden="true">
                                    <svg viewBox="0 0 24 24">
                                        <path d="M12 2 14 5.2 17.7 4.5 17.3 8.2 20.5 10.2 17.6 12.7 19.1 16.2 15.4 16.9 14 20.5 10.8 18.5 7.7 20.5 6.6 16.9 2.9 16.2 4.4 12.7 1.5 10.2 4.7 8.2 4.3 4.5 8 5.2 12 2Zm0 5.2a4.8 4.8 0 1 0 0 9.6 4.8 4.8 0 0 0 0-9.6Zm0 1.8a3 3 0 1 1 0 6 3 3 0 0 1 0-6Z"/>
                                    </svg>
                                </span>
                                <strong>Offers (${fn:length(availableCoupons)})</strong>
                            </div>

                            <div class="cart-myntra-offer-carousel" data-offer-carousel>
                                <button type="button" class="cart-myntra-offer-arrow is-left" data-offer-prev aria-label="Previous offer">
                                    <span></span>
                                </button>

                                <div class="cart-myntra-offer-window">
                                    <div class="cart-myntra-offer-track" data-offer-track>
                                        <c:forEach var="coupon" items="${availableCoupons}">
                                            <article class="cart-myntra-offer-slide">
                                                <div class="cart-offer-brand-mark">
                                                    <span>${fn:substring(coupon.couponCode, 0, 1)}</span>
                                                </div>

                                                <div class="cart-offer-copy">
                                                    <h3>${coupon.couponTitle}</h3>
                                                    <p>${coupon.couponDescription}</p>
                                                </div>
                                            </article>
                                        </c:forEach>
                                    </div>
                                </div>

                                <button type="button" class="cart-myntra-offer-arrow is-right" data-offer-next aria-label="Next offer">
                                    <span></span>
                                </button>
                            </div>

                            <div class="cart-myntra-offer-dots" data-offer-dots aria-hidden="true">
                                <c:forEach var="coupon" items="${availableCoupons}" varStatus="status">
                                    <button type="button" class="${status.first ? 'is-active' : ''}" data-offer-dot="${status.index}"></button>
                                </c:forEach>
                            </div>
                        </section>
                    </c:if>

                    <c:if test="${outOfStockCount gt 0}">
                        <section class="cart-stock-alert">
                            <span class="cart-stock-icon" aria-hidden="true">
                                <svg viewBox="0 0 24 24">
                                    <path d="M7 7V6a5 5 0 0 1 10 0v1h2.2l1 14H3.8l1-14H7Zm2 0h6V6a3 3 0 0 0-6 0v1Zm3 4a1 1 0 0 0-1 1v3a1 1 0 1 0 2 0v-3a1 1 0 0 0-1-1Zm0 7a1.1 1.1 0 1 0 0-2.2 1.1 1.1 0 0 0 0 2.2Z"/>
                                </svg>
                            </span>

                            <strong>
                                ${outOfStockCount} item<c:if test="${outOfStockCount gt 1}">s</c:if> out of stock.
                            </strong>
                            <a href="#cartItems">VIEW</a>
                        </section>
                    </c:if>

                    <section class="cart-selected-row">
                        <div class="cart-selected-left">
                            <span class="cart-check-icon" aria-hidden="true"></span>
                            <strong>${cartPage.totalItems} ITEM(S) SELECTED</strong>
                        </div>

                        <div class="cart-bulk-actions">
                            <a href="${contextPath}/cart">REMOVE</a>
                            <a href="${contextPath}/wishlist">MOVE TO WISHLIST</a>
                        </div>
                    </section>

                    <section class="cart-items-list" id="cartItems" aria-label="Bag items">
                        <c:forEach var="item" items="${cartPage.items}">
                            <article class="cart-item-card ${item.stockQuantity le 0 ? 'is-out-of-stock' : ''}">
                                <span class="cart-item-check" aria-hidden="true"></span>

                                <a href="${contextPath}/product?id=${item.productId}" class="cart-item-image">
                                    <c:choose>
                                        <c:when test="${item.hasImage()}">
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
                                            <div class="cart-image-placeholder">Image</div>
                                        </c:otherwise>
                                    </c:choose>
                                </a>

                                <section class="cart-item-info">
                                    <form method="post" action="${contextPath}/cart/remove" class="cart-remove-form">
                                        <input type="hidden" name="cartItemId" value="${item.cartItemId}">
                                        <button type="submit" aria-label="Remove ${item.productName} from bag">Remove</button>
                                    </form>

                                    <a href="${contextPath}/product?id=${item.productId}" class="cart-product-title">
                                        <h2>${item.brandName}</h2>
                                        <p>${item.productName}</p>
                                    </a>

                                    <div class="cart-variant-row">
                                        <span>Size: ${item.displaySize}</span>
                                    </div>

                                    <div class="cart-qty-row">
                                        <form method="post" action="${contextPath}/cart/update" class="cart-qty-form">
                                            <input type="hidden" name="cartItemId" value="${item.cartItemId}">

                                            <label>
                                                Qty:
                                                <select name="quantity" onchange="this.form.submit()" ${item.stockQuantity le 0 ? 'disabled' : ''}>
                                                    <c:choose>
                                                        <c:when test="${item.stockQuantity gt 0}">
                                                            <c:forEach begin="1" end="${item.stockQuantity}" var="qty">
                                                                <option value="${qty}" <c:if test="${qty eq item.quantity}">selected</c:if>>
                                                                    ${qty}
                                                                </option>
                                                            </c:forEach>
                                                        </c:when>

                                                        <c:otherwise>
                                                            <option value="${item.quantity}" selected>${item.quantity}</option>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </select>
                                            </label>
                                        </form>

                                        <c:choose>
                                            <c:when test="${item.stockQuantity le 0}">
                                                <span class="cart-out-stock-pill">Out of stock</span>
                                            </c:when>
                                            <c:when test="${item.stockQuantity le 5}">
                                                <span class="cart-low-stock">${item.stockQuantity} left</span>
                                            </c:when>
                                        </c:choose>
                                    </div>

                                    <div class="cart-price-row">
                                        <strong>
                                            ${currency}<fmt:formatNumber value="${item.priceAtAdded}" maxFractionDigits="0" groupingUsed="false" />
                                        </strong>

                                        <c:if test="${item.hasDiscount()}">
                                            <span>
                                                ${currency}<fmt:formatNumber value="${item.mrpPrice}" maxFractionDigits="0" groupingUsed="false" />
                                            </span>

                                            <em>
                                                ${currency}<fmt:formatNumber value="${item.itemDiscountTotal}" maxFractionDigits="0" groupingUsed="false" />
                                                OFF
                                            </em>
                                        </c:if>
                                    </div>

                                    <div class="cart-item-actions">
                                        <form method="post" action="${contextPath}/cart/move-to-wishlist">
                                            <input type="hidden" name="cartItemId" value="${item.cartItemId}">
                                            <button type="submit">MOVE TO WISHLIST</button>
                                        </form>
                                    </div>
                                </section>
                            </article>
                        </c:forEach>
                    </section>
                </section>

                <aside class="cart-right" aria-label="Price details">
                    <section class="cart-side-section">
                        <h2>OFFERS &amp; COUPONS</h2>

                        <button type="button" class="cart-offer-summary-card" data-cart-modal-open="offers">
                            <div>
                                <span class="cart-real-icon cart-summary-coupon-icon" aria-hidden="true">
                                    <svg viewBox="0 0 24 24">
                                        <path d="M20.5 10.3a2 2 0 0 1 0 3.4V18a2 2 0 0 1-2 2h-13a2 2 0 0 1-2-2v-4.3a2 2 0 0 0 0-3.4V6a2 2 0 0 1 2-2h13a2 2 0 0 1 2 2v4.3ZM5.5 6v2.9a4 4 0 0 1 0 6.2V18h13v-2.9a4 4 0 0 1 0-6.2V6h-13Z"/>
                                    </svg>
                                </span>

                                <strong>
                                    <c:choose>
                                        <c:when test="${not empty availableCoupons}">
                                            ${fn:length(availableCoupons)} Offers On Your Bag
                                        </c:when>
                                        <c:otherwise>
                                            No Offers On Your Bag
                                        </c:otherwise>
                                    </c:choose>
                                </strong>
                            </div>

                            <span class="cart-right-chevron" aria-hidden="true"></span>
                        </button>

                        <div class="cart-apply-coupon-row">
                            <div class="cart-apply-coupon-label">
                                <span class="cart-real-icon cart-tag-real-icon" aria-hidden="true">
                                    <svg viewBox="0 0 24 24">
                                        <path d="M10.5 3H5a2 2 0 0 0-2 2v5.5l9.2 9.2a2.4 2.4 0 0 0 3.4 0l4.1-4.1a2.4 2.4 0 0 0 0-3.4L10.5 3Zm-3 5.5a1.5 1.5 0 1 1 0-3 1.5 1.5 0 0 1 0 3Z"/>
                                    </svg>
                                </span>
                                <strong>Apply Coupons</strong>
                            </div>

                            <button type="button" class="cart-outline-action-btn" data-cart-modal-open="coupon">
                                <c:choose>
                                    <c:when test="${cartPage.appliedCouponPresent}">CHANGE</c:when>
                                    <c:otherwise>APPLY</c:otherwise>
                                </c:choose>
                            </button>
                        </div>

                        <c:if test="${cartPage.appliedCouponPresent}">
                            <div class="cart-applied-coupon">
                                <div>
                                    <span>Applied Coupon</span>
                                    <strong>${cartPage.appliedCoupon.couponCode}</strong>
                                </div>

                                <form method="post" action="${contextPath}/cart/coupon/remove">
                                    <button type="submit">REMOVE</button>
                                </form>
                            </div>
                        </c:if>
                    </section>

                    <section class="cart-side-section cart-gift-section">
                        <h2>GIFTING &amp; PERSONALISATION</h2>

                        <div class="cart-gift-card ${cartPage.giftPackageEnabled ? 'is-active' : ''}">
                            <div class="cart-gift-ribbon" aria-hidden="true"></div>

                            <div class="cart-gift-content">
                                <h3>Buying for a loved one?</h3>

                                <c:choose>
                                    <c:when test="${cartPage.giftPackageEnabled}">
                                        <p>
                                            Gift packaging added for
                                            ${currency}<fmt:formatNumber value="${cartPage.giftPackageFee}" maxFractionDigits="0" groupingUsed="false" />.
                                        </p>

                                        <form method="post" action="${contextPath}/cart/gift/toggle">
                                            <input type="hidden" name="enabled" value="false">
                                            <button type="submit">REMOVE GIFT PACKAGE</button>
                                        </form>
                                    </c:when>

                                    <c:otherwise>
                                        <p>
                                            Add gift packaging and a personalised message for
                                            ${currency}<fmt:formatNumber value="${giftPackageFee}" maxFractionDigits="0" groupingUsed="false" />.
                                        </p>

                                        <button type="button" data-cart-modal-open="gift">ADD GIFT PACKAGE</button>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </section>

                    <section class="cart-side-section cart-donation-section">
                        <h2>SUPPORT TRANSFORMATIVE SOCIAL WORK IN INDIA</h2>

                        <label class="cart-donate-check">
                            <input type="checkbox" data-donation-checkbox <c:if test="${cartPage.donationAvailable}">checked</c:if>>
                            <span>Donate and make a difference</span>
                        </label>

                        <div class="cart-donation-chips">
                            <c:forEach var="amount" items="${donationOptions}">
                                <form method="post" action="${contextPath}/cart/donation/update">
                                    <input type="hidden" name="amount" value="${amount}">
                                    <button type="submit" class="${cartPage.donationAmount eq amount ? 'is-selected' : ''}">
                                        ${currency}<fmt:formatNumber value="${amount}" maxFractionDigits="0" groupingUsed="false" />
                                    </button>
                                </form>
                            </c:forEach>
                        </div>

                        <c:if test="${cartPage.donationAvailable}">
                            <form method="post" action="${contextPath}/cart/donation/remove" class="cart-donation-remove-form" data-donation-remove-form>
                                <button type="submit">REMOVE DONATION</button>
                            </form>
                        </c:if>

                        <a href="${contextPath}/cart" class="cart-know-more">Know More</a>
                    </section>

                    <section class="cart-price-card">
    <h2>PRICE DETAILS (${cartPage.totalItems} Item<c:if test="${cartPage.totalItems gt 1}">s</c:if>)</h2>

    <div class="cart-price-line">
        <span>Total MRP</span>
        <strong>
            ${currency}<fmt:formatNumber value="${cartPage.totalMrp}" maxFractionDigits="0" groupingUsed="false" />
        </strong>
    </div>

    <c:if test="${cartPage.discountAvailable}">
        <div class="cart-price-line">
            <span>Discount on MRP</span>
            <strong class="cart-green">
                - ${currency}<fmt:formatNumber value="${cartPage.totalDiscount}" maxFractionDigits="0" groupingUsed="false" />
            </strong>
        </div>
    </c:if>

    <div class="cart-price-line">
        <span>Coupon Discount</span>
        <c:choose>
            <c:when test="${cartPage.couponDiscountAvailable}">
                <strong class="cart-green">
                    - ${currency}<fmt:formatNumber value="${cartPage.couponDiscount}" maxFractionDigits="0" groupingUsed="false" />
                </strong>
            </c:when>
            <c:otherwise>
                <button type="button" class="cart-pink cart-price-coupon-btn" data-cart-modal-open="coupon">
                    Apply Coupon
                </button>
            </c:otherwise>
        </c:choose>
    </div>

    <c:if test="${cartPage.giftPackageFeeAvailable}">
        <div class="cart-price-line">
            <span>Gift Packaging Fee</span>
            <strong>
                ${currency}<fmt:formatNumber value="${cartPage.giftPackageFee}" maxFractionDigits="0" groupingUsed="false" />
            </strong>
        </div>
    </c:if>

    <c:if test="${cartPage.donationAvailable}">
        <div class="cart-price-line">
            <span>Donation</span>
            <strong>
                ${currency}<fmt:formatNumber value="${cartPage.donationAmount}" maxFractionDigits="0" groupingUsed="false" />
            </strong>
        </div>
    </c:if>

    <div class="cart-total-line">
        <span>Total Amount</span>
        <strong>
            ${currency}<fmt:formatNumber value="${cartPage.payableAmount}" maxFractionDigits="0" groupingUsed="false" />
        </strong>
    </div>

    <p class="cart-terms-text">
        By placing the order, you agree to MyntraDemo's
        <a href="${contextPath}/terms">Terms of Use</a>
        and
        <a href="${contextPath}/privacy">Privacy Policy</a>
    </p>

    <a href="${contextPath}/checkout/address" class="cart-place-order-btn">
        PLACE ORDER
    </a>
</section>
                </aside>
            </section>

            <c:if test="${cartPage.recommendedProductsAvailable}">
                <section class="cart-recommend-section">
                    <div class="cart-recommend-header">
                        <h2>You may also like</h2>
                    </div>

                    <div class="cart-recommend-grid">
                        <c:forEach var="product" items="${cartPage.recommendedProducts}">
                            <article class="cart-recommend-card">
                                <a href="${contextPath}/product?id=${product.productId}" class="cart-recommend-image">
                                    <c:choose>
                                        <c:when test="${product.imageAvailable}">
                                            <c:choose>
                                                <c:when test="${fn:startsWith(product.imageUrl, 'http://') or fn:startsWith(product.imageUrl, 'https://')}">
                                                    <img src="${product.imageUrl}" alt="${product.brandName} ${product.productName}">
                                                </c:when>
                                                <c:otherwise>
                                                    <img src="${contextPath}${product.imageUrl}" alt="${product.brandName} ${product.productName}">
                                                </c:otherwise>
                                            </c:choose>
                                        </c:when>

                                        <c:otherwise>
                                            <div class="cart-recommend-placeholder">Image</div>
                                        </c:otherwise>
                                    </c:choose>
                                </a>

                                <div class="cart-recommend-info">
                                    <h3>${product.brandName}</h3>
                                    <p>${product.productName}</p>

                                    <div class="cart-recommend-price">
                                        <strong>
                                            ${currency}<fmt:formatNumber value="${product.sellingPrice}" maxFractionDigits="0" groupingUsed="false" />
                                        </strong>

                                        <c:if test="${product.discounted}">
                                            <span>
                                                ${currency}<fmt:formatNumber value="${product.mrpPrice}" maxFractionDigits="0" groupingUsed="false" />
                                            </span>
                                            <em>(${product.discountPercent}% OFF)</em>
                                        </c:if>
                                    </div>

                                    <a href="${contextPath}/product?id=${product.productId}" class="cart-recommend-action">
                                        VIEW PRODUCT
                                    </a>
                                </div>
                            </article>
                        </c:forEach>
                    </div>
                </section>
            </c:if>

            <section class="cart-modal cart-gift-modal" data-cart-modal="gift" aria-hidden="true">
                <div class="cart-modal-backdrop" data-cart-modal-backdrop></div>

                <div class="cart-modal-panel cart-gift-modal-panel" role="dialog" aria-modal="true" aria-labelledby="giftModalTitle">
                    <button type="button" class="cart-modal-close" data-cart-modal-close aria-label="Close gift modal"></button>

                    <form method="post" action="${contextPath}/cart/gift/toggle" class="gift-package-form">
                        <input type="hidden" name="enabled" value="true">

                        <section class="gift-modal-left">
                            <span>Gift Packaging</span>
                            <h2 id="giftModalTitle">Make It Special</h2>

                            <input type="text" name="recipientName" placeholder="Recipient Name" maxlength="120" data-modal-focus>

                            <div class="gift-message-field">
                                <textarea name="giftMessage" placeholder="Message" maxlength="200" data-gift-message></textarea>
                                <span data-gift-message-count>0/200</span>
                            </div>

                            <input type="text" name="senderName" placeholder="Sender Name" maxlength="120">

                            <p class="gift-note">
                                <strong>Please Note:</strong> Gift packaging is not available for Pay on Delivery orders.
                            </p>

                            <button type="submit" class="gift-apply-btn">APPLY GIFT PACKAGING</button>
                        </section>

                        <section class="gift-modal-right">
                            <div class="gift-visual">
                                <div class="gift-box"></div>
                                <div class="gift-wrap"></div>
                            </div>

                            <div class="gift-how-title">
                                <strong>HOW DOES IT WORK?</strong>
                                <span></span>
                            </div>

                            <div class="gift-how-list">
                                <article>
                                    <div class="gift-how-icon">✍</div>
                                    <div>
                                        <h3>Personalised card</h3>
                                        <p>Your message will be printed on a card placed inside the package.</p>
                                    </div>
                                </article>

                                <article>
                                    <div class="gift-how-icon">%</div>
                                    <div>
                                        <h3>Invoice will be included</h3>
                                        <p>The receiver will get an invoice showing the amount you pay or discount applied.</p>
                                    </div>
                                </article>

                                <article>
                                    <div class="gift-how-icon">🏷</div>
                                    <div>
                                        <h3>Original product tags will be retained</h3>
                                        <p>Original tags with MRP will be left intact for size exchange.</p>
                                    </div>
                                </article>
                            </div>
                        </section>
                    </form>
                </div>
            </section>

            <section class="cart-modal cart-coupon-modal" data-cart-modal="coupon" aria-hidden="true">
                <div class="cart-modal-backdrop" data-cart-modal-backdrop></div>

                <div class="cart-modal-panel cart-coupon-modal-panel" role="dialog" aria-modal="true" aria-labelledby="couponModalTitle">
                    <header class="cart-modal-header">
                        <h2 id="couponModalTitle">APPLY COUPON</h2>
                        <button type="button" class="cart-modal-close" data-cart-modal-close aria-label="Close coupon modal"></button>
                    </header>

                    <section class="coupon-check-row">
                        <input type="text" placeholder="Enter coupon code" data-coupon-search data-modal-focus>
                        <button type="button">CHECK</button>
                    </section>

                    <section class="coupon-modal-list">
                        <c:choose>
                            <c:when test="${not empty availableCoupons}">
                                <c:forEach var="coupon" items="${availableCoupons}">
                                    <article
                                        class="coupon-modal-card"
                                        data-coupon-card
                                        data-coupon-code="${coupon.couponCode}"
                                        data-coupon-title="${coupon.couponTitle}"
                                        data-coupon-description="${coupon.couponDescription}"
                                    >
                                        <div class="coupon-modal-code">
                                            <span>${coupon.couponCode}</span>
                                        </div>

                                        <div class="coupon-modal-copy">
                                            <h3>${coupon.couponTitle}</h3>
                                            <p>${coupon.couponDescription}</p>

                                            <small>
                                                Min bag value:
                                                ${currency}<fmt:formatNumber value="${coupon.minimumOrderAmount}" maxFractionDigits="0" groupingUsed="false" />
                                                <c:if test="${not empty coupon.maxDiscountAmount}">
                                                    · Max discount:
                                                    ${currency}<fmt:formatNumber value="${coupon.maxDiscountAmount}" maxFractionDigits="0" groupingUsed="false" />
                                                </c:if>
                                            </small>
                                        </div>

                                        <div class="coupon-modal-action">
                                            <c:choose>
                                                <c:when test="${cartPage.appliedCouponPresent and cartPage.appliedCoupon.couponId eq coupon.couponId}">
                                                    <form method="post" action="${contextPath}/cart/coupon/remove">
                                                        <button type="submit">REMOVE</button>
                                                    </form>
                                                </c:when>

                                                <c:otherwise>
                                                    <form method="post" action="${contextPath}/cart/coupon/apply">
                                                        <input type="hidden" name="couponId" value="${coupon.couponId}">
                                                        <button type="submit">APPLY</button>
                                                    </form>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </article>
                                </c:forEach>
                            </c:when>

                            <c:otherwise>
                                <p class="coupon-empty-text">No other coupons available</p>
                            </c:otherwise>
                        </c:choose>
                    </section>

                    <footer class="coupon-modal-footer">
                        <div>
                            <span>Maximum savings:</span>
                            <strong>
                                <c:choose>
                                    <c:when test="${cartPage.couponDiscountAvailable}">
                                        ${currency}<fmt:formatNumber value="${cartPage.couponDiscount}" maxFractionDigits="0" groupingUsed="false" />
                                    </c:when>
                                    <c:otherwise>${currency}0</c:otherwise>
                                </c:choose>
                            </strong>
                        </div>

                        <button type="button" data-cart-modal-close>APPLY</button>
                    </footer>
                </div>
            </section>

            <section class="cart-modal cart-offers-modal" data-cart-modal="offers" aria-hidden="true">
                <div class="cart-modal-backdrop" data-cart-modal-backdrop></div>

                <div class="cart-modal-panel cart-offers-modal-panel" role="dialog" aria-modal="true" aria-labelledby="offersModalTitle">
                    <header class="offers-modal-header">
                        <div>
                            <h2 id="offersModalTitle">Offers on your bag</h2>
                            <p>
                                <c:choose>
                                    <c:when test="${cartPage.appliedCouponPresent}">
                                        1/${fn:length(availableCoupons)} Offers Applied
                                    </c:when>
                                    <c:otherwise>
                                        0/${fn:length(availableCoupons)} Offers Applied
                                    </c:otherwise>
                                </c:choose>
                            </p>
                        </div>

                        <button type="button" class="cart-modal-close" data-cart-modal-close aria-label="Close offers modal"></button>
                    </header>

                    <section class="offers-modal-grid">
                        <c:choose>
                            <c:when test="${not empty availableCoupons}">
                                <c:forEach var="coupon" items="${availableCoupons}">
                                    <article class="offer-card">
                                        <div class="offer-card-confetti">
                                            <span class="checkout-logo">MD</span>
                                        </div>

                                        <h3>${coupon.couponTitle}</h3>
                                        <p>${coupon.couponDescription}</p>

                                        <c:choose>
                                            <c:when test="${cartPage.appliedCouponPresent and cartPage.appliedCoupon.couponId eq coupon.couponId}">
                                                <form method="post" action="${contextPath}/cart/coupon/remove">
                                                    <button type="submit">REMOVE</button>
                                                </form>
                                            </c:when>

                                            <c:otherwise>
                                                <form method="post" action="${contextPath}/cart/coupon/apply">
                                                    <input type="hidden" name="couponId" value="${coupon.couponId}">
                                                    <button type="submit">APPLY OFFER</button>
                                                </form>
                                            </c:otherwise>
                                        </c:choose>
                                    </article>
                                </c:forEach>
                            </c:when>

                            <c:otherwise>
                                <p class="coupon-empty-text">No offers available for your bag.</p>
                            </c:otherwise>
                        </c:choose>
                    </section>
                </div>
            </section>
        </c:when>

        <c:otherwise>
            <section class="cart-empty-state">
                <div class="cart-empty-icon" aria-hidden="true"></div>
                <h1>Your bag is empty</h1>
                <p>Add products from the catalog to see them here.</p>
                <a href="${contextPath}/products" class="md-btn md-btn-primary">CONTINUE SHOPPING</a>
            </section>
        </c:otherwise>
    </c:choose>
</main>

<script src="${contextPath}/assets/js/common/toast.js"></script>
<script src="${contextPath}/assets/js/cart/cart.js"></script>
</body>
</html>