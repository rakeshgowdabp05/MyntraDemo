package com.myntrademo.controller.customer;

import com.myntrademo.constant.AttributeConstants;
import com.myntrademo.constant.CatalogConstants;
import com.myntrademo.constant.CheckoutViewConstants;
import com.myntrademo.constant.MessageConstants;
import com.myntrademo.dto.checkout.PlacedOrderDto;
import com.myntrademo.service.CheckoutOrderService;
import com.myntrademo.service.impl.CheckoutOrderServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/order/success")
public class OrderSuccessServlet extends HttpServlet {

    private final CheckoutOrderService checkoutOrderService = new CheckoutOrderServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        moveFlashMessagesToRequest(request);

        Long userId = getAuthenticatedUserId(request);
        Long orderId = parseLong(request.getParameter("id"));

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if (orderId == null) {
            request.getSession().setAttribute(AttributeConstants.ERROR_MESSAGE, MessageConstants.ORDER_NOT_FOUND);
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }

        try {
            PlacedOrderDto order = checkoutOrderService.getOrderForUser(userId, orderId);

            if (order == null) {
                request.getSession().setAttribute(AttributeConstants.ERROR_MESSAGE, MessageConstants.ORDER_NOT_FOUND);
                response.sendRedirect(request.getContextPath() + "/products");
                return;
            }

            request.setAttribute("order", order);
            request.setAttribute(AttributeConstants.CURRENCY_SYMBOL, CatalogConstants.DEFAULT_CURRENCY_SYMBOL);
            request.getRequestDispatcher(CheckoutViewConstants.ORDER_SUCCESS_VIEW).forward(request, response);

        } catch (SQLException exception) {
            request.getSession().setAttribute(AttributeConstants.ERROR_MESSAGE, MessageConstants.SERVER_ERROR);
            response.sendRedirect(request.getContextPath() + "/products");
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