package com.myntrademo.controller.customer;

import com.myntrademo.constant.AttributeConstants;
import com.myntrademo.constant.CheckoutAttributeConstants;
import com.myntrademo.constant.MessageConstants;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/cart/coupon/remove")
public class RemoveCouponServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getSession().removeAttribute(CheckoutAttributeConstants.APPLIED_COUPON_ID);
        request.getSession().setAttribute(AttributeConstants.SUCCESS_MESSAGE, MessageConstants.COUPON_REMOVED);

        response.sendRedirect(request.getContextPath() + "/cart");
    }
}