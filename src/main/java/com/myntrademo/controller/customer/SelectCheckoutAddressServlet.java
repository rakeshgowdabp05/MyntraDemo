package com.myntrademo.controller.customer;

import com.myntrademo.constant.AttributeConstants;
import com.myntrademo.constant.CheckoutAttributeConstants;
import com.myntrademo.constant.MessageConstants;
import com.myntrademo.dto.cart.CartAddressDto;
import com.myntrademo.service.AddressService;
import com.myntrademo.service.impl.AddressServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/checkout/address/select")
public class SelectCheckoutAddressServlet extends HttpServlet {

    private final AddressService addressService = new AddressServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Long userId = getAuthenticatedUserId(request);

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Long addressId = parseLong(request.getParameter("addressId"));

        if (addressId == null) {
            request.getSession().setAttribute(
                    AttributeConstants.ERROR_MESSAGE,
                    MessageConstants.CHECKOUT_ADDRESS_REQUIRED
            );
            response.sendRedirect(request.getContextPath() + "/checkout/address");
            return;
        }

        try {
            if (!isAddressOwnedByUser(userId, addressId)) {
                request.getSession().setAttribute(
                        AttributeConstants.ERROR_MESSAGE,
                        MessageConstants.CHECKOUT_ADDRESS_REQUIRED
                );
                response.sendRedirect(request.getContextPath() + "/checkout/address");
                return;
            }

            request.getSession().setAttribute(
                    CheckoutAttributeConstants.SELECTED_CHECKOUT_ADDRESS_ID,
                    addressId
            );
            request.getSession().setAttribute(
                    AttributeConstants.SUCCESS_MESSAGE,
                    MessageConstants.CHECKOUT_ADDRESS_SELECTED
            );

            response.sendRedirect(request.getContextPath() + "/checkout/address");

        } catch (SQLException exception) {
            request.getSession().setAttribute(AttributeConstants.ERROR_MESSAGE, MessageConstants.SERVER_ERROR);
            response.sendRedirect(request.getContextPath() + "/checkout/address");
        }
    }

    private boolean isAddressOwnedByUser(Long userId, Long addressId) throws SQLException {
        List<CartAddressDto> addresses = addressService.getAddresses(userId);

        for (CartAddressDto address : addresses) {
            if (address.getAddressId() != null && address.getAddressId().equals(addressId)) {
                return true;
            }
        }

        return false;
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

    private Long parseLong(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return Long.parseLong(value);
        } catch (NumberFormatException exception) {
            return null;
        }
    }
}