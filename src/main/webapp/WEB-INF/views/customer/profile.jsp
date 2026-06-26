<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Profile - MyntraDemo</title>
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
                    <strong>${profileUser.fullName}</strong>
                </div>
            </div>

            <nav class="account-menu">
                <a href="${contextPath}/orders">Orders</a>
                <a href="${contextPath}/wishlist">Wishlist</a>
                <a href="${contextPath}/gift-cards">Gift Cards</a>
                <a href="${contextPath}/contacts">Contact Us</a>

                <span></span>

                <a href="${contextPath}/myntra-credit">Myntra Credit</a>
                <a href="${contextPath}/coupons">Coupons</a>
                <a href="${contextPath}/saved-cards">Saved Cards</a>
                <a href="${contextPath}/saved-vpa">Saved VPA</a>
                <a href="${contextPath}/address">Saved Addresses</a>

                <span></span>

                <a href="${contextPath}/profile" class="is-active">Edit Profile</a>
            </nav>
        </aside>

        <section class="account-content">
            <div class="account-card account-profile-card">
                <div class="account-card-head">
                    <div>
                        <h1>Profile Details</h1>
                        <p>Manage your personal information for orders and delivery.</p>
                    </div>

                    <strong>Active</strong>
                </div>

                <div class="profile-details-list">
                    <div>
                        <span>Full Name</span>
                        <strong>${profileUser.fullName}</strong>
                    </div>

                    <div>
                        <span>Mobile Number</span>
                        <strong>
                            <c:choose>
                                <c:when test="${not empty profileUser.phone}">
                                    ${profileUser.phone}
                                </c:when>
                                <c:otherwise>Not added</c:otherwise>
                            </c:choose>
                        </strong>
                    </div>

                    <div>
                        <span>Email ID</span>
                        <strong>${profileUser.email}</strong>
                    </div>
                </div>
            </div>

            <div class="account-card">
                <div class="account-card-head">
                    <div>
                        <h2>Edit Profile</h2>
                        <p>Update your personal details. Email is locked for login security.</p>
                    </div>
                </div>

                <form method="post" action="${contextPath}/profile/update" class="profile-edit-form">
                    <label>
                        <span>Full Name</span>
                        <input type="text" name="fullName" value="${profileUser.fullName}" required>
                    </label>

                    <label>
                        <span>Mobile Number</span>
                        <input type="tel" name="phone" value="${profileUser.phone}" placeholder="10 digit mobile number">
                    </label>

                    <label class="full-width">
                        <span>Email ID</span>
                        <input type="email" value="${profileUser.email}" disabled>
                        <small>Email cannot be changed.</small>
                    </label>

                    <button type="submit">SAVE DETAILS</button>
                </form>
            </div>
        </section>
    </section>
</main>

<script src="${contextPath}/assets/js/common/toast.js"></script>
</body>
</html>