package com.myntrademo.controller.customer;

import com.myntrademo.constant.AttributeConstants;
import com.myntrademo.constant.CatalogConstants;
import com.myntrademo.constant.MessageConstants;
import com.myntrademo.constant.SupportConstants;
import com.myntrademo.dto.checkout.PlacedOrderDto;
import com.myntrademo.service.CheckoutOrderService;
import com.myntrademo.service.impl.CheckoutOrderServiceImpl;
import com.myntrademo.util.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/contacts/chat")
public class HelpChatServlet extends HttpServlet {

    private final CheckoutOrderService checkoutOrderService = new CheckoutOrderServiceImpl();

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

            request.setAttribute("order", order);
            request.setAttribute("primaryItem", order == null ? null : order.getPrimaryItem());
            request.setAttribute(AttributeConstants.CURRENCY_SYMBOL, CatalogConstants.DEFAULT_CURRENCY_SYMBOL);

            request.getRequestDispatcher("/WEB-INF/views/customer/help-chat.jsp").forward(request, response);

        } catch (SQLException exception) {
            request.getSession().setAttribute(AttributeConstants.ERROR_MESSAGE, MessageConstants.SERVER_ERROR);
            response.sendRedirect(request.getContextPath() + "/contacts");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Long userId = getAuthenticatedUserId(request);

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String orderNumber = request.getParameter("storeOrderId");
        String message = cleanMessage(request.getParameter("message"));

        if (message.isBlank()) {
            request.getSession().setAttribute(AttributeConstants.ERROR_MESSAGE, MessageConstants.SUPPORT_QUERY_FAILED);
            redirectBack(request, response, orderNumber);
            return;
        }

        try {
            PlacedOrderDto order = null;

            if (orderNumber != null && !orderNumber.isBlank()) {
                order = checkoutOrderService.getOrderForUserByOrderNumber(userId, orderNumber);
            }

            saveSupportQuery(userId, order, message);

            request.getSession().setAttribute(AttributeConstants.SUCCESS_MESSAGE, MessageConstants.SUPPORT_QUERY_SENT);
            redirectBack(request, response, orderNumber);

        } catch (SQLException exception) {
            request.getSession().setAttribute(AttributeConstants.ERROR_MESSAGE, MessageConstants.SUPPORT_QUERY_FAILED);
            redirectBack(request, response, orderNumber);
        }
    }

    private PlacedOrderDto resolveOrder(HttpServletRequest request, Long userId) throws SQLException {
        String orderNumber = request.getParameter("storeOrderId");

        if (orderNumber == null || orderNumber.isBlank()) {
            return null;
        }

        return checkoutOrderService.getOrderForUserByOrderNumber(userId, orderNumber);
    }

    private void saveSupportQuery(Long userId, PlacedOrderDto order, String message) throws SQLException {
        String sql = """
                INSERT INTO support_queries
                (
                    user_id,
                    order_id,
                    order_number,
                    customer_name,
                    customer_phone,
                    query_source,
                    query_status,
                    query_message
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setLong(1, userId);

            if (order == null || order.getOrderId() == null) {
                statement.setObject(2, null);
                statement.setString(3, null);
                statement.setString(4, null);
                statement.setString(5, null);
            } else {
                statement.setLong(2, order.getOrderId());
                statement.setString(3, order.getDisplayOrderNumber());
                statement.setString(4, order.getDisplayCustomerName());
                statement.setString(5, order.getDisplayCustomerPhone());
            }

            statement.setString(6, SupportConstants.QUERY_SOURCE_CHAT);
            statement.setString(7, SupportConstants.QUERY_STATUS_OPEN);
            statement.setString(8, message);

            statement.executeUpdate();
        }
    }

    private void redirectBack(HttpServletRequest request, HttpServletResponse response, String orderNumber)
            throws IOException {

        if (orderNumber == null || orderNumber.isBlank()) {
            response.sendRedirect(request.getContextPath() + "/contacts/chat");
            return;
        }

        response.sendRedirect(request.getContextPath() + "/contacts/chat?storeOrderId=" + orderNumber);
    }

    private String cleanMessage(String value) {
        if (value == null) {
            return "";
        }

        String cleaned = value.trim();

        if (cleaned.length() > SupportConstants.MAX_SUPPORT_MESSAGE_LENGTH) {
            return cleaned.substring(0, SupportConstants.MAX_SUPPORT_MESSAGE_LENGTH);
        }

        return cleaned;
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