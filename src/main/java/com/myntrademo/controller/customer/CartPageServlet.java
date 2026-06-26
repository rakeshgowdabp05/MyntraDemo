package com.myntrademo.controller.customer;

import com.myntrademo.constant.AttributeConstants;
import com.myntrademo.constant.CartExtraChargeConstants;
import com.myntrademo.constant.CatalogConstants;
import com.myntrademo.constant.CheckoutAttributeConstants;
import com.myntrademo.constant.MessageConstants;
import com.myntrademo.constant.ViewConstants;
import com.myntrademo.dto.cart.CartPageDto;
import com.myntrademo.service.CartService;
import com.myntrademo.service.impl.CartServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

@WebServlet("/cart")
public class CartPageServlet extends HttpServlet {

    private final CartService cartService = new CartServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        moveFlashMessagesToRequest(request);

        Long userId = getAuthenticatedUserId(request);
        Long appliedCouponId = getAppliedCouponId(request);

        try {
            CartPageDto cartPage = cartService.getCartPage(userId, appliedCouponId);

            if (appliedCouponId != null && !cartPage.isAppliedCouponPresent()) {
                removeAppliedCoupon(request);
            }

            applyGiftAndDonationFromSession(request, cartPage);

            request.setAttribute(AttributeConstants.CART_PAGE, cartPage);
            request.setAttribute(CheckoutAttributeConstants.CART_ADDRESS, cartPage.getDefaultAddress());
            request.setAttribute(CheckoutAttributeConstants.AVAILABLE_COUPONS, cartPage.getAvailableCoupons());
            request.setAttribute(CheckoutAttributeConstants.DONATION_OPTIONS, CartExtraChargeConstants.DONATION_OPTIONS);
            request.setAttribute(CheckoutAttributeConstants.GIFT_PACKAGE_FEE, CartExtraChargeConstants.GIFT_PACKAGE_FEE);
            request.setAttribute(AttributeConstants.CURRENCY_SYMBOL, CatalogConstants.DEFAULT_CURRENCY_SYMBOL);

            request.getRequestDispatcher(ViewConstants.CART_VIEW).forward(request, response);

        } catch (IllegalArgumentException exception) {
            request.getSession().setAttribute(AttributeConstants.ERROR_MESSAGE, exception.getMessage());
            response.sendRedirect(request.getContextPath() + "/login");

        } catch (SQLException exception) {
            request.setAttribute(AttributeConstants.ERROR_MESSAGE, MessageConstants.SERVER_ERROR);
            request.getRequestDispatcher(ViewConstants.CART_VIEW).forward(request, response);
        }
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

    private Long getAppliedCouponId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            return null;
        }

        Object couponId = session.getAttribute(CheckoutAttributeConstants.APPLIED_COUPON_ID);

        if (couponId instanceof Long value) {
            return value;
        }

        if (couponId instanceof Integer value) {
            return value.longValue();
        }

        if (couponId instanceof String value) {
            try {
                return Long.parseLong(value);
            } catch (NumberFormatException exception) {
                return null;
            }
        }

        return null;
    }

    private void removeAppliedCoupon(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.removeAttribute(CheckoutAttributeConstants.APPLIED_COUPON_ID);
        }
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