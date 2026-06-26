package com.myntrademo.controller.customer;

import com.myntrademo.constant.AttributeConstants;
import com.myntrademo.constant.CheckoutAttributeConstants;
import com.myntrademo.constant.MessageConstants;
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

@WebServlet(urlPatterns = {"/address", "/addresses"})
public class AddressPageServlet extends HttpServlet {

    private static final String ADDRESS_VIEW = "/WEB-INF/views/customer/address.jsp";

    private final AddressService addressService = new AddressServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        moveFlashMessagesToRequest(request);

        Long userId = getAuthenticatedUserId(request);

        try {
            request.setAttribute(CheckoutAttributeConstants.ADDRESSES, addressService.getAddresses(userId));
            request.setAttribute(
                    CheckoutAttributeConstants.DEFAULT_ADDRESS,
                    addressService.getDefaultAddress(userId).orElse(null)
            );
            request.setAttribute("activeAccountMenu", "addresses");

            request.getRequestDispatcher(ADDRESS_VIEW).forward(request, response);

        } catch (IllegalArgumentException exception) {
            request.getSession().setAttribute(AttributeConstants.ERROR_MESSAGE, exception.getMessage());
            response.sendRedirect(request.getContextPath() + "/login");

        } catch (SQLException exception) {
            request.setAttribute(AttributeConstants.ERROR_MESSAGE, MessageConstants.SERVER_ERROR);
            request.getRequestDispatcher(ADDRESS_VIEW).forward(request, response);
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