package com.myntrademo.controller.customer;

import com.myntrademo.constant.AttributeConstants;
import com.myntrademo.constant.MessageConstants;
import com.myntrademo.dto.cart.CartAddressDto;
import com.myntrademo.service.AddressService;
import com.myntrademo.service.impl.AddressServiceImpl;
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
import java.util.List;

@WebServlet("/order/address/update")
public class UpdateOrderAddressServlet extends HttpServlet {

    private final AddressService addressService = new AddressServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Long userId = getAuthenticatedUserId(request);

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Long orderId = parseLong(request.getParameter("orderId"));
        Long addressId = parseLong(request.getParameter("addressId"));

        if (orderId == null || addressId == null) {
            request.getSession().setAttribute(AttributeConstants.ERROR_MESSAGE, MessageConstants.ADDRESS_REQUIRED);
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }

        try {
            CartAddressDto address = findAddress(userId, addressId);

            if (address == null) {
                request.getSession().setAttribute(AttributeConstants.ERROR_MESSAGE, MessageConstants.ADDRESS_NOT_FOUND);
                response.sendRedirect(request.getContextPath() + "/order/details?id=" + orderId);
                return;
            }

            updateOrderAddress(userId, orderId, address);

            request.getSession().setAttribute(AttributeConstants.SUCCESS_MESSAGE, MessageConstants.ADDRESS_DEFAULT_UPDATED);
            response.sendRedirect(request.getContextPath() + "/order/details?id=" + orderId);

        } catch (SQLException exception) {
            request.getSession().setAttribute(AttributeConstants.ERROR_MESSAGE, MessageConstants.SERVER_ERROR);
            response.sendRedirect(request.getContextPath() + "/order/details?id=" + orderId);
        }
    }

    private CartAddressDto findAddress(Long userId, Long addressId) throws SQLException {
        List<CartAddressDto> addresses = addressService.getAddresses(userId);

        for (CartAddressDto address : addresses) {
            if (address.getAddressId() != null && address.getAddressId().equals(addressId)) {
                return address;
            }
        }

        return null;
    }

    private void updateOrderAddress(Long userId, Long orderId, CartAddressDto address) throws SQLException {
        String sql = """
                UPDATE checkout_orders
                SET
                    address_id = ?,
                    customer_name = ?,
                    customer_phone = ?,
                    delivery_pincode = ?,
                    delivery_address_text = ?
                WHERE order_id = ?
                  AND user_id = ?
                """;

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setLong(1, address.getAddressId());
            statement.setString(2, address.getFullName());
            statement.setString(3, address.getPhone());
            statement.setString(4, address.getPincode());
            statement.setString(5, address.getDisplayAddress());
            statement.setLong(6, orderId);
            statement.setLong(7, userId);
            statement.executeUpdate();
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