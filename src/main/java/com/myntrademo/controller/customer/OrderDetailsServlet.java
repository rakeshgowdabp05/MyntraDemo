package com.myntrademo.controller.customer;

import com.myntrademo.constant.AttributeConstants;
import com.myntrademo.constant.CatalogConstants;
import com.myntrademo.constant.CheckoutViewConstants;
import com.myntrademo.constant.MessageConstants;
import com.myntrademo.dto.cart.CartAddressDto;
import com.myntrademo.dto.checkout.PlacedOrderDto;
import com.myntrademo.service.AddressService;
import com.myntrademo.service.CheckoutOrderService;
import com.myntrademo.service.impl.AddressServiceImpl;
import com.myntrademo.service.impl.CheckoutOrderServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(urlPatterns = {"/order/details", "/my/item/details"})
public class OrderDetailsServlet extends HttpServlet {

    private final CheckoutOrderService checkoutOrderService = new CheckoutOrderServiceImpl();
    private final AddressService addressService = new AddressServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Long userId = getAuthenticatedUserId(request);

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            PlacedOrderDto order = resolveOrder(request, userId);

            if (order == null) {
                request.getSession().setAttribute(AttributeConstants.ERROR_MESSAGE, MessageConstants.ORDER_NOT_FOUND);
                response.sendRedirect(request.getContextPath() + "/products");
                return;
            }

            List<CartAddressDto> addresses = addressService.getAddresses(userId);

            request.setAttribute("order", order);
            request.setAttribute("primaryItem", order.getPrimaryItem());
            request.setAttribute("addresses", addresses);
            request.setAttribute(AttributeConstants.CURRENCY_SYMBOL, CatalogConstants.DEFAULT_CURRENCY_SYMBOL);
            request.getRequestDispatcher(CheckoutViewConstants.ORDER_DETAILS_VIEW).forward(request, response);

        } catch (SQLException exception) {
            request.getSession().setAttribute(AttributeConstants.ERROR_MESSAGE, MessageConstants.SERVER_ERROR);
            response.sendRedirect(request.getContextPath() + "/products");
        }
    }

    private PlacedOrderDto resolveOrder(HttpServletRequest request, Long userId) throws SQLException {
        Long orderId = parseLong(request.getParameter("id"));

        if (orderId != null) {
            return checkoutOrderService.getOrderForUser(userId, orderId);
        }

        String orderNumber = request.getParameter("storeOrderId");

        if (orderNumber != null && !orderNumber.isBlank()) {
            return checkoutOrderService.getOrderForUserByOrderNumber(userId, orderNumber);
        }

        return null;
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