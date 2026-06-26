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

@WebServlet("/checkout/gift-card/remove")
public class RemoveGiftCardServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getSession().removeAttribute(CheckoutAttributeConstants.APPLIED_GIFT_CARD_CODE);
        request.getSession().removeAttribute(CheckoutAttributeConstants.APPLIED_GIFT_CARD_DISCOUNT);
        request.getSession().setAttribute(AttributeConstants.SUCCESS_MESSAGE, MessageConstants.GIFT_CARD_REMOVED);

        response.sendRedirect(request.getContextPath() + "/checkout/payment");
    }
}