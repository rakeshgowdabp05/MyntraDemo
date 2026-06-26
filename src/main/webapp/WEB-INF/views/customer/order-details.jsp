<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="currency" value="${empty currencySymbol ? '₹' : currencySymbol}" />

<c:url var="helpUrl" value="/contacts">
    <c:param name="storeOrderId" value="${order.displayOrderNumber}" />
    <c:if test="${not empty primaryItem}">
        <c:param name="itemId" value="${primaryItem.productId}" />
    </c:if>
</c:url>

<c:url var="addAddressUrl" value="/address/add">
    <c:param name="redirectTo" value="/order/details?id=${order.orderId}" />
</c:url>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Order Details - MyntraDemo</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="stylesheet" href="${contextPath}/assets/css/common/base.css">
    <link rel="stylesheet" href="${contextPath}/assets/css/common/toast.css">
    <link rel="stylesheet" href="${contextPath}/assets/css/checkout/order-details.css">
</head>
<body>

<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/toast.jsp" %>

<main class="order-details-page">
    <section class="account-layout">
        <aside class="account-sidebar">
            <div class="account-title-block">
                <h2>Account</h2>
                <p>${order.displayCustomerName}</p>
            </div>

            <nav class="account-nav">
                <a href="${contextPath}/profile" class="account-main-link">Overview</a>

                <span>ORDERS</span>
                <a href="${contextPath}/orders" class="active">Orders &amp; Returns</a>

                <span>CREDITS</span>
                <a href="${contextPath}/coupons">Coupons</a>
                <a href="${contextPath}/credits">Myntra Credit</a>
                <a href="${contextPath}/cash">MynCash</a>

                <span>ACCOUNT</span>
                <a href="${contextPath}/profile">Profile</a>
                <a href="${contextPath}/saved-cards">Saved Cards</a>
                <a href="${contextPath}/addresses">Addresses</a>
                <a href="${contextPath}/insider">Myntra Insider</a>
            </nav>
        </aside>

        <section class="order-detail-content">
            <c:if test="${not empty primaryItem}">
                <section class="order-hero-card">
                    <a href="${helpUrl}" class="help-chip">Help</a>

                    <a href="${contextPath}/product?id=${primaryItem.productId}" class="order-product-image">
                        <c:choose>
                            <c:when test="${primaryItem.imageAvailable}">
                                <img src="${primaryItem.imageUrl}" alt="${primaryItem.displayProductName}">
                            </c:when>
                            <c:otherwise>
                                <span>${primaryItem.displayBrandName}</span>
                            </c:otherwise>
                        </c:choose>
                    </a>

                    <h1>${primaryItem.displayBrandName}</h1>
                    <p>${primaryItem.displayProductName}</p>
                    <p>Size: ${primaryItem.displaySizeLabel} · Quantity: ${primaryItem.quantity}</p>
                    <strong>Order ID: # ${order.displayOrderNumber}</strong>
                </section>
            </c:if>

            <section class="tracking-card ${order.orderStatus eq 'CANCELLED' ? 'is-cancelled' : ''}">
                <c:choose>
                    <c:when test="${order.orderStatus eq 'CANCELLED'}">
                        <h2>Cancelled on ${order.placedDateLabel}</h2>

                        <button type="button" class="tracking-step cancelled-step" data-track-modal-open>
                            <span class="tracking-icon"></span>
                            <span class="tracking-copy">
                                <strong>Cancelled</strong>
                                <em>on ${order.placedDateLabel}</em>
                            </span>
                            <b>›</b>
                        </button>

                        <div class="tracking-line cancelled-line"></div>

                        <div class="tracking-mini cancelled-mini">
                            <span></span>
                            <strong>Order cancelled</strong>
                            <em>on ${order.placedDateLabel}</em>
                        </div>
                    </c:when>

                    <c:otherwise>
                        <h2>Arriving by ${order.estimatedDeliveryLabel}</h2>

                        <button type="button" class="tracking-step is-active" data-track-modal-open>
                            <span class="tracking-icon"></span>
                            <span class="tracking-copy">
                                <strong>Placed</strong>
                                <em>on ${order.placedDateLabel}</em>
                            </span>
                            <b>›</b>
                        </button>

                        <div class="tracking-line"></div>

                        <div class="tracking-mini">
                            <span></span>
                            <strong>Order placed</strong>
                            <em>on ${order.placedDateLabel}</em>
                        </div>
                    </c:otherwise>
                </c:choose>

                <div class="tracking-actions">
                    <button type="button" data-track-modal-open>Track Item</button>

                    <c:choose>
                        <c:when test="${order.orderStatus eq 'CANCELLED'}">
                            <button type="button" class="disabled-action" disabled>Cancelled</button>
                        </c:when>
                        <c:otherwise>
                            <button type="button" data-cancel-modal-open>Cancel Item</button>
                        </c:otherwise>
                    </c:choose>
                </div>
            </section>

            <section class="expiry-strip">
                <span></span>
                <p>Expiry Date <strong>31 May, 2028</strong></p>
            </section>

            <section class="delivery-card">
                <div class="avatar">${fn:substring(order.displayCustomerName, 0, 1)}</div>

                <div class="delivery-main">
                    <h2>Delivery To</h2>
                    <strong>${order.displayCustomerName}</strong>

                    <div class="delivery-info-row">
                        <span>Contact Details</span>
                        <p>${order.displayCustomerPhone}</p>
                    </div>

                    <div class="delivery-info-row">
                        <span>Delivery Address</span>
                        <p>${order.displayDeliveryAddressText}</p>
                    </div>

                    <button type="button" class="change-address-btn" data-address-intro-open>
                        Change Address
                    </button>
                </div>

                <div class="map-chip"></div>
            </section>

            <c:if test="${order.savingsAvailable}">
                <section class="saving-strip">
                    <span>%</span>
                    <p>
                        On this item you saved a total of
                        <strong>${currency}<fmt:formatNumber value="${order.totalSavedAmount}" maxFractionDigits="0" groupingUsed="false" /></strong>
                    </p>
                </section>
            </c:if>

            <c:if test="${order.paymentStatus eq 'PENDING'}">
                <section class="prepay-card">
                    <div class="prepay-head">
                        <div>
                            <h2>Pre pay for your order</h2>
                            <p>Pay now to enjoy a cashless &amp; hassle free delivery experience.</p>
                        </div>
                        <span></span>
                    </div>

                    <div class="prepay-subtitle">
                        Paying For 1 of ${order.totalItems} Item(s)
                    </div>

                    <div class="prepay-item">
                        <c:if test="${not empty primaryItem and primaryItem.imageAvailable}">
                            <a href="${contextPath}/product?id=${primaryItem.productId}">
                                <img src="${primaryItem.imageUrl}" alt="${primaryItem.displayProductName}">
                            </a>
                        </c:if>

                        <div>
                            <strong>${primaryItem.displayBrandName}</strong>
                            <p>${primaryItem.displayProductName}</p>
                        </div>
                    </div>

                    <button type="button">
                        Pay Now: ${currency}<fmt:formatNumber value="${order.payableAmount}" maxFractionDigits="0" groupingUsed="false" />
                    </button>
                </section>
            </c:if>

            <section class="price-card">
                <button type="button" class="price-card-head" data-payment-modal-open>
                    <h2>Total Order Price</h2>
                    <strong>
                        ${currency}<fmt:formatNumber value="${order.payableAmount}" maxFractionDigits="0" groupingUsed="false" />
                        <span>⌄</span>
                    </strong>
                </button>

                <div class="price-method">
                    <span></span>
                    <p>${order.displayPaymentSentence}</p>
                </div>
            </section>

            <section class="updates-card">
                <div class="updates-icon"></div>
                <div>
                    <h2>Updates sent to</h2>
                    <span>Call</span>
                    <p>${order.displayCustomerPhone}</p>
                </div>
            </section>

            <section class="meta-card">
                <div class="meta-icon"></div>
                <div>
                    <h2>Order details</h2>

                    <div class="meta-grid">
                        <div>
                            <span>Ordered On</span>
                            <strong>${order.orderedOnLabel}</strong>
                        </div>

                        <div>
                            <span>Order ID</span>
                            <strong># ${order.displayOrderNumber}</strong>
                        </div>
                    </div>
                </div>
            </section>
        </section>
    </section>
</main>

<section class="order-modal" data-track-modal aria-hidden="true">
    <div class="order-modal-backdrop" data-modal-close></div>

    <div class="track-modal-panel">
        <header>
            <h2>Track Item</h2>
            <button type="button" data-modal-close aria-label="Close"></button>
        </header>

        <div class="track-modal-body">
            <c:choose>
                <c:when test="${order.orderStatus eq 'CANCELLED'}">
                    <strong>Cancelled on ${order.placedDateLabel}</strong>

                    <div class="track-modal-step active cancelled">
                        <span></span>
                        <div>
                            <h3>Order cancelled</h3>
                            <p>This item was cancelled on ${order.placedDateLabel}.</p>
                        </div>
                    </div>
                </c:when>

                <c:otherwise>
                    <strong>Arriving by ${order.estimatedDeliveryLabel}</strong>

                    <div class="track-modal-step active">
                        <span></span>
                        <div>
                            <h3>Order placed</h3>
                            <p>Placed on ${order.placedDateLabel}</p>
                        </div>
                    </div>

                    <div class="track-modal-step">
                        <span></span>
                        <div>
                            <h3>Order confirmed</h3>
                            <p>Your item is being prepared for dispatch.</p>
                        </div>
                    </div>

                    <div class="track-modal-step">
                        <span></span>
                        <div>
                            <h3>Delivery expected</h3>
                            <p>${order.estimatedDeliveryLabel}</p>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</section>

<section class="order-modal" data-cancel-modal aria-hidden="true">
    <div class="order-modal-backdrop" data-modal-close></div>

    <form method="post" action="${contextPath}/order/cancel" class="cancel-modal-panel">
        <input type="hidden" name="orderId" value="${order.orderId}">

        <header>
            <h2>Cancel Item</h2>
            <button type="button" data-modal-close aria-label="Close"></button>
        </header>

        <div class="cancel-item-preview">
            <c:if test="${not empty primaryItem and primaryItem.imageAvailable}">
                <img src="${primaryItem.imageUrl}" alt="${primaryItem.displayProductName}">
            </c:if>

            <div>
                <strong>${primaryItem.displayBrandName}</strong>
                <p>${primaryItem.displayProductName}</p>
                <span>Size: ${primaryItem.displaySizeLabel} · Quantity: ${primaryItem.quantity}</span>
            </div>
        </div>

        <p class="cancel-warning">
            This will cancel the item from your order and update the real order status.
        </p>

        <div class="cancel-actions">
            <button type="button" data-modal-close>NO, KEEP ITEM</button>
            <button type="submit">YES, CANCEL</button>
        </div>
    </form>
</section>

<section class="order-modal" data-address-intro-modal aria-hidden="true">
    <div class="order-modal-backdrop" data-modal-close></div>

    <div class="address-modal-panel small">
        <header>
            <h2>Change delivery address</h2>
            <button type="button" data-modal-close aria-label="Close"></button>
        </header>

        <p class="address-modal-subtitle">
            Address will be changed for all the eligible items in this order.
        </p>

        <div class="address-item-preview">
            <c:if test="${not empty primaryItem and primaryItem.imageAvailable}">
                <img src="${primaryItem.imageUrl}" alt="${primaryItem.displayProductName}">
            </c:if>

            <div>
                <strong>${primaryItem.displayBrandName}</strong>
                <p>${primaryItem.displayProductName}</p>
                <span>Size: ${primaryItem.displaySizeLabel}</span>
            </div>
        </div>

        <button type="button" class="modal-primary-btn" data-address-list-open>CONTINUE</button>
    </div>
</section>

<section class="order-modal" data-address-list-modal aria-hidden="true">
    <div class="order-modal-backdrop" data-modal-close></div>

    <form method="post" action="${contextPath}/order/address/update" class="address-list-panel">
        <input type="hidden" name="orderId" value="${order.orderId}">

        <a href="${addAddressUrl}" class="add-new-address-btn">
            ADD NEW ADDRESS
        </a>

        <h2>DEFAULT ADDRESS</h2>

        <div class="address-list-scroll">
            <c:forEach var="address" items="${addresses}" varStatus="status">
                <label class="address-select-row">
                    <input type="radio" name="addressId" value="${address.addressId}" ${status.first ? 'checked' : ''}>

                    <span class="radio-ui"></span>

                    <span class="address-select-copy">
                        <strong>${address.fullName}</strong>
                        <p>${address.displayAddress}</p>
                        <em>Mobile: ${address.phone}</em>
                    </span>

                    <b>HOME</b>
                </label>

                <c:if test="${status.first}">
                    <h2>OTHER ADDRESSES</h2>
                </c:if>
            </c:forEach>
        </div>

        <div class="address-list-actions">
            <button type="button" data-modal-close>CANCEL</button>
            <button type="submit">CONFIRM</button>
        </div>
    </form>
</section>

<section class="order-modal" data-payment-modal aria-hidden="true">
    <div class="order-modal-backdrop" data-modal-close></div>

    <div class="payment-info-panel">
        <header>
            <h2>Payment Information</h2>
            <button type="button" data-modal-close aria-label="Close"></button>
        </header>

        <div class="payment-info-row">
            <span>
                <c:if test="${not empty primaryItem}">
                    ${primaryItem.quantity} x ${primaryItem.displayProductName}
                </c:if>
            </span>
            <strong>${currency}<fmt:formatNumber value="${order.totalMrp}" maxFractionDigits="0" groupingUsed="false" /></strong>
        </div>

        <c:if test="${order.totalDiscount gt 0}">
            <div class="payment-info-row">
                <span>Discount</span>
                <strong>-${currency}<fmt:formatNumber value="${order.totalDiscount}" maxFractionDigits="0" groupingUsed="false" /></strong>
            </div>
        </c:if>

        <div class="payment-info-row">
            <span>Discounted Price</span>
            <strong>${currency}<fmt:formatNumber value="${order.discountedPrice}" maxFractionDigits="0" groupingUsed="false" /></strong>
        </div>

        <c:if test="${order.paymentFeeAvailable}">
            <div class="payment-info-row">
                <span>${order.displayPaymentFeeLabel}</span>
                <strong>${currency}<fmt:formatNumber value="${order.paymentFee}" maxFractionDigits="0" groupingUsed="false" /></strong>
            </div>
        </c:if>

        <div class="payment-info-total">
            <span>Total Paid</span>
            <strong>${currency}<fmt:formatNumber value="${order.payableAmount}" maxFractionDigits="0" groupingUsed="false" /></strong>
        </div>

        <div class="payment-mode-box">
            <span></span>
            <p>${order.displayPaymentSentence}</p>
        </div>

        <button type="button" class="invoice-trigger" data-invoice-modal-open>GET INVOICE</button>
    </div>
</section>

<section class="order-modal" data-invoice-modal aria-hidden="true">
    <div class="order-modal-backdrop" data-modal-close></div>

    <div class="invoice-panel">
        <header>
            <h2>INVOICES</h2>
            <button type="button" data-modal-close aria-label="Close"></button>
        </header>

        <p class="invoice-help">
            In case of download failure please raise a request from Helpcenter
        </p>

        <article class="invoice-card">
            <div class="invoice-card-head">
                <span></span>
                <div>
                    <strong>Invoice 1</strong>
                    <p>${order.displayOrderNumber}</p>
                </div>
            </div>

            <div class="invoice-line">
                <span>Charge</span>
                <strong>${order.displayPaymentFeeLabel}</strong>
            </div>

            <div class="invoice-line">
                <span>Amount</span>
                <strong>
                    ${currency}<fmt:formatNumber value="${order.paymentFee}" maxFractionDigits="0" groupingUsed="false" />
                </strong>
            </div>

            <div class="invoice-line">
                <span>Invoice Date</span>
                <strong>${order.orderedOnLabel}</strong>
            </div>

            <a href="${contextPath}/order/invoice/download?id=${order.orderId}" class="invoice-download-btn">
                DOWNLOAD
            </a>
        </article>
    </div>
</section>

<script src="${contextPath}/assets/js/common/toast.js"></script>
<script src="${contextPath}/assets/js/checkout/order-details.js"></script>
</body>
</html>