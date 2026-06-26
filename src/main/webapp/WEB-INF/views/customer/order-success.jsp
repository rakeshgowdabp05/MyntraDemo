<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="currency" value="${empty currencySymbol ? '₹' : currencySymbol}" />

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Order Confirmed - MyntraDemo</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="stylesheet" href="${contextPath}/assets/css/common/base.css">
    <link rel="stylesheet" href="${contextPath}/assets/css/common/toast.css">
    <link rel="stylesheet" href="${contextPath}/assets/css/checkout/order-success.css">
</head>
<body>

<%@ include file="/WEB-INF/views/common/toast.jsp" %>

<main class="order-success-page">
    <section class="order-success-shell">
        <section class="order-confirm-card">
            <div class="success-burst">
                <span></span>
            </div>

            <h1>Order confirmed</h1>

            <p>
                Your order is confirmed. You will receive an order confirmation
                email/SMS shortly with the expected delivery date for your items.
            </p>
        </section>

        <section class="success-delivery-card">
            <div class="success-delivery-copy">
                <span>Delivering to:</span>
                <strong>${order.displayCustomerName} | ${order.displayCustomerPhone}</strong>
                <p>${order.displayDeliveryAddressText}</p>

                <a href="${contextPath}/order/details?id=${order.orderId}">ORDER DETAILS &gt;</a>
            </div>

            <div class="delivery-illustration">
                <span></span>
            </div>

            <div class="success-note">
                <i></i>
                <span>You can Track/View/Modify order from orders page.</span>
            </div>
        </section>

        <div class="success-actions">
            <a href="${contextPath}/products" class="secondary">CONTINUE SHOPPING</a>
            <a href="${contextPath}/order/details?id=${order.orderId}">VIEW ORDER</a>
        </div>
    </section>
</main>

<script src="${contextPath}/assets/js/common/toast.js"></script>
</body>
</html>