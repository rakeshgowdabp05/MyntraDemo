<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="currency" value="${empty currencySymbol ? '₹' : currencySymbol}" />
<c:set var="paymentMethods" value="${requestScope.paymentMethods}" />
<c:set var="selectedPaymentMethod" value="${requestScope.selectedPaymentMethodDto}" />
<c:set var="selectedPaymentCode" value="${empty requestScope.selectedPaymentMethod ? 'CASH_ON_DELIVERY' : requestScope.selectedPaymentMethod}" />
<c:set var="selectedOptionCode" value="${requestScope.selectedPaymentOptionCode}" />
<c:set var="availableCoupons" value="${cartPage.availableCoupons}" />
<c:set var="amountBeforeGiftCard" value="${requestScope.amountBeforeGiftCard}" />
<c:set var="paymentPayableAmount" value="${requestScope.paymentPayableAmount}" />
<c:set var="appliedGiftCardCode" value="${requestScope.appliedGiftCardCode}" />
<c:set var="appliedGiftCardDiscount" value="${empty requestScope.appliedGiftCardDiscount ? 0 : requestScope.appliedGiftCardDiscount}" />
<c:set var="basePayableAfterGiftCard" value="${cartPage.payableAmount - appliedGiftCardDiscount}" />

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Payment - MyntraDemo</title>
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
        <span>ADDRESS</span>
        <i></i>
        <span class="is-active">PAYMENT</span>
    </nav>

    <div class="checkout-secure">
        <span></span>
        <strong>100% SECURE</strong>
    </div>
</header>

<main class="checkout-page" data-payment-page data-base-payable="${basePayableAfterGiftCard}">
    <section class="checkout-shell">
        <section class="checkout-left">

            <c:if test="${not empty availableCoupons}">
                <section class="payment-offers-box">
                    <div class="payment-offers-title">
                        <span class="payment-offer-icon" aria-hidden="true">✺</span>
                        <strong>Offers (${fn:length(availableCoupons)})</strong>
                    </div>

                    <div class="payment-offer-carousel" data-payment-offer-carousel>
                        <button type="button" class="payment-offer-arrow" data-payment-offer-prev aria-label="Previous offer">‹</button>

                        <div class="payment-offer-window">
                            <div class="payment-offer-track" data-payment-offer-track>
                                <c:forEach var="coupon" items="${availableCoupons}">
                                    <article class="payment-offer-card">
                                        <span class="payment-offer-logo">${fn:substring(coupon.couponCode, 0, 1)}</span>
                                        <div>
                                            <h2>${coupon.couponTitle}</h2>
                                            <p>${coupon.couponDescription}</p>
                                        </div>
                                    </article>
                                </c:forEach>
                            </div>
                        </div>

                        <button type="button" class="payment-offer-arrow" data-payment-offer-next aria-label="Next offer">›</button>
                    </div>

                    <div class="payment-offer-dots" aria-hidden="true">
                        <c:forEach var="coupon" items="${availableCoupons}" varStatus="status">
                            <button type="button" class="${status.first ? 'is-active' : ''}" data-payment-offer-dot="${status.index}"></button>
                        </c:forEach>
                    </div>
                </section>
            </c:if>

            <h1 class="payment-title">Choose Payment Mode</h1>

            <section class="payment-mode-shell">
                <aside class="payment-tabs">
                    <button type="button"
                            class="payment-tab payment-recommended-tab ${selectedPaymentCode eq 'CASH_ON_DELIVERY' ? 'is-active' : ''}"
                            data-payment-tab="CASH_ON_DELIVERY"
                            data-payment-fee="10">
                        <span class="payment-method-icon icon-recommended"></span>
                        <strong>Recommended</strong>
                    </button>

                    <c:forEach var="method" items="${paymentMethods}">
                        <button type="button"
                                class="payment-tab ${method.code eq selectedPaymentCode ? 'is-active' : ''}"
                                data-payment-tab="${method.code}"
                                data-payment-fee="${method.feeAmount}">
                            <span class="payment-method-icon icon-${method.cssClass}"></span>
                            <strong>${method.label}</strong>

                            <c:if test="${method.offerAvailable}">
                                <em>${method.tabOfferText}</em>
                            </c:if>
                        </button>
                    </c:forEach>
                </aside>

                <section class="payment-detail-panel">

                    <c:forEach var="method" items="${paymentMethods}">
                        <section class="payment-detail-panel-item ${method.code eq selectedPaymentCode ? 'is-active' : ''}"
                                 data-payment-panel="${method.code}">

                            <c:choose>
                                <c:when test="${method.code eq 'CARD'}">
                                    <h2>CREDIT/ DEBIT CARD</h2>

                                    <c:if test="${not empty availableCoupons}">
                                        <div class="payment-inline-offer">
                                            <c:forEach var="coupon" items="${availableCoupons}" begin="0" end="0">
                                                <span>${fn:substring(coupon.couponCode, 0, 1)}</span>
                                                <div>
                                                    <strong>${coupon.couponTitle}</strong>
                                                    <p>${coupon.couponDescription}</p>
                                                </div>
                                            </c:forEach>
                                        </div>
                                    </c:if>

                                    <p class="payment-small-info">
                                        Please ensure your card can be used for online transactions.
                                        <a href="${contextPath}/cart">Know More</a>
                                    </p>

                                    <div class="payment-input-grid">
                                        <label>
                                            <input type="text" placeholder="Card Number">
                                        </label>

                                        <div class="payment-card-row">
                                            <label>
                                                <input type="text" placeholder="Valid Thru (MM/YY)">
                                            </label>

                                            <label>
                                                <input type="password" placeholder="CVV">
                                            </label>
                                        </div>
                                    </div>

                                    <form method="post" action="${contextPath}/checkout/place-order" class="payment-place-order-form">
                                        <input type="hidden" name="paymentMethod" value="${method.code}">
                                        <input type="hidden" name="paymentOptionCode" value="">
                                        <button type="submit">Pay Now</button>
                                    </form>
                                </c:when>

                                <c:when test="${method.code eq 'UPI'}">
                                    <h2>Pay using UPI</h2>

                                    <div class="payment-option-list">
                                        <c:forEach var="option" items="${method.options}">
                                            <button type="button"
                                                    class="payment-option-row ${option.optionCode eq selectedOptionCode ? 'is-selected' : ''}"
                                                    data-payment-option
                                                    data-method-code="${method.code}"
                                                    data-option-code="${option.optionCode}"
                                                    data-option-label="${option.optionLabel}">
                                                <span class="payment-option-radio"></span>
                                                <span class="payment-option-logo">${option.logoText}</span>

                                                <span class="payment-option-copy">
                                                    <strong>${option.optionLabel}</strong>

                                                    <c:if test="${option.subtitleAvailable}">
                                                        <small>${option.optionSubtitle}</small>
                                                    </c:if>

                                                    <c:if test="${option.noticeAvailable}">
                                                        <em>${option.optionNotice}</em>
                                                    </c:if>
                                                </span>
                                            </button>
                                        </c:forEach>
                                    </div>

                                    <form method="post" action="${contextPath}/checkout/place-order" class="payment-place-order-form">
                                        <input type="hidden" name="paymentMethod" value="${method.code}">
                                        <input type="hidden" name="paymentOptionCode" data-payment-hidden-option="${method.code}" value="${selectedOptionCode}">
                                        <button type="submit">Pay Now</button>
                                    </form>
                                </c:when>

                                <c:when test="${method.code eq 'WALLET' or method.code eq 'PAY_LATER' or method.code eq 'EMI'}">
                                    <h2>${method.detailTitle}</h2>

                                    <div class="payment-option-list">
                                        <c:forEach var="option" items="${method.options}">
                                            <button type="button"
                                                    class="payment-option-row ${option.optionCode eq selectedOptionCode ? 'is-selected' : ''} ${option.disabled ? 'is-disabled' : ''}"
                                                    data-payment-option
                                                    data-method-code="${method.code}"
                                                    data-option-code="${option.optionCode}"
                                                    data-option-label="${option.optionLabel}"
                                                    <c:if test="${option.disabled}">disabled</c:if>>
                                                <span class="payment-option-radio"></span>
                                                <span class="payment-option-logo">${option.logoText}</span>

                                                <span class="payment-option-copy">
                                                    <strong>${option.optionLabel}</strong>

                                                    <c:if test="${option.subtitleAvailable}">
                                                        <small>${option.optionSubtitle}</small>
                                                    </c:if>

                                                    <c:if test="${option.noticeAvailable}">
                                                        <em>${option.optionNotice}</em>
                                                    </c:if>
                                                </span>
                                            </button>
                                        </c:forEach>
                                    </div>

                                    <form method="post" action="${contextPath}/checkout/place-order" class="payment-place-order-form">
                                        <input type="hidden" name="paymentMethod" value="${method.code}">
                                        <input type="hidden" name="paymentOptionCode" data-payment-hidden-option="${method.code}" value="${selectedOptionCode}">
                                        <button type="submit">Pay Now</button>
                                    </form>
                                </c:when>

                                <c:when test="${method.code eq 'NET_BANKING'}">
                                    <h2>Net Banking</h2>

                                    <div class="payment-option-list">
                                        <c:forEach var="option" items="${method.options}" varStatus="status">
                                            <c:if test="${status.index lt 5}">
                                                <button type="button"
                                                        class="payment-option-row ${option.optionCode eq selectedOptionCode ? 'is-selected' : ''} ${option.disabled ? 'is-disabled' : ''}"
                                                        data-payment-option
                                                        data-method-code="${method.code}"
                                                        data-option-code="${option.optionCode}"
                                                        data-option-label="${option.optionLabel}"
                                                        <c:if test="${option.disabled}">disabled</c:if>>
                                                    <span class="payment-option-radio"></span>
                                                    <span class="payment-option-logo">${option.logoText}</span>

                                                    <span class="payment-option-copy">
                                                        <strong>${option.optionLabel}</strong>

                                                        <c:if test="${option.noticeAvailable}">
                                                            <em>${option.optionNotice}</em>
                                                        </c:if>
                                                    </span>
                                                </button>
                                            </c:if>
                                        </c:forEach>

                                        <button type="button" class="other-bank-btn" data-bank-modal-open>
                                            Other Banks
                                            <span>⌄</span>
                                        </button>

                                        <div class="selected-bank-summary" data-selected-bank-summary>
                                            <span>Selected Bank</span>
                                            <strong data-selected-bank-name></strong>
                                        </div>
                                    </div>

                                    <form method="post" action="${contextPath}/checkout/place-order" class="payment-place-order-form">
                                        <input type="hidden" name="paymentMethod" value="${method.code}">
                                        <input type="hidden" name="paymentOptionCode" data-payment-hidden-option="${method.code}" value="${selectedOptionCode}">
                                        <button type="submit">
                                            Pay ${currency}<fmt:formatNumber value="${paymentPayableAmount}" maxFractionDigits="0" groupingUsed="false" />
                                        </button>
                                    </form>
                                </c:when>

                                <c:otherwise>
                                    <h2>
                                        <c:choose>
                                            <c:when test="${method.recommended}">
                                                Recommended Payment Options
                                            </c:when>
                                            <c:otherwise>
                                                ${method.detailTitle}
                                            </c:otherwise>
                                        </c:choose>
                                    </h2>

                                    <div class="payment-selected-option">
                                        <div class="payment-selected-radio"></div>

                                        <div class="payment-selected-copy">
                                            <h3>${method.detailTitle}</h3>
                                            <p>${method.detailDescription}</p>
                                        </div>

                                        <span class="payment-selected-icon">
                                            <span class="payment-method-icon icon-${method.cssClass}"></span>
                                        </span>
                                    </div>

                                    <form method="post" action="${contextPath}/checkout/place-order" class="payment-place-order-form">
                                        <input type="hidden" name="paymentMethod" value="${method.code}">
                                        <input type="hidden" name="paymentOptionCode" value="">
                                        <button type="submit">
                                            <c:choose>
                                                <c:when test="${method.code eq 'CASH_ON_DELIVERY'}">
                                                    Place Order
                                                </c:when>
                                                <c:otherwise>
                                                    Pay ${currency}<fmt:formatNumber value="${paymentPayableAmount}" maxFractionDigits="0" groupingUsed="false" />
                                                </c:otherwise>
                                            </c:choose>
                                        </button>
                                    </form>
                                </c:otherwise>
                            </c:choose>
                        </section>
                    </c:forEach>
                </section>
            </section>

            <section class="gift-card-row">
                <div>
                    <span>🎁</span>
                    <strong>Have a Gift Card?</strong>
                </div>

                <c:choose>
                    <c:when test="${not empty appliedGiftCardCode}">
                        <div class="applied-gift-card">
                            <strong>${appliedGiftCardCode}</strong>

                            <form method="post" action="${contextPath}/checkout/gift-card/remove">
                                <button type="submit">REMOVE</button>
                            </form>
                        </div>
                    </c:when>

                    <c:otherwise>
                        <button type="button" data-gift-card-modal-open>APPLY GIFT CARD</button>
                    </c:otherwise>
                </c:choose>
            </section>
        </section>

        <aside class="checkout-right">
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

                <div class="checkout-price-line" data-payment-fee-row>
                    <span>
                        <span data-payment-fee-label>${selectedPaymentMethod.label}</span> Fee
                        <a href="${contextPath}/cart">Know More</a>
                    </span>
                    <strong>
                        ${currency}<span data-payment-fee-value><fmt:formatNumber value="${selectedPaymentMethod.feeAmount}" maxFractionDigits="0" groupingUsed="false" /></span>
                    </strong>
                </div>

                <c:if test="${not empty appliedGiftCardCode and appliedGiftCardDiscount gt 0}">
                    <div class="checkout-price-line">
                        <span>Gift Card Discount</span>
                        <strong class="checkout-green">
                            - ${currency}<fmt:formatNumber value="${appliedGiftCardDiscount}" maxFractionDigits="0" groupingUsed="false" />
                        </strong>
                    </div>
                </c:if>

                <div class="checkout-total-line">
                    <span>Total Amount</span>
                    <strong>
                        ${currency}<span data-payment-total-amount><fmt:formatNumber value="${paymentPayableAmount}" maxFractionDigits="0" groupingUsed="false" /></span>
                    </strong>
                </div>

                <p class="payment-terms">
                    By placing the order, you agree to MyntraDemo's
                    <a href="${contextPath}/terms">Terms of Use</a>
                    and
                    <a href="${contextPath}/privacy">Privacy Policy</a>
                </p>
            </section>
        </aside>
    </section>
</main>

<section class="bank-modal" data-bank-modal aria-hidden="true">
    <div class="bank-modal-backdrop" data-bank-modal-close></div>

    <div class="bank-modal-panel">
        <header>
            <h2>Select Bank</h2>
            <button type="button" data-bank-modal-close aria-label="Close bank list"></button>
        </header>

        <div class="bank-modal-list">
            <c:forEach var="method" items="${paymentMethods}">
                <c:if test="${method.code eq 'NET_BANKING'}">
                    <c:forEach var="option" items="${method.options}">
                        <button type="button"
                                class="${option.disabled ? 'is-disabled' : ''}"
                                data-bank-option
                                data-method-code="NET_BANKING"
                                data-option-code="${option.optionCode}"
                                data-option-label="${option.optionLabel}"
                                <c:if test="${option.disabled}">disabled</c:if>>
                            <span class="bank-icon"></span>
                            <strong>${option.optionLabel}</strong>
                        </button>
                    </c:forEach>
                </c:if>
            </c:forEach>
        </div>
    </div>
</section>

<section class="gift-card-modal" data-gift-card-modal aria-hidden="true">
    <div class="gift-card-modal-backdrop" data-gift-card-modal-close></div>

    <div class="gift-card-modal-panel">
        <header>
            <h2>Apply Gift Card</h2>
            <button type="button" data-gift-card-modal-close aria-label="Close gift card"></button>
        </header>

        <form method="post" action="${contextPath}/checkout/gift-card/apply" class="gift-card-form">
            <input type="hidden" name="orderAmount" value="${amountBeforeGiftCard}">

            <label>
                Gift Card Code
                <input type="text" name="giftCardCode" placeholder="Enter gift card code" required>
            </label>

            <button type="submit">APPLY GIFT CARD</button>
        </form>
    </div>
</section>

<footer class="checkout-payment-footer">
    <div class="payment-icons">
        <span class="payment-badge ssl">
            <svg viewBox="0 0 24 24">
                <path d="M6 10V8a6 6 0 1 1 12 0v2h1.2A1.8 1.8 0 0 1 21 11.8v7.4a1.8 1.8 0 0 1-1.8 1.8H4.8A1.8 1.8 0 0 1 3 19.2v-7.4A1.8 1.8 0 0 1 4.8 10H6Zm2 0h8V8a4 4 0 1 0-8 0v2Z"/>
            </svg>
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
<script src="${contextPath}/assets/js/checkout/checkout-payment.js"></script>
</body>
</html>