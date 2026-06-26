package com.myntrademo.controller.customer;

import com.myntrademo.constant.AttributeConstants;
import com.myntrademo.constant.CatalogConstants;
import com.myntrademo.constant.MessageConstants;
import com.myntrademo.dto.checkout.CustomerOrderCardDto;
import com.myntrademo.service.CustomerOrdersService;
import com.myntrademo.service.impl.CustomerOrdersServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/orders")
public class OrdersServlet extends HttpServlet {

    private static final String ORDERS_VIEW = "/WEB-INF/views/customer/orders.jsp";

    private final CustomerOrdersService customerOrdersService = new CustomerOrdersServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Long userId = getAuthenticatedUserId(request);

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String status = clean(request.getParameter("status"));
        String search = clean(request.getParameter("search"));

        try {
            List<CustomerOrderCardDto> orders = customerOrdersService.getOrdersForUser(userId, status, search);

            request.setAttribute("orders", orders);
            request.setAttribute("selectedStatus", status.isBlank() ? "ALL" : status.toUpperCase());
            request.setAttribute("searchText", search);
            request.setAttribute("accountDisplayName", getAccountDisplayName(request));
            request.setAttribute(AttributeConstants.CURRENCY_SYMBOL, CatalogConstants.DEFAULT_CURRENCY_SYMBOL);

            request.getRequestDispatcher(ORDERS_VIEW).forward(request, response);

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

    private String getAccountDisplayName(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            return "Customer";
        }

        String[] possibleKeys = {
                "authFullName",
                "authUserName",
                "fullName",
                "userName",
                "name"
        };

        for (String key : possibleKeys) {
            Object value = session.getAttribute(key);

            if (value != null && !String.valueOf(value).isBlank()) {
                return String.valueOf(value);
            }
        }

        return "Customer";
    }

    private String clean(String value) {
        return value == null ? "" : value.trim();
    }
}