package com.myntrademo.controller.customer;

import com.myntrademo.constant.AttributeConstants;
import com.myntrademo.constant.MessageConstants;
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

@WebServlet(urlPatterns = "/order/cancel")
public class CancelOrderServlet extends HttpServlet {

    private static final String ORDER_STATUS_PLACED = "PLACED";
    private static final String ORDER_STATUS_CANCELLED = "CANCELLED";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Long orderId = parseLong(request.getParameter("orderId"));

        if (orderId != null) {
            response.sendRedirect(request.getContextPath() + "/order/details?id=" + orderId);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/orders");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Long userId = getAuthenticatedUserId(request);
        Long orderId = parseLong(request.getParameter("orderId"));

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        if (orderId == null) {
            request.getSession().setAttribute(AttributeConstants.ERROR_MESSAGE, MessageConstants.ORDER_CANCEL_FAILED);
            response.sendRedirect(request.getContextPath() + "/orders");
            return;
        }

        try {
            boolean cancelled = cancelOrder(userId, orderId);

            if (cancelled) {
                request.getSession().setAttribute(AttributeConstants.SUCCESS_MESSAGE, MessageConstants.ORDER_CANCELLED_SUCCESSFULLY);
            } else {
                request.getSession().setAttribute(AttributeConstants.ERROR_MESSAGE, MessageConstants.ORDER_CANCEL_FAILED);
            }

            response.sendRedirect(request.getContextPath() + "/order/details?id=" + orderId);

        } catch (SQLException exception) {
            request.getSession().setAttribute(AttributeConstants.ERROR_MESSAGE, MessageConstants.SERVER_ERROR);
            response.sendRedirect(request.getContextPath() + "/order/details?id=" + orderId);
        }
    }

    private boolean cancelOrder(Long userId, Long orderId) throws SQLException {
        String sql = """
                UPDATE checkout_orders
                SET order_status = ?
                WHERE order_id = ?
                  AND user_id = ?
                  AND order_status = ?
                """;

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, ORDER_STATUS_CANCELLED);
            statement.setLong(2, orderId);
            statement.setLong(3, userId);
            statement.setString(4, ORDER_STATUS_PLACED);

            return statement.executeUpdate() > 0;
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
}