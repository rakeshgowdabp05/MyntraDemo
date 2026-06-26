package com.myntrademo.controller.customer;

import com.myntrademo.constant.AttributeConstants;
import com.myntrademo.constant.CartExtraChargeConstants;
import com.myntrademo.constant.CatalogConstants;
import com.myntrademo.constant.CheckoutAttributeConstants;
import com.myntrademo.constant.CheckoutViewConstants;
import com.myntrademo.constant.MessageConstants;
import com.myntrademo.dto.cart.CartAddressDto;
import com.myntrademo.dto.cart.CartPageDto;
import com.myntrademo.service.AddressService;
import com.myntrademo.service.CartService;
import com.myntrademo.service.impl.AddressServiceImpl;
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
import java.util.List;

@WebServlet("/checkout/address")
public class CheckoutAddressServlet extends HttpServlet {

    private final CartService cartService = new CartServiceImpl();
    private final AddressService addressService = new AddressServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        moveFlashMessagesToRequest(request);

        Long userId = getAuthenticatedUserId(request);

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
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

            List<CartAddressDto> addresses = addressService.getAddresses(userId);
            Long selectedAddressId = resolveSelectedAddressId(request, cartPage, addresses);

            request.setAttribute(AttributeConstants.CART_PAGE, cartPage);
            request.setAttribute(CheckoutAttributeConstants.ADDRESSES, addresses);
            request.setAttribute(CheckoutAttributeConstants.SELECTED_CHECKOUT_ADDRESS_ID, selectedAddressId);
            request.setAttribute(CheckoutAttributeConstants.DONATION_OPTIONS, CartExtraChargeConstants.DONATION_OPTIONS);
            request.setAttribute(CheckoutAttributeConstants.GIFT_PACKAGE_FEE, CartExtraChargeConstants.GIFT_PACKAGE_FEE);
            request.setAttribute(AttributeConstants.CURRENCY_SYMBOL, CatalogConstants.DEFAULT_CURRENCY_SYMBOL);

            request.getRequestDispatcher(CheckoutViewConstants.CHECKOUT_ADDRESS_VIEW).forward(request, response);

        } catch (SQLException exception) {
            request.setAttribute(AttributeConstants.ERROR_MESSAGE, MessageConstants.SERVER_ERROR);
            request.getRequestDispatcher(CheckoutViewConstants.CHECKOUT_ADDRESS_VIEW).forward(request, response);
        }
    }

    private Long resolveSelectedAddressId(
            HttpServletRequest request,
            CartPageDto cartPage,
            List<CartAddressDto> addresses
    ) {
        Long selectedAddressId = getSessionLong(request, CheckoutAttributeConstants.SELECTED_CHECKOUT_ADDRESS_ID);

        if (isAddressPresent(addresses, selectedAddressId)) {
            return selectedAddressId;
        }

        CartAddressDto defaultAddress = cartPage.getDefaultAddress();

        if (defaultAddress != null && defaultAddress.getAddressId() != null) {
            selectedAddressId = defaultAddress.getAddressId();
            request.getSession().setAttribute(
                    CheckoutAttributeConstants.SELECTED_CHECKOUT_ADDRESS_ID,
                    selectedAddressId
            );
            return selectedAddressId;
        }

        if (addresses != null && !addresses.isEmpty()) {
            selectedAddressId = addresses.get(0).getAddressId();
            request.getSession().setAttribute(
                    CheckoutAttributeConstants.SELECTED_CHECKOUT_ADDRESS_ID,
                    selectedAddressId
            );
            return selectedAddressId;
        }

        request.getSession().removeAttribute(CheckoutAttributeConstants.SELECTED_CHECKOUT_ADDRESS_ID);
        return null;
    }

    private boolean isAddressPresent(List<CartAddressDto> addresses, Long selectedAddressId) {
        if (addresses == null || selectedAddressId == null) {
            return false;
        }

        for (CartAddressDto address : addresses) {
            if (address.getAddressId() != null && address.getAddressId().equals(selectedAddressId)) {
                return true;
            }
        }

        return false;
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