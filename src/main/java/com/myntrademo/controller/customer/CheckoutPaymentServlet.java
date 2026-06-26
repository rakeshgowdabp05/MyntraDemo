package com.myntrademo.controller.customer;

import com.myntrademo.constant.AttributeConstants;
import com.myntrademo.constant.CartExtraChargeConstants;
import com.myntrademo.constant.CatalogConstants;
import com.myntrademo.constant.CheckoutAttributeConstants;
import com.myntrademo.constant.CheckoutViewConstants;
import com.myntrademo.constant.MessageConstants;
import com.myntrademo.dto.cart.CartPageDto;
import com.myntrademo.dto.checkout.GiftCardDto;
import com.myntrademo.dto.checkout.PaymentMethodDto;
import com.myntrademo.dto.checkout.PaymentOptionDto;
import com.myntrademo.service.CartService;
import com.myntrademo.service.GiftCardService;
import com.myntrademo.service.PaymentMethodService;
import com.myntrademo.service.impl.CartServiceImpl;
import com.myntrademo.service.impl.GiftCardServiceImpl;
import com.myntrademo.service.impl.PaymentMethodServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/checkout/payment")
public class CheckoutPaymentServlet extends HttpServlet {

    private final CartService cartService = new CartServiceImpl();
    private final PaymentMethodService paymentMethodService = new PaymentMethodServiceImpl();
    private final GiftCardService giftCardService = new GiftCardServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        moveFlashMessagesToRequest(request);

        Long userId = getAuthenticatedUserId(request);

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Long selectedAddressId = getSessionLong(request, CheckoutAttributeConstants.SELECTED_CHECKOUT_ADDRESS_ID);

        if (selectedAddressId == null) {
            request.getSession().setAttribute(
                    AttributeConstants.ERROR_MESSAGE,
                    MessageConstants.CHECKOUT_ADDRESS_REQUIRED
            );
            response.sendRedirect(request.getContextPath() + "/checkout/address");
            return;
        }

        try {
            Long appliedCouponId = getSessionLong(request, CheckoutAttributeConstants.APPLIED_COUPON_ID);
            CartPageDto cartPage = cartService.getCartPage(userId, appliedCouponId);

            if (cartPage == null || cartPage.isEmpty()) {
                request.getSession().setAttribute(
                        AttributeConstants.ERROR_MESSAGE,
                        MessageConstants.CART_EMPTY_FOR_CHECKOUT
                );
                response.sendRedirect(request.getContextPath() + "/cart");
                return;
            }

            applyGiftAndDonationFromSession(request, cartPage);

            List<PaymentMethodDto> paymentMethods = paymentMethodService.getPaymentMethods();
            String selectedMethodCode = getSessionString(request, CheckoutAttributeConstants.SELECTED_PAYMENT_METHOD);
            PaymentMethodDto selectedPaymentMethod = paymentMethodService.findSelectedMethod(paymentMethods, selectedMethodCode);

            if (selectedPaymentMethod == null) {
                request.setAttribute(AttributeConstants.ERROR_MESSAGE, MessageConstants.SERVER_ERROR);
                request.getRequestDispatcher(CheckoutViewConstants.CHECKOUT_PAYMENT_VIEW).forward(request, response);
                return;
            }

            request.getSession().setAttribute(
                    CheckoutAttributeConstants.SELECTED_PAYMENT_METHOD,
                    selectedPaymentMethod.getCode()
            );

            String selectedOptionCode = getSessionString(request, CheckoutAttributeConstants.SELECTED_PAYMENT_OPTION_CODE);
            PaymentOptionDto selectedOption = paymentMethodService.findSelectedOption(
                    selectedPaymentMethod.getCode(),
                    selectedOptionCode
            );

            BigDecimal amountBeforeGiftCard = cartPage.getPayableAmount().add(selectedPaymentMethod.getFeeAmount());
            String appliedGiftCardCode = getSessionString(request, CheckoutAttributeConstants.APPLIED_GIFT_CARD_CODE);
            BigDecimal giftCardDiscount = resolveGiftCardDiscount(request, appliedGiftCardCode, amountBeforeGiftCard);
            BigDecimal paymentPayableAmount = amountBeforeGiftCard.subtract(giftCardDiscount);

            if (paymentPayableAmount.compareTo(BigDecimal.ZERO) < 0) {
                paymentPayableAmount = BigDecimal.ZERO;
            }

            request.setAttribute(AttributeConstants.CART_PAGE, cartPage);
            request.setAttribute(CheckoutAttributeConstants.PAYMENT_METHODS, paymentMethods);
            request.setAttribute(CheckoutAttributeConstants.SELECTED_PAYMENT_METHOD_DTO, selectedPaymentMethod);
            request.setAttribute(CheckoutAttributeConstants.SELECTED_PAYMENT_METHOD, selectedPaymentMethod.getCode());
            request.setAttribute(CheckoutAttributeConstants.SELECTED_PAYMENT_OPTION_DTO, selectedOption);
            request.setAttribute(CheckoutAttributeConstants.SELECTED_PAYMENT_OPTION_CODE, selectedOptionCode);
            request.setAttribute(CheckoutAttributeConstants.APPLIED_GIFT_CARD_CODE, appliedGiftCardCode);
            request.setAttribute(CheckoutAttributeConstants.APPLIED_GIFT_CARD_DISCOUNT, giftCardDiscount);
            request.setAttribute("amountBeforeGiftCard", amountBeforeGiftCard);
            request.setAttribute("paymentPayableAmount", paymentPayableAmount);
            request.setAttribute(AttributeConstants.CURRENCY_SYMBOL, CatalogConstants.DEFAULT_CURRENCY_SYMBOL);

            request.getRequestDispatcher(CheckoutViewConstants.CHECKOUT_PAYMENT_VIEW).forward(request, response);

        } catch (SQLException exception) {
            request.setAttribute(AttributeConstants.ERROR_MESSAGE, MessageConstants.SERVER_ERROR);
            request.getRequestDispatcher(CheckoutViewConstants.CHECKOUT_PAYMENT_VIEW).forward(request, response);
        }
    }

    private BigDecimal resolveGiftCardDiscount(
            HttpServletRequest request,
            String appliedGiftCardCode,
            BigDecimal amountBeforeGiftCard
    ) throws SQLException {
        if (appliedGiftCardCode == null || appliedGiftCardCode.isBlank()) {
            return BigDecimal.ZERO;
        }

        GiftCardDto giftCard = giftCardService.findUsableGiftCard(appliedGiftCardCode, amountBeforeGiftCard);

        if (giftCard == null) {
            request.getSession().removeAttribute(CheckoutAttributeConstants.APPLIED_GIFT_CARD_CODE);
            request.getSession().removeAttribute(CheckoutAttributeConstants.APPLIED_GIFT_CARD_DISCOUNT);
            return BigDecimal.ZERO;
        }

        BigDecimal discount = giftCardService.calculateDiscount(giftCard, amountBeforeGiftCard);
        request.getSession().setAttribute(CheckoutAttributeConstants.APPLIED_GIFT_CARD_DISCOUNT, discount);
        return discount;
    }

    private void applyGiftAndDonationFromSession(HttpServletRequest request, CartPageDto cartPage) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            return;
        }

        Object giftEnabled = session.getAttribute(CheckoutAttributeConstants.GIFT_PACKAGE_ENABLED);

        if (giftEnabled instanceof Boolean enabled && enabled) {
            cartPage.setGiftPackageFee(CartExtraChargeConstants.GIFT_PACKAGE_FEE);
            cartPage.setGiftPackageEnabled(true);
        }

        Object donationAmount = session.getAttribute(CheckoutAttributeConstants.DONATION_AMOUNT);

        if (donationAmount instanceof BigDecimal amount) {
            cartPage.setDonationAmount(amount);
        }
    }

    private Long getAuthenticatedUserId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            return null;
        }

        Object userId = session.getAttribute(AttributeConstants.AUTH_USER_ID);

        if (userId instanceof Long value) {
            return value;
        }

        if (userId instanceof Integer value) {
            return value.longValue();
        }

        return null;
    }

    private Long getSessionLong(HttpServletRequest request, String attributeName) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            return null;
        }

        Object value = session.getAttribute(attributeName);

        if (value instanceof Long longValue) {
            return longValue;
        }

        if (value instanceof Integer integerValue) {
            return integerValue.longValue();
        }

        if (value instanceof String stringValue) {
            try {
                return Long.parseLong(stringValue);
            } catch (NumberFormatException exception) {
                return null;
            }
        }

        return null;
    }

    private String getSessionString(HttpServletRequest request, String attributeName) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            return null;
        }

        Object value = session.getAttribute(attributeName);

        if (value instanceof String text) {
            return text;
        }

        return null;
    }

    private void moveFlashMessagesToRequest(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            return;
        }

        moveSessionAttributeToRequest(session, request, AttributeConstants.SUCCESS_MESSAGE);
        moveSessionAttributeToRequest(session, request, AttributeConstants.ERROR_MESSAGE);
    }

    private void moveSessionAttributeToRequest(
            HttpSession session,
            HttpServletRequest request,
            String attributeName
    ) {
        Object value = session.getAttribute(attributeName);

        if (value != null) {
            request.setAttribute(attributeName, value);
            session.removeAttribute(attributeName);
        }
    }
}