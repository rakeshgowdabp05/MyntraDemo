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

@WebServlet("/cart/donation/remove")
public class RemoveDonationServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getSession().removeAttribute(CheckoutAttributeConstants.DONATION_AMOUNT);
        request.getSession().setAttribute(AttributeConstants.SUCCESS_MESSAGE, MessageConstants.DONATION_REMOVED);

        response.sendRedirect(request.getContextPath() + "/cart");
    }
}