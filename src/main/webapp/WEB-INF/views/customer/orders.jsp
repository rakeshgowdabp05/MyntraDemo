<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="currency" value="${empty currencySymbol ? '₹' : currencySymbol}" />
<c:set var="selectedStatus" value="${empty selectedStatus ? 'ALL' : selectedStatus}" />

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Orders & Returns - MyntraDemo</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="stylesheet" href="${contextPath}/assets/css/common/base.css">
    <link rel="stylesheet" href="${contextPath}/assets/css/common/toast.css">
    <link rel="stylesheet" href="${contextPath}/assets/css/checkout/orders.css">
</head>
<body>

<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/toast.jsp" %>

<main class="orders-page">
    <section class="orders-shell">
        <aside class="orders-sidebar">
            <div class="orders-account-block">
                <h1>Account</h1>
                <p>${accountDisplayName}</p>
            </div>

            <nav class="orders-account-nav">
                <a href="${contextPath}/profile" class="orders-overview-link">Overview</a>

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

        <section class="orders-content">
            <header class="orders-header">
                <div>
                    <h2>Orders &amp; Returns</h2>
                    <p>View, track, cancel, and get help for your orders</p>
                </div>

                <form method="get" action="${contextPath}/orders" class="orders-search-form">
                    <input type="hidden" name="status" value="${selectedStatus}">
                    <input
                            type="search"
                            name="search"
                            value="${searchText}"
                            placeholder="Search orders by product, brand or order ID">
                    <button type="submit">SEARCH</button>
                </form>
            </header>

            <section class="orders-filter-bar">
                <a href="${contextPath}/orders${not empty searchText ? '?search='.concat(searchText) : ''}"
                   class="${selectedStatus eq 'ALL' ? 'active' : ''}">
                    All
                </a>

                <a href="${contextPath}/orders?status=PLACED${not empty searchText ? '&search='.concat(searchText) : ''}"
                   class="${selectedStatus eq 'PLACED' ? 'active' : ''}">
                    Active
                </a>

                <a href="${contextPath}/orders?status=DELIVERED${not empty searchText ? '&search='.concat(searchText) : ''}"
                   class="${selectedStatus eq 'DELIVERED' ? 'active' : ''}">
                    Delivered
                </a>

                <a href="${contextPath}/orders?status=CANCELLED${not empty searchText ? '&search='.concat(searchText) : ''}"
                   class="${selectedStatus eq 'CANCELLED' ? 'active' : ''}">
                    Cancelled
                </a>
            </section>

            <c:choose>
                <c:when test="${empty orders}">
                    <section class="orders-empty-state">
                        <div class="empty-bag-icon"></div>
                        <h2>No orders found</h2>
                        <p>Your orders will appear here after you place them.</p>
                        <a href="${contextPath}/products">CONTINUE SHOPPING</a>
                    </section>
                </c:when>

                <c:otherwise>
                    <section class="orders-list">
                        <c:forEach var="order" items="${orders}">
                            <article class="order-card ${order.cancelled ? 'is-cancelled' : ''}">
                                <a href="${contextPath}/order/details?id=${order.orderId}" class="order-card-main">
                                    <div class="order-status-icon"></div>

                                    <div class="order-image">
                                        <c:choose>
                                            <c:when test="${order.imageAvailable}">
                                                <img src="${order.imageUrl}" alt="${order.displayProductName}">
                                            </c:when>
                                            <c:otherwise>
                                                <span>${order.displayBrandName}</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>

                                    <div class="order-copy">
                                        <div class="order-status-line">
                                            <strong>${order.statusLine}</strong>
                                            <span>${order.displayOrderStatus}</span>
                                        </div>

                                        <h3>${order.displayBrandName}</h3>
                                        <p>${order.displayProductName}</p>

                                        <div class="order-meta">
                                            <span>Size: ${order.displaySizeLabel}</span>
                                            <span>${order.quantityLabel}</span>
                                            <span>${order.totalItemsLabel}</span>
                                        </div>

                                        <div class="order-bottom-row">
                                            <span>Order ID: # ${order.displayOrderNumber}</span>
                                            <b>${currency}<fmt:formatNumber value="${order.payableAmount}" maxFractionDigits="0" groupingUsed="false" /></b>
                                        </div>
                                    </div>

                                    <div class="order-arrow">›</div>
                                </a>

                                <div class="order-actions">
                                    <a href="${contextPath}/order/details?id=${order.orderId}">VIEW DETAILS</a>

                                    <c:choose>
                                        <c:when test="${order.cancelled}">
                                            <span>CANCELLED</span>
                                        </c:when>

                                        <c:otherwise>
                                            <a href="${contextPath}/contacts?storeOrderId=${order.displayOrderNumber}">HELP</a>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </article>
                        </c:forEach>
                    </section>
                </c:otherwise>
            </c:choose>
        </section>
    </section>
</main>

<script src="${contextPath}/assets/js/common/toast.js"></script>
<script src="${contextPath}/assets/js/checkout/orders.js"></script>
</body>
</html>