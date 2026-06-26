package com.myntrademo.controller.customer;

import com.myntrademo.constant.AttributeConstants;
import com.myntrademo.constant.CartExtraChargeConstants;
import com.myntrademo.constant.CheckoutAttributeConstants;
import com.myntrademo.constant.MessageConstants;
import com.myntrademo.dto.cart.CartAddressDto;
import com.myntrademo.dto.cart.CartPageDto;
import com.myntrademo.dto.checkout.PaymentMethodDto;
import com.myntrademo.dto.checkout.PlaceOrderRequest;
import com.myntrademo.dto.checkout.PlacedOrderDto;
import com.myntrademo.service.AddressService;
import com.myntrademo.service.CartService;
import com.myntrademo.service.CheckoutOrderService;
import com.myntrademo.service.GiftCardService;
import com.myntrademo.service.PaymentMethodService;
import com.myntrademo.service.impl.AddressServiceImpl;
import com.myntrademo.service.impl.CartServiceImpl;
import com.myntrademo.service.impl.CheckoutOrderServiceImpl;
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

@WebServlet("/checkout/place-order")
public class PlaceOrderServlet extends HttpServlet {

    private final CartService cartService = new CartServiceImpl();
    private final AddressService addressService = new AddressServiceImpl();
    private final PaymentMethodService paymentMethodService = new PaymentMethodServiceImpl();
    private final GiftCardService giftCardService = new GiftCardServiceImpl();
    private final CheckoutOrderService checkoutOrderService = new CheckoutOrderServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Long userId = getAuthenticatedUserId(request);

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            Long selectedAddressId = getSessionLong(request, CheckoutAttributeConstants.SELECTED_CHECKOUT_ADDRESS_ID);

            if (selectedAddressId == null) {
                request.getSession().setAttribute(AttributeConstants.ERROR_MESSAGE, MessageConstants.CHECKOUT_ADDRESS_REQUIRED);
                response.sendRedirect(request.getContextPath() + "/checkout/address");
                return;
            }

            String paymentMethodCode = request.getParameter("paymentMethod");
            String paymentOptionCode = request.getParameter("paymentOptionCode");

            if (!paymentMethodService.isValidPaymentMethod(paymentMethodCode)) {
                request.getSession().setAttribute(AttributeConstants.ERROR_MESSAGE, MessageConstants.PAYMENT_METHOD_REQUIRED);
                response.sendRedirect(request.getContextPath() + "/checkout/payment");
                return;
            }

            CartAddressDto selectedAddress = findAddress(userId, selectedAddressId);

            if (selectedAddress == null) {
                request.getSession().setAttribute(AttributeConstants.ERROR_MESSAGE, MessageConstants.CHECKOUT_ADDRESS_REQUIRED);
                response.sendRedirect(request.getContextPath() + "/checkout/address");
                return;
            }

            Long appliedCouponId = getSessionLong(request, CheckoutAttributeConstants.APPLIED_COUPON_ID);
            CartPageDto cartPage = cartService.getCartPage(userId, appliedCouponId);

            if (cartPage == null || cartPage.isEmpty()) {
                request.getSession().setAttribute(AttributeConstants.ERROR_MESSAGE, MessageConstants.CART_EMPTY_FOR_CHECKOUT);
                response.sendRedirect(request.getContextPath() + "/cart");
                return;
            }

            applyGiftAndDonationFromSession(request, cartPage);

            PaymentMethodDto paymentMethod = resolvePaymentMethod(paymentMethodCode);
            BigDecimal paymentFee = paymentMethod == null ? BigDecimal.ZERO : paymentMethod.getFeeAmount();

            String giftCardCode = getSessionString(request, CheckoutAttributeConstants.APPLIED_GIFT_CARD_CODE);
            BigDecimal giftCardDiscount = resolveGiftCardDiscount(request, giftCardCode, cartPage.getPayableAmount().add(paymentFee));

            PlaceOrderRequest placeOrderRequest = new PlaceOrderRequest();
            placeOrderRequest.setUserId(userId);
            placeOrderRequest.setCartPage(cartPage);
            placeOrderRequest.setDeliveryAddress(selectedAddress);
            placeOrderRequest.setPaymentMethodCode(paymentMethodCode);
            placeOrderRequest.setPaymentOptionCode(paymentOptionCode);
            placeOrderRequest.setPaymentFee(paymentFee);
            placeOrderRequest.setGiftCardCode(giftCardCode);
            placeOrderRequest.setGiftCardDiscount(giftCardDiscount);

            PlacedOrderDto placedOrder = checkoutOrderService.placeOrder(placeOrderRequest);
            clearCheckoutSession(request.getSession());

            request.getSession().setAttribute(AttributeConstants.SUCCESS_MESSAGE, MessageConstants.ORDER_PLACED_SUCCESSFULLY);
            response.sendRedirect(request.getContextPath() + "/order/success?id=" + placedOrder.getOrderId());

        } catch (IllegalArgumentException exception) {
            request.getSession().setAttribute(AttributeConstants.ERROR_MESSAGE, MessageConstants.ORDER_PLACE_FAILED);
            response.sendRedirect(request.getContextPath() + "/checkout/payment");

        } catch (SQLException exception) {
            request.getSession().setAttribute(AttributeConstants.ERROR_MESSAGE, MessageConstants.SERVER_ERROR);
            response.sendRedirect(request.getContextPath() + "/checkout/payment");
        }
    }

    private CartAddressDto findAddress(Long userId, Long selectedAddressId) throws SQLException {
        List<CartAddressDto> addresses = addressService.getAddresses(userId);

        for (CartAddressDto address : addresses) {
            if (address.getAddressId() != null && address.getAddressId().equals(selectedAddressId)) {
                return address;
            }
        }

        return null;
    }

    private PaymentMethodDto resolvePaymentMethod(String paymentMethodCode) throws SQLException {
        List<PaymentMethodDto> methods = paymentMethodService.getPaymentMethods();

        for (PaymentMethodDto method : methods) {
            if (method.getCode().equals(paymentMethodCode)) {
                return method;
            }
        }

        return null;
    }

    private BigDecimal resolveGiftCardDiscount(
            HttpServletRequest request,
            String giftCardCode,
            BigDecimal orderAmount
    ) throws SQLException {
        Object sessionDiscount = request.getSession().getAttribute(CheckoutAttributeConstants.APPLIED_GIFT_CARD_DISCOUNT);

        if (giftCardCode == null || giftCardCode.isBlank()) {
            return BigDecimal.ZERO;
        }

        if (sessionDiscount instanceof BigDecimal amount) {
            return amount;
        }

        return giftCardService.calculateDiscount(
                giftCardService.findUsableGiftCard(giftCardCode, orderAmount),
                orderAmount
        );
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

    private void clearCheckoutSession(HttpSession session) {
        session.removeAttribute(CheckoutAttributeConstants.SELECTED_CHECKOUT_ADDRESS_ID);
        session.removeAttribute(CheckoutAttributeConstants.SELECTED_PAYMENT_METHOD);
        session.removeAttribute(CheckoutAttributeConstants.SELECTED_PAYMENT_OPTION_CODE);
        session.removeAttribute(CheckoutAttributeConstants.APPLIED_COUPON_ID);
        session.removeAttribute(CheckoutAttributeConstants.GIFT_PACKAGE_ENABLED);
        session.removeAttribute(CheckoutAttributeConstants.GIFT_PACKAGE_FEE);
        session.removeAttribute(CheckoutAttributeConstants.GIFT_RECIPIENT_NAME);
        session.removeAttribute(CheckoutAttributeConstants.GIFT_MESSAGE);
        session.removeAttribute(CheckoutAttributeConstants.GIFT_SENDER_NAME);
        session.removeAttribute(CheckoutAttributeConstants.DONATION_AMOUNT);
        session.removeAttribute(CheckoutAttributeConstants.APPLIED_GIFT_CARD_CODE);
        session.removeAttribute(CheckoutAttributeConstants.APPLIED_GIFT_CARD_DISCOUNT);
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
        Object value = request.getSession().getAttribute(attributeName);

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
        Object value = request.getSession().getAttribute(attributeName);

        if (value instanceof String text) {
            return text;
        }

        return null;
    }
}