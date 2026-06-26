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

@WebServlet("/cart/gift/toggle")
public class ToggleGiftPackageServlet extends HttpServlet {

    private static final int MAX_GIFT_FIELD_LENGTH = 120;
    private static final int MAX_GIFT_MESSAGE_LENGTH = 200;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        boolean enableGiftPackage = "true".equalsIgnoreCase(request.getParameter("enabled"));

        if (enableGiftPackage) {
            String recipientName = clean(request.getParameter("recipientName"), MAX_GIFT_FIELD_LENGTH);
            String giftMessage = clean(request.getParameter("giftMessage"), MAX_GIFT_MESSAGE_LENGTH);
            String senderName = clean(request.getParameter("senderName"), MAX_GIFT_FIELD_LENGTH);

            request.getSession().setAttribute(CheckoutAttributeConstants.GIFT_PACKAGE_ENABLED, Boolean.TRUE);
            request.getSession().setAttribute(CheckoutAttributeConstants.GIFT_RECIPIENT_NAME, recipientName);
            request.getSession().setAttribute(CheckoutAttributeConstants.GIFT_MESSAGE, giftMessage);
            request.getSession().setAttribute(CheckoutAttributeConstants.GIFT_SENDER_NAME, senderName);
            request.getSession().setAttribute(AttributeConstants.SUCCESS_MESSAGE, MessageConstants.GIFT_PACKAGE_ADDED);
        } else {
            request.getSession().removeAttribute(CheckoutAttributeConstants.GIFT_PACKAGE_ENABLED);
            request.getSession().removeAttribute(CheckoutAttributeConstants.GIFT_RECIPIENT_NAME);
            request.getSession().removeAttribute(CheckoutAttributeConstants.GIFT_MESSAGE);
            request.getSession().removeAttribute(CheckoutAttributeConstants.GIFT_SENDER_NAME);
            request.getSession().setAttribute(AttributeConstants.SUCCESS_MESSAGE, MessageConstants.GIFT_PACKAGE_REMOVED);
        }

        response.sendRedirect(request.getContextPath() + "/cart");
    }

    private String clean(String value, int maxLength) {
        if (value == null) {
            return "";
        }

        String cleaned = value.trim();

        if (cleaned.length() > maxLength) {
            return cleaned.substring(0, maxLength);
        }

        return cleaned;
    }
}