<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="pageData" value="${accountFeaturePage}" />

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${pageData.title} - MyntraDemo</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="stylesheet" href="${contextPath}/assets/css/common/base.css">
    <link rel="stylesheet" href="${contextPath}/assets/css/common/toast.css">
    <link rel="stylesheet" href="${contextPath}/assets/css/account/account.css">
</head>
<body>

<%@ include file="/WEB-INF/views/common/toast.jsp" %>
<%@ include file="/WEB-INF/views/common/header.jsp" %>

<main class="account-page">
    <section class="account-shell">
        <aside class="account-sidebar">
            <div class="account-user-card">
                <div class="account-avatar">R</div>

                <div>
                    <span>Hello,</span>
                    <strong>${sessionScope.authUserName}</strong>
                </div>
            </div>

            <nav class="account-menu">
                <a href="${contextPath}/orders">Orders</a>
                <a href="${contextPath}/wishlist">Wishlist</a>
                <a href="${contextPath}/gift-cards" class="${pageData.activeMenu eq 'giftCards' ? 'is-active' : ''}">Gift Cards</a>
                <a href="${contextPath}/contacts">Contact Us</a>

                <span></span>

                <a href="${contextPath}/myntra-credit" class="${pageData.activeMenu eq 'credit' ? 'is-active' : ''}">Myntra Credit</a>
                <a href="${contextPath}/coupons" class="${pageData.activeMenu eq 'coupons' ? 'is-active' : ''}">Coupons</a>
                <a href="${contextPath}/saved-cards" class="${pageData.activeMenu eq 'savedCards' ? 'is-active' : ''}">Saved Cards</a>
                <a href="${contextPath}/saved-vpa" class="${pageData.activeMenu eq 'savedVpa' ? 'is-active' : ''}">Saved VPA</a>
                <a href="${contextPath}/address">Saved Addresses</a>

                <span></span>

                <a href="${contextPath}/profile">Edit Profile</a>
            </nav>
        </aside>

        <section class="account-content">
            <div class="account-card">
                <div class="account-card-head">
                    <div>
                        <h1>${pageData.title}</h1>
                        <p>${pageData.subtitle}</p>
                    </div>

                    <c:if test="${pageData.totalItems gt 0}">
                        <strong>${pageData.totalItems} item(s)</strong>
                    </c:if>
                </div>

                <c:choose>
                    <c:when test="${pageData.totalItems eq 0}">
                        <section class="account-feature-empty">
                            <div class="account-feature-icon">${pageData.iconText}</div>

                            <h2>${pageData.emptyTitle}</h2>
                            <p>${pageData.emptyDescription}</p>

                            <a href="${contextPath}/products">CONTINUE SHOPPING</a>
                        </section>
                    </c:when>

                    <c:otherwise>
                        <section class="account-feature-list">
                            <c:forEach var="item" items="${pageData.items}">
                                <article class="account-feature-item">
                                    <div class="account-feature-item-main">
                                        <div class="account-feature-item-icon">
                                            <c:choose>
                                                <c:when test="${not empty item.badge}">
                                                    ${item.badge}
                                                </c:when>
                                                <c:otherwise>
                                                    ${pageData.iconText}
                                                </c:otherwise>
                                            </c:choose>
                                        </div>

                                        <div>
                                            <h2>${item.title}</h2>

                                            <c:if test="${not empty item.subtitle}">
                                                <p>${item.subtitle}</p>
                                            </c:if>

                                            <c:if test="${item.detailAvailable}">
                                                <small>${item.detail}</small>
                                            </c:if>
                                        </div>
                                    </div>

                                    <div class="account-feature-item-side">
                                        <c:if test="${item.amountAvailable}">
                                            <strong>${item.amount}</strong>
                                        </c:if>

                                        <c:if test="${item.statusAvailable}">
                                            <span>${item.status}</span>
                                        </c:if>
                                    </div>
                                </article>
                            </c:forEach>
                        </section>
                    </c:otherwise>
                </c:choose>
            </div>
        </section>
    </section>
</main>

<script src="${contextPath}/assets/js/common/toast.js"></script>
</body>
</html>