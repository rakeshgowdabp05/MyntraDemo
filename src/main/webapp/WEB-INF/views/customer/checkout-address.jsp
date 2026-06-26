<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="currency" value="${empty currencySymbol ? '₹' : currencySymbol}" />
<c:set var="addresses" value="${requestScope.addresses}" />
<c:set var="selectedAddressId" value="${requestScope.selectedCheckoutAddressId}" />
<c:set var="estimateText" value="${empty requestScope.deliveryEstimateText ? 'standard delivery' : requestScope.deliveryEstimateText}" />

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Select Address - MyntraDemo</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="stylesheet" href="${contextPath}/assets/css/common/base.css">
    <link rel="stylesheet" href="${contextPath}/assets/css/common/toast.css">
    <link rel="stylesheet" href="${contextPath}/assets/css/checkout/checkout.css">
</head>
<body>

<%@ include file="/WEB-INF/views/common/toast.jsp" %>

<header class="checkout-header">
    <a href="${contextPath}/products" class="checkout-logo">MD</a>

    <nav class="checkout-steps" aria-label="Checkout steps">
        <span>BAG</span>
        <i></i>
        <span class="is-active">ADDRESS</span>
        <i></i>
        <span>PAYMENT</span>
    </nav>

    <div class="checkout-secure">
        <span></span>
        <strong>100% SECURE</strong>
    </div>
</header>

<main class="checkout-page">
    <section class="checkout-shell">
        <section class="checkout-left">
            <div class="checkout-address-heading">
                <h1>Select Delivery Address</h1>
                <a href="${contextPath}/address">ADD NEW ADDRESS</a>
            </div>

            <c:choose>
                <c:when test="${not empty addresses}">
                    <c:forEach var="address" items="${addresses}">
                        <c:if test="${address.addressId eq selectedAddressId}">
                            <section class="checkout-address-group">
                                <h2>DEFAULT ADDRESS</h2>

                                <article class="checkout-address-card is-selected">
                                    <div class="checkout-address-card-body">
                                        <span class="checkout-radio is-selected"></span>

                                        <div class="checkout-address-content">
                                            <div class="checkout-address-name-row">
                                                <strong>${address.fullName}</strong>
                                                <span>HOME</span>
                                            </div>

                                            <p>${address.displayAddress}</p>

                                            <div class="checkout-address-mobile">
                                                Mobile: <strong>${address.phone}</strong>
                                            </div>

                                            <p class="checkout-cod-note">• Pay on Delivery not available</p>

                                            <div class="checkout-address-actions">
                                                <form method="post" action="${contextPath}/address/delete">
                                                    <input type="hidden" name="addressId" value="${address.addressId}">
                                                    <button type="submit">REMOVE</button>
                                                </form>

                                                <a href="${contextPath}/address">EDIT</a>
                                            </div>
                                        </div>
                                    </div>
                                </article>
                            </section>
                        </c:if>
                    </c:forEach>

                    <section class="checkout-address-group">
                        <h2>OTHER ADDRESS</h2>

                        <div class="checkout-other-address-list">
                            <c:forEach var="address" items="${addresses}">
                                <c:if test="${address.addressId ne selectedAddressId}">
                                    <form method="post" action="${contextPath}/checkout/address/select" class="checkout-address-select-form">
                                        <input type="hidden" name="addressId" value="${address.addressId}">

                                        <button type="submit" class="checkout-address-card checkout-address-button">
                                            <div class="checkout-address-card-body">
                                                <span class="checkout-radio"></span>

                                                <div class="checkout-address-content">
                                                    <div class="checkout-address-name-row">
                                                        <strong>${address.fullName}</strong>
                                                        <span>HOME</span>
                                                    </div>

                                                    <p>${address.displayAddress}</p>

                                                    <div class="checkout-address-mobile">
                                                        Mobile: <strong>${address.phone}</strong>
                                                    </div>
                                                </div>
                                            </div>
                                        </button>
                                    </form>
                                </c:if>
                            </c:forEach>
                        </div>

                        <a href="${contextPath}/address" class="checkout-add-address-inline">
                            + Add New Address
                        </a>
                    </section>
                </c:when>

                <c:otherwise>
                    <section class="checkout-empty-address">
                        <h2>No address found</h2>
                        <p>Add a delivery address to continue checkout.</p>
                        <a href="${contextPath}/address">ADD ADDRESS</a>
                    </section>
                </c:otherwise>
            </c:choose>
        </section>

        <aside class="checkout-right">
            <section class="delivery-estimates">
                <h2>DELIVERY ESTIMATES</h2>

                <c:forEach var="item" items="${cartPage.items}">
                    <article class="delivery-estimate-item">
                        <div class="delivery-estimate-image">
                            <c:choose>
                                <c:when test="${item.hasImage()}">
                                    <c:choose>
                                        <c:when test="${fn:startsWith(item.imageUrl, 'http://') or fn:startsWith(item.imageUrl, 'https://')}">
                                            <img src="${item.imageUrl}" alt="${item.productName}">
                                        </c:when>
                                        <c:otherwise>
                                            <img src="${contextPath}${item.imageUrl}" alt="${item.productName}">
                                        </c:otherwise>
                                    </c:choose>
                                </c:when>
                                <c:otherwise>
                                    <span>MD</span>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <p>
                            Estimated delivery by
                            <strong>${estimateText}</strong>
                        </p>
                    </article>
                </c:forEach>
            </section>

            <section class="checkout-price-card">
                <h2>PRICE DETAILS (${cartPage.totalItems} Item<c:if test="${cartPage.totalItems gt 1}">s</c:if>)</h2>

                <div class="checkout-price-line">
                    <span>Total MRP</span>
                    <strong>
                        ${currency}<fmt:formatNumber value="${cartPage.totalMrp}" maxFractionDigits="0" groupingUsed="false" />
                    </strong>
                </div>

                <c:if test="${cartPage.discountAvailable}">
                    <div class="checkout-price-line">
                        <span>Discount on MRP</span>
                        <strong class="checkout-green">
                            - ${currency}<fmt:formatNumber value="${cartPage.totalDiscount}" maxFractionDigits="0" groupingUsed="false" />
                        </strong>
                    </div>
                </c:if>

                <c:if test="${cartPage.couponDiscountAvailable}">
                    <div class="checkout-price-line">
                        <span>Coupon Discount</span>
                        <strong class="checkout-green">
                            - ${currency}<fmt:formatNumber value="${cartPage.couponDiscount}" maxFractionDigits="0" groupingUsed="false" />
                        </strong>
                    </div>
                </c:if>

                <c:if test="${cartPage.giftPackageFeeAvailable}">
                    <div class="checkout-price-line">
                        <span>Gift Packaging Fee</span>
                        <strong>
                            ${currency}<fmt:formatNumber value="${cartPage.giftPackageFee}" maxFractionDigits="0" groupingUsed="false" />
                        </strong>
                    </div>
                </c:if>

                <c:if test="${cartPage.donationAvailable}">
                    <div class="checkout-price-line">
                        <span>Donation</span>
                        <strong>
                            ${currency}<fmt:formatNumber value="${cartPage.donationAmount}" maxFractionDigits="0" groupingUsed="false" />
                        </strong>
                    </div>
                </c:if>

                <div class="checkout-price-line">
                    <span>
                        Platform Fee
                        <a href="${contextPath}/cart">Know More</a>
                    </span>
                    <strong>₹0</strong>
                </div>

                <div class="checkout-total-line">
                    <span>Total Amount</span>
                    <strong>
                        ${currency}<fmt:formatNumber value="${cartPage.payableAmount}" maxFractionDigits="0" groupingUsed="false" />
                    </strong>
                </div>

                <c:choose>
                    <c:when test="${not empty selectedAddressId}">
                        <a href="${contextPath}/checkout/payment" class="continue-btn">CONTINUE</a>
                    </c:when>
                    <c:otherwise>
                        <button class="continue-btn is-disabled" disabled>CONTINUE</button>
                    </c:otherwise>
                </c:choose>
            </section>
        </aside>
    </section>
</main>

<footer class="checkout-payment-footer">
    <div class="payment-icons">
        <span class="payment-badge ssl">
            <svg viewBox="0 0 24 24"><path d="M6 10V8a6 6 0 1 1 12 0v2h1.2A1.8 1.8 0 0 1 21 11.8v7.4a1.8 1.8 0 0 1-1.8 1.8H4.8A1.8 1.8 0 0 1 3 19.2v-7.4A1.8 1.8 0 0 1 4.8 10H6Zm2 0h8V8a4 4 0 1 0-8 0v2Z"/></svg>
            <b>256-bit<br>SSL</b>
        </span>

        <span class="payment-card visa">VISA</span>
        <span class="payment-card mastercard">MasterCard</span>
        <span class="payment-card amex">AMEX</span>
        <span class="payment-card diners">Diners Club</span>
        <span class="payment-card netbanking">NET<br>BANKING</span>
        <span class="payment-card cod">CASH<br>ON DELIVERY</span>
        <span class="payment-card rupay">RuPay</span>
        <span class="payment-card paypal">PayPal</span>
        <span class="payment-card bhim">BHIM</span>
    </div>

    <a href="${contextPath}/support">Need Help ? Contact Us</a>
</footer>

<script src="${contextPath}/assets/js/common/toast.js"></script>
</body>
</html>