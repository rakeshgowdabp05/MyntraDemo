package com.myntrademo.controller.customer;

import com.myntrademo.constant.AttributeConstants;
import com.myntrademo.constant.CatalogConstants;
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

@WebServlet(urlPatterns = {"/contacts", "/contacts/generic", "/contacts/issues"})
public class HelpCenterServlet extends HttpServlet {

    private final CheckoutOrderService checkoutOrderService = new CheckoutOrderServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Long userId = getAuthenticatedUserId(request);

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String servletPath = request.getServletPath();
        String storeOrderId = request.getParameter("storeOrderId");

        try {
            PlacedOrderDto order = null;

            if (storeOrderId != null && !storeOrderId.isBlank()) {
                order = checkoutOrderService.getOrderForUserByOrderNumber(userId, storeOrderId);
            }

            request.setAttribute("order", order);
            request.setAttribute("primaryItem", order == null ? null : order.getPrimaryItem());
            request.setAttribute("mode", resolveMode(servletPath, order));
            request.setAttribute(AttributeConstants.CURRENCY_SYMBOL, CatalogConstants.DEFAULT_CURRENCY_SYMBOL);

            request.getRequestDispatcher("/WEB-INF/views/customer/help-center.jsp").forward(request, response);

        } catch (SQLException exception) {
            response.sendRedirect(request.getContextPath() + "/products");
        }
    }

    private String resolveMode(String servletPath, PlacedOrderDto order) {
        if ("/contacts/generic".equals(servletPath)) {
            return "generic";
        }

        if ("/contacts/issues".equals(servletPath)) {
            return "issues";
        }

        if (order == null) {
            return "orders";
        }

        return "order-detail";
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