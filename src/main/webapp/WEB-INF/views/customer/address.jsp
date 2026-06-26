<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="addresses" value="${requestScope.addresses}" />

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Saved Addresses - MyntraDemo</title>
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
                <div class="account-avatar">
                    <c:choose>
                        <c:when test="${not empty sessionScope.authUserName}">
                            ${fn:substring(sessionScope.authUserName, 0, 1)}
                        </c:when>
                        <c:otherwise>U</c:otherwise>
                    </c:choose>
                </div>

                <div>
                    <span>Hello,</span>
                    <strong>${sessionScope.authUserName}</strong>
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
                <a href="${contextPath}/address" class="is-active">Saved Addresses</a>

                <span></span>

                <a href="${contextPath}/profile">Edit Profile</a>
            </nav>
        </aside>

        <section class="account-content">
            <div class="account-card address-manager-card">
                <div class="account-card-head">
                    <div>
                        <h1>Saved Addresses</h1>
                        <p>Add, edit, remove, and set your default delivery address.</p>
                    </div>

                    <a href="#add-address" class="account-outline-action">+ ADD NEW ADDRESS</a>
                </div>

                <c:choose>
                    <c:when test="${not empty addresses}">
                        <section class="saved-address-list">
                            <c:forEach var="address" items="${addresses}">
                                <article class="${address.defaultAddress ? 'saved-address-card is-default' : 'saved-address-card'}">
                                    <div class="saved-address-head">
                                        <div>
                                            <strong>${address.fullName}</strong>
                                            <em>
                                                <c:choose>
                                                    <c:when test="${not empty address.addressType}">
                                                        ${address.addressType}
                                                    </c:when>
                                                    <c:otherwise>HOME</c:otherwise>
                                                </c:choose>
                                            </em>
                                        </div>

                                        <c:if test="${address.defaultAddress}">
                                            <span>DEFAULT</span>
                                        </c:if>
                                    </div>

                                    <p>${address.displayAddress}</p>

                                    <div class="saved-address-meta">
                                        <span>Mobile: <strong>${address.phone}</strong></span>
                                        <span>Pincode: <strong>${address.pincode}</strong></span>
                                    </div>

                                    <div class="saved-address-actions">
                                        <details class="address-edit-details">
                                            <summary>EDIT</summary>

                                            <form method="post" action="${contextPath}/address/update" class="compact-address-form">
                                                <input type="hidden" name="addressId" value="${address.addressId}">

                                                <input type="text" name="fullName" value="${address.fullName}" placeholder="Full name" required>
                                                <input type="tel" name="phone" value="${address.phone}" placeholder="Mobile number" required>
                                                <input type="text" name="pincode" value="${address.pincode}" placeholder="Pincode" maxlength="6" required>

                                                <textarea name="addressLine" placeholder="House no, building, street, area" required>${address.addressLine}</textarea>

                                                <input type="text" name="locality" value="${address.locality}" placeholder="Locality / Landmark">

                                                <div>
                                                    <input type="text" name="city" value="${address.city}" placeholder="City" required>
                                                    <input type="text" name="state" value="${address.state}" placeholder="State" required>
                                                </div>

                                                <div>
                                                    <input type="text" name="country" value="${address.country}" placeholder="Country" required>

                                                    <select name="addressType">
                                                        <option value="HOME" <c:if test="${address.addressType eq 'HOME'}">selected</c:if>>Home</option>
                                                        <option value="WORK" <c:if test="${address.addressType eq 'WORK'}">selected</c:if>>Work</option>
                                                        <option value="OTHER" <c:if test="${address.addressType eq 'OTHER'}">selected</c:if>>Other</option>
                                                    </select>
                                                </div>

                                                <button type="submit">SAVE</button>
                                            </form>
                                        </details>

                                        <form method="post" action="${contextPath}/address/delete">
                                            <input type="hidden" name="addressId" value="${address.addressId}">
                                            <button type="submit">REMOVE</button>
                                        </form>

                                        <c:if test="${not address.defaultAddress}">
                                            <form method="post" action="${contextPath}/address/default">
                                                <input type="hidden" name="addressId" value="${address.addressId}">
                                                <button type="submit">MAKE DEFAULT</button>
                                            </form>
                                        </c:if>
                                    </div>
                                </article>
                            </c:forEach>
                        </section>
                    </c:when>

                    <c:otherwise>
                        <section class="saved-address-empty">
                            <h2>No address saved yet</h2>
                            <p>Add your delivery address to make checkout faster.</p>
                        </section>
                    </c:otherwise>
                </c:choose>
            </div>

            <div class="account-card" id="add-address">
                <div class="account-card-head">
                    <div>
                        <h2>Add New Address</h2>
                        <p>Use accurate delivery details for successful delivery.</p>
                    </div>
                </div>

                <form method="post" action="${contextPath}/address/add" class="profile-edit-form">
                    <label>
                        <span>Full Name</span>
                        <input type="text" name="fullName" placeholder="Full name" required>
                    </label>

                    <label>
                        <span>Mobile Number</span>
                        <input type="tel" name="phone" placeholder="10 digit mobile number" required>
                    </label>

                    <label>
                        <span>Pincode</span>
                        <input type="text" name="pincode" placeholder="6 digit pincode" maxlength="6" required>
                    </label>

                    <label class="full-width">
                        <span>Address</span>
                        <textarea name="addressLine" placeholder="House no, building, street, area" required></textarea>
                    </label>

                    <label class="full-width">
                        <span>Locality / Landmark</span>
                        <input type="text" name="locality" placeholder="Locality / landmark">
                    </label>

                    <label>
                        <span>City</span>
                        <input type="text" name="city" placeholder="City" required>
                    </label>

                    <label>
                        <span>State</span>
                        <input type="text" name="state" placeholder="State" required>
                    </label>

                    <label>
                        <span>Country</span>
                        <input type="text" name="country" value="India" required>
                    </label>

                    <label>
                        <span>Address Type</span>
                        <select name="addressType">
                            <option value="HOME">Home</option>
                            <option value="WORK">Work</option>
                            <option value="OTHER">Other</option>
                        </select>
                    </label>

                    <label class="profile-checkbox full-width">
                        <input type="checkbox" name="defaultAddress">
                        <span>Make this my default address</span>
                    </label>

                    <button type="submit">SAVE ADDRESS</button>
                </form>
            </div>
        </section>
    </section>
</main>

<script src="${contextPath}/assets/js/common/toast.js"></script>
</body>
</html>