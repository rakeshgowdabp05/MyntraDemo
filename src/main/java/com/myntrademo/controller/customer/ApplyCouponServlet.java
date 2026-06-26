package com.myntrademo.controller.customer;

import com.myntrademo.constant.AttributeConstants;
import com.myntrademo.constant.CheckoutAttributeConstants;
import com.myntrademo.constant.MessageConstants;
import com.myntrademo.dto.cart.CartPageDto;
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

@WebServlet("/cart/coupon/apply")
public class ApplyCouponServlet extends HttpServlet {

    private final CartService cartService = new CartServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Long userId = getAuthenticatedUserId(request);
        Long couponId = parseLong(request.getParameter("couponId"));

        try {
            if (couponId == null) {
                throw new IllegalArgumentException(MessageConstants.COUPON_NOT_FOUND);
            }

            CartPageDto cartPage = cartService.getCartPage(userId, couponId);

            if (!cartPage.isAppliedCouponPresent()) {
                throw new IllegalArgumentException(MessageConstants.COUPON_NOT_APPLICABLE);
            }

            request.getSession().setAttribute(CheckoutAttributeConstants.APPLIED_COUPON_ID, couponId);
            request.getSession().setAttribute(AttributeConstants.SUCCESS_MESSAGE, MessageConstants.COUPON_APPLIED);

        } catch (IllegalArgumentException exception) {
            request.getSession().setAttribute(AttributeConstants.ERROR_MESSAGE, exception.getMessage());

        } catch (SQLException exception) {
            request.getSession().setAttribute(AttributeConstants.ERROR_MESSAGE, MessageConstants.SERVER_ERROR);
        }

        response.sendRedirect(request.getContextPath() + "/cart");
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