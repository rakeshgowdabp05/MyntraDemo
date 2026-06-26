package com.myntrademo.controller.customer;

import com.myntrademo.constant.AttributeConstants;
import com.myntrademo.constant.MessageConstants;
import com.myntrademo.constant.RequestParamConstants;
import com.myntrademo.constant.RouteConstants;
import com.myntrademo.service.WishlistService;
import com.myntrademo.service.impl.WishlistServiceImpl;
import com.myntrademo.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/cart/move-to-wishlist")
public class MoveCartToWishlistServlet extends HttpServlet {

    private final WishlistService wishlistService = new WishlistServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Long userId = getAuthenticatedUserId(request);
        Long cartItemId = parseLong(request.getParameter(RequestParamConstants.CART_ITEM_ID));

        try {
            wishlistService.moveCartItemToWishlist(userId, cartItemId);
            request.getSession().setAttribute(AttributeConstants.SUCCESS_MESSAGE, MessageConstants.CART_ITEM_MOVED_TO_WISHLIST);

        } catch (IllegalArgumentException exception) {
            request.getSession().setAttribute(AttributeConstants.ERROR_MESSAGE, exception.getMessage());

        } catch (SQLException exception) {
            request.getSession().setAttribute(AttributeConstants.ERROR_MESSAGE, MessageConstants.SERVER_ERROR);
        }

        response.sendRedirect(request.getContextPath() + RouteConstants.CART);
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
}