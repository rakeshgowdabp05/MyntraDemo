package com.myntrademo.controller.customer;

import com.myntrademo.constant.AttributeConstants;
import com.myntrademo.constant.MessageConstants;
import com.myntrademo.dto.cart.CartAddressDto;
import com.myntrademo.service.AddressService;
import com.myntrademo.service.impl.AddressServiceImpl;
import com.myntrademo.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/address/add")
public class AddAddressServlet extends HttpServlet {

    private final AddressService addressService = new AddressServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Long userId = getAuthenticatedUserId(request);

        try {
            addressService.addAddress(userId, buildAddress(request));
            request.getSession().setAttribute(AttributeConstants.SUCCESS_MESSAGE, MessageConstants.ADDRESS_ADDED);

        } catch (IllegalArgumentException exception) {
            request.getSession().setAttribute(AttributeConstants.ERROR_MESSAGE, exception.getMessage());

        } catch (SQLException exception) {
            request.getSession().setAttribute(AttributeConstants.ERROR_MESSAGE, MessageConstants.SERVER_ERROR);
        }

        response.sendRedirect(request.getContextPath() + "/address");
    }

    private CartAddressDto buildAddress(HttpServletRequest request) {
        CartAddressDto address = new CartAddressDto();

        address.setFullName(request.getParameter("fullName"));
        address.setPhone(request.getParameter("phone"));
        address.setPincode(request.getParameter("pincode"));
        address.setAddressLine(request.getParameter("addressLine"));
        address.setLocality(request.getParameter("locality"));
        address.setCity(request.getParameter("city"));
        address.setState(request.getParameter("state"));
        address.setCountry(request.getParameter("country"));
        address.setAddressType(request.getParameter("addressType"));
        address.setDefaultAddress("on".equalsIgnoreCase(request.getParameter("defaultAddress")));

        if (ValidationUtil.isBlank(address.getCountry())) {
            address.setCountry("India");
        }

        if (ValidationUtil.isBlank(address.getAddressType())) {
            address.setAddressType("HOME");
        }

        return address;
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
}