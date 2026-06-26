<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Chat With Us - MyntraDemo</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="stylesheet" href="${contextPath}/assets/css/common/base.css">
    <link rel="stylesheet" href="${contextPath}/assets/css/common/toast.css">
    <link rel="stylesheet" href="${contextPath}/assets/css/checkout/help-chat.css">
</head>
<body>

<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/toast.jsp" %>

<main class="chat-page">
    <section class="chat-shell">
        <aside class="chat-sidebar">
            <h1>HELP CENTER</h1>
            <p>We are here to help you</p>

            <nav>
                <a href="${contextPath}/contacts?storeOrderId=${order.displayOrderNumber}" class="active">
                    Order Related Queries
                    <span>›</span>
                </a>

                <a href="${contextPath}/contacts/generic">
                    Non-order Related Issues
                    <span>›</span>
                </a>

                <a href="${contextPath}/contacts/issues">
                    Recent Issues
                    <span>›</span>
                </a>
            </nav>
        </aside>

        <section class="chat-content">
            <header class="chat-header">
                <div>
                    <h2>Chat with us</h2>
                    <p>Tell us what went wrong. We will save your query and help you with this order.</p>
                </div>

                <a href="${contextPath}/orders">ORDERS</a>
            </header>

            <c:if test="${not empty order}">
                <article class="chat-order-card">
                    <div class="chat-status-icon"></div>

                    <div class="chat-order-main">
                        <strong>Confirmed</strong>
                        <p>Arriving by ${order.estimatedDeliveryLabel}</p>

                        <a href="${contextPath}/order/details?id=${order.orderId}" class="chat-product-card">
                            <c:if test="${not empty primaryItem and primaryItem.imageAvailable}">
                                <img src="${primaryItem.imageUrl}" alt="${primaryItem.displayProductName}">
                            </c:if>

                            <span>
                                <b>${primaryItem.displayBrandName}</b>
                                <em>${primaryItem.displayProductName}</em>
                                <small>Size: ${primaryItem.displaySizeLabel}</small>
                            </span>

                            <i>›</i>
                        </a>
                    </div>
                </article>
            </c:if>

            <section class="chat-box">
                <div class="chat-message system">
                    <span></span>
                    <p>
                        Hi ${empty order ? 'there' : order.displayCustomerName}, please describe your issue.
                        Your query will be saved with your account and order details.
                    </p>
                </div>

                <form method="post" action="${contextPath}/contacts/chat" class="chat-form">
                    <c:if test="${not empty order}">
                        <input type="hidden" name="storeOrderId" value="${order.displayOrderNumber}">
                    </c:if>

                    <label for="message">Your message</label>

                    <textarea
                            id="message"
                            name="message"
                            rows="6"
                            maxlength="1000"
                            placeholder="Example: I want help with delivery, cancellation, payment, invoice, or address change."
                            required></textarea>

                    <div class="chat-actions">
                        <a href="${empty order ? contextPath.concat('/contacts') : contextPath.concat('/contacts?storeOrderId=').concat(order.displayOrderNumber)}">
                            BACK
                        </a>

                        <button type="submit">SEND MESSAGE</button>
                    </div>
                </form>
            </section>
        </section>
    </section>
</main>

<script src="${contextPath}/assets/js/common/toast.js"></script>
</body>
</html>