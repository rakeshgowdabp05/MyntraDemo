<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="mode" value="${requestScope.mode}" />

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Help Center - MyntraDemo</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="stylesheet" href="${contextPath}/assets/css/common/base.css">
    <link rel="stylesheet" href="${contextPath}/assets/css/common/toast.css">
    <link rel="stylesheet" href="${contextPath}/assets/css/checkout/help-center.css">
</head>
<body>

<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/toast.jsp" %>

<main class="help-page">
    <section class="help-shell">
        <header class="help-head">
            <div>
                <h1>HELP CENTER</h1>
                <p>We are here to help you</p>
            </div>

            <a href="${contextPath}/orders" class="help-order-card">
                <span></span>
                <div>
                    <strong>TRACK, CANCEL, RETURN/EXCHANGE</strong>
                    <p>Manage your purchases</p>
                </div>
                <b>ORDERS</b>
            </a>
        </header>

        <section class="help-layout">
            <aside class="help-sidebar">
                <h2>SELECT QUERY TYPE</h2>

                <a href="${contextPath}/contacts${not empty order ? '?storeOrderId='.concat(order.displayOrderNumber) : ''}"
                   class="${mode eq 'order-detail' or mode eq 'orders' ? 'active' : ''}">
                    Order Related Queries
                    <span>›</span>
                </a>

                <a href="${contextPath}/contacts/generic" class="${mode eq 'generic' ? 'active' : ''}">
                    Non-order Related Issues
                    <span>›</span>
                </a>

                <a href="${contextPath}/contacts/issues" class="${mode eq 'issues' ? 'active' : ''}">
                    Recent Issues
                    <span>›</span>
                </a>

                <div class="help-divider"></div>

                <a href="${contextPath}/faqs">
                    Frequently Asked Questions
                    <span>›</span>
                </a>

                <p class="postal-note">
                    Want to reach us old style? Here is our
                    <a href="${contextPath}/contact-address">postal address</a>
                </p>
            </aside>

            <section class="help-content">
                <c:choose>
                    <c:when test="${mode eq 'order-detail' and not empty order}">
                        <article class="help-order-detail-card">
                            <div class="help-status-row">
                                <span></span>
                                <div>
                                    <strong>Confirmed</strong>
                                    <p>Arriving by ${order.estimatedDeliveryLabel}</p>
                                </div>
                            </div>

                            <a href="${contextPath}/order/details?id=${order.orderId}" class="help-product-row">
                                <c:if test="${not empty primaryItem and primaryItem.imageAvailable}">
                                    <img src="${primaryItem.imageUrl}" alt="${primaryItem.displayProductName}">
                                </c:if>

                                <div>
                                    <strong>${primaryItem.displayBrandName}</strong>
                                    <p>${primaryItem.displayProductName}</p>
                                    <span>Size: ${primaryItem.displaySizeLabel}</span>
                                </div>

                                <b>›</b>
                            </a>

                            <h2>Contact Us</h2>
                            <p>Hi ${order.displayCustomerName}, let us help you with your queries</p>

                            <a href="${contextPath}/contacts/chat?storeOrderId=${order.displayOrderNumber}" class="chat-button">
    CHAT WITH US
</a>

<a href="tel:${order.displayCustomerPhone}" class="call-button">
    CALL ME NOW
</a>
                        </article>
                    </c:when>

                    <c:when test="${mode eq 'generic'}">
                        <article class="generic-help">
                            <h2>Browse Topics</h2>

                            <div class="topic-grid">
                                <a href="#"><span>👥</span><strong>Account</strong></a>
                                <a href="#"><span>↩</span><strong>Returns &amp; Exchanges</strong></a>
                                <a href="#"><span>♛</span><strong>Myntra Credit &amp; Insider</strong></a>
                                <a href="#"><span>⚙</span><strong>Offers</strong></a>
                                <a href="#"><span>💳</span><strong>Payments</strong></a>
                                <a href="#"><span>🧾</span><strong>Cancellations &amp; Charges</strong></a>
                            </div>

                            <div class="faq-list">
                                <a href="#">How do I login to my Myntra account?<span>›</span></a>
                                <a href="#">I am not able to login to my Myntra account.<span>›</span></a>
                                <a href="#">Why am I not getting an OTP?<span>›</span></a>
                                <a href="#">I am not getting the Forgot Password link.<span>›</span></a>
                            </div>
                        </article>
                    </c:when>

                    <c:when test="${mode eq 'issues'}">
                        <article class="issues-empty">
                            <div class="issues-filter">
                                <span>Queries from <strong>Last 30 Days</strong></span>
                                <button type="button">CHANGE</button>
                            </div>

                            <div class="empty-circle"></div>
                            <h2>No queries found</h2>
                            <p>
                                There were no queries raised<br>
                                in <strong>Last 30 Days</strong>
                            </p>
                            <span>Search queries from <strong>Different dates</strong></span>
                        </article>
                    </c:when>

                    <c:otherwise>
                        <article class="help-order-list-card">
                            <h2>Select the item we can help you with</h2>

                            <c:if test="${not empty order}">
                                <a href="${contextPath}/contacts?storeOrderId=${order.displayOrderNumber}" class="help-product-row">
                                    <c:if test="${not empty primaryItem and primaryItem.imageAvailable}">
                                        <img src="${primaryItem.imageUrl}" alt="${primaryItem.displayProductName}">
                                    </c:if>

                                    <div>
                                        <strong>${primaryItem.displayBrandName}</strong>
                                        <p>${primaryItem.displayProductName}</p>
                                        <span>Size: ${primaryItem.displaySizeLabel}</span>
                                    </div>

                                    <b>›</b>
                                </a>
                            </c:if>
                        </article>
                    </c:otherwise>
                </c:choose>
            </section>
        </section>
    </section>
</main>

<script src="${contextPath}/assets/js/common/toast.js"></script>
</body>
</html>