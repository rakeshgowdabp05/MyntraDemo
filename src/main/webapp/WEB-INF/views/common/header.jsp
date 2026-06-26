<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="isLoggedIn" value="${not empty sessionScope.authUserId}" />

<link rel="stylesheet" href="${contextPath}/assets/css/common/header.css">

<header class="md-header">
    <div class="md-header-inner">
        <a href="${contextPath}/home" class="md-logo" aria-label="MyntraDemo Home">
            <span>M</span>
            <strong>MyntraDemo</strong>
        </a>

        <nav class="md-main-nav" aria-label="Main navigation">
            <a href="${contextPath}/products?section=men">MEN</a>
            <a href="${contextPath}/products?section=women">WOMEN</a>
            <a href="${contextPath}/products?section=kids">KIDS</a>
            <a href="${contextPath}/products?section=home">HOME</a>
            <a href="${contextPath}/products?section=beauty">BEAUTY</a>
            <a href="${contextPath}/products?section=genz">GENZ</a>
            <a href="${contextPath}/studio" class="studio-link">STUDIO <span>NEW</span></a>
        </nav>

        <form class="md-search" method="get" action="${contextPath}/products" autocomplete="off" data-search-form>
            <button type="submit" class="md-search-icon" aria-label="Search">
                <svg viewBox="0 0 24 24" aria-hidden="true">
                    <circle cx="10.5" cy="10.5" r="7"></circle>
                    <path d="M16 16L21 21"></path>
                </svg>
            </button>

            <input
                    type="search"
                    name="search"
                    value="${param.search}"
                    placeholder="Search for products, brands and more"
                    data-search-input>

            <div class="md-search-suggestions" data-search-suggestions></div>
        </form>

        <nav class="md-action-nav" aria-label="Account navigation">
            <div class="profile-menu">
                <button type="button" class="profile-trigger">
                    <svg class="header-svg-icon" viewBox="0 0 24 24" aria-hidden="true">
                        <path d="M12 12.2C14.65 12.2 16.8 10.05 16.8 7.4C16.8 4.75 14.65 2.6 12 2.6C9.35 2.6 7.2 4.75 7.2 7.4C7.2 10.05 9.35 12.2 12 12.2Z"></path>
                        <path d="M4.2 21.4C4.2 17.1 7.7 14.2 12 14.2C16.3 14.2 19.8 17.1 19.8 21.4"></path>
                    </svg>
                    <strong>Profile</strong>
                </button>

                <div class="profile-dropdown myntra-profile-dropdown">
                    <c:choose>
                        <c:when test="${isLoggedIn}">
                            <div class="profile-dd-user">
                                <h3>Hello ${sessionScope.authUserName}</h3>
                                <p>
                                    <c:choose>
                                        <c:when test="${not empty sessionScope.authUserPhone}">
                                            ${sessionScope.authUserPhone}
                                        </c:when>
                                        <c:otherwise>
                                            ${sessionScope.authUserEmail}
                                        </c:otherwise>
                                    </c:choose>
                                </p>
                            </div>

                            <div class="profile-dd-group">
                                <a href="${contextPath}/orders">Orders</a>
                                <a href="${contextPath}/wishlist">Wishlist</a>
                                <a href="${contextPath}/gift-cards">Gift Cards</a>
                                <a href="${contextPath}/contacts">Contact Us</a>
                            </div>

                            <div class="profile-dd-group">
                                <a href="${contextPath}/myntra-credit">Myntra Credit</a>
                                <a href="${contextPath}/coupons">Coupons</a>
                                <a href="${contextPath}/saved-cards">Saved Cards</a>
                                <a href="${contextPath}/saved-vpa">Saved VPA</a>
                                <a href="${contextPath}/address">Saved Addresses</a>
                            </div>

                            <div class="profile-dd-group profile-dd-last">
                                <a href="${contextPath}/profile">Edit Profile</a>

                                <form method="post" action="${contextPath}/logout" class="profile-logout-form">
                                    <button type="submit">Logout</button>
                                </form>
                            </div>
                        </c:when>

                        <c:otherwise>
                            <div class="profile-dd-user">
                                <h3>Welcome</h3>
                                <p>To access account and manage orders</p>
                            </div>

                            <div class="profile-dd-login">
                                <a href="${contextPath}/login">LOGIN / SIGNUP</a>
                            </div>

                            <div class="profile-dd-group">
                                <a href="${contextPath}/orders">Orders</a>
                                <a href="${contextPath}/wishlist">Wishlist</a>
                                <a href="${contextPath}/gift-cards">Gift Cards</a>
                                <a href="${contextPath}/contacts">Contact Us</a>
                            </div>

                            <div class="profile-dd-group">
                                <a href="${contextPath}/myntra-credit">Myntra Credit</a>
                                <a href="${contextPath}/coupons">Coupons</a>
                                <a href="${contextPath}/saved-cards">Saved Cards</a>
                                <a href="${contextPath}/saved-vpa">Saved VPA</a>
                                <a href="${contextPath}/address">Saved Addresses</a>
                            </div>

                            <div class="profile-dd-group profile-dd-last">
                                <a href="${contextPath}/profile">Edit Profile</a>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <a href="${contextPath}/wishlist" class="header-action-link">
                <svg class="header-svg-icon" viewBox="0 0 24 24" aria-hidden="true">
                    <path d="M12 20.4C11.7 20.4 11.4 20.3 11.2 20.1C5.7 15.2 3 12.7 3 8.9C3 5.8 5.4 3.4 8.5 3.4C10.2 3.4 11.9 4.2 13 5.5C14.1 4.2 15.8 3.4 17.5 3.4C20.6 3.4 23 5.8 23 8.9C23 12.7 20.3 15.2 14.8 20.1C14.6 20.3 14.3 20.4 14 20.4H12Z"></path>
                </svg>
                <strong>Wishlist</strong>
            </a>

            <a href="${contextPath}/cart" class="header-action-link bag-link">
                <svg class="header-svg-icon" viewBox="0 0 24 24" aria-hidden="true">
                    <path d="M6.2 8.2H17.8L18.7 21H5.3L6.2 8.2Z"></path>
                    <path d="M8.7 8.2V6.3C8.7 4.5 10.2 3 12 3C13.8 3 15.3 4.5 15.3 6.3V8.2"></path>
                </svg>
                <strong>Bag</strong>

                <c:if test="${not empty cartItemCount and cartItemCount gt 0}">
                    <em>${cartItemCount}</em>
                </c:if>
            </a>
        </nav>
    </div>
</header>

<script src="${contextPath}/assets/js/common/header.js"></script>