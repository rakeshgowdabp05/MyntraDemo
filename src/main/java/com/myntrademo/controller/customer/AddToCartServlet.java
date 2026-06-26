package com.myntrademo.controller.customer;

import com.myntrademo.constant.AttributeConstants;
import com.myntrademo.constant.MessageConstants;
import com.myntrademo.constant.RequestParamConstants;
import com.myntrademo.constant.RouteConstants;
import com.myntrademo.dto.cart.AddToCartRequest;
import com.myntrademo.service.CartService;
import com.myntrademo.service.impl.CartServiceImpl;
import com.myntrademo.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/cart/add")
public class AddToCartServlet extends HttpServlet {

    private final CartService cartService = new CartServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Long userId = getAuthenticatedUserId(request);
        Long productId = parseLong(request.getParameter(RequestParamConstants.PRODUCT_ID));
        Long variantId = parseLong(request.getParameter(RequestParamConstants.VARIANT_ID));
        int quantity = parseIntOrDefault(request.getParameter(RequestParamConstants.QUANTITY), 1);

        AddToCartRequest addToCartRequest = new AddToCartRequest(productId, variantId, quantity);

        try {
            cartService.addToCart(userId, addToCartRequest);
            request.getSession().setAttribute(AttributeConstants.SUCCESS_MESSAGE, MessageConstants.CART_ITEM_ADDED);
            response.sendRedirect(request.getContextPath() + RouteConstants.CART);

        } catch (IllegalArgumentException exception) {
            request.getSession().setAttribute(AttributeConstants.ERROR_MESSAGE, exception.getMessage());

            if (productId != null) {
                response.sendRedirect(request.getContextPath() + RouteConstants.PRODUCT_DETAIL + "?id=" + productId);
            } else {
                response.sendRedirect(request.getContextPath() + RouteConstants.PRODUCTS);
            }

        } catch (SQLException exception) {
            request.getSession().setAttribute(AttributeConstants.ERROR_MESSAGE, MessageConstants.SERVER_ERROR);

            if (productId != null) {
                response.sendRedirect(request.getContextPath() + RouteConstants.PRODUCT_DETAIL + "?id=" + productId);
            } else {
                response.sendRedirect(request.getContextPath() + RouteConstants.PRODUCTS);
            }
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
        try {
            if (ValidationUtil.isBlank(value)) {
                return null;
            }

            return Long.parseLong(value);

        } catch (NumberFormatException exception) {
            return null;
        }
    }

    private int parseIntOrDefault(String value, int defaultValue) {
        try {
            if (ValidationUtil.isBlank(value)) {
                return defaultValue;
            }

            return Integer.parseInt(value);

        } catch (NumberFormatException exception) {
            return defaultValue;
        }
    }
}