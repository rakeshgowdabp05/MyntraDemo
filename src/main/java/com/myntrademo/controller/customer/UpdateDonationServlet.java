package com.myntrademo.controller.customer;

import com.myntrademo.constant.AttributeConstants;
import com.myntrademo.constant.CartExtraChargeConstants;
import com.myntrademo.constant.CheckoutAttributeConstants;
import com.myntrademo.constant.MessageConstants;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/cart/donation/update")
public class UpdateDonationServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            BigDecimal donationAmount = new BigDecimal(request.getParameter("amount"));

            if (!CartExtraChargeConstants.DONATION_OPTIONS.contains(donationAmount)) {
                throw new IllegalArgumentException(MessageConstants.INVALID_DONATION_AMOUNT);
            }

            request.getSession().setAttribute(CheckoutAttributeConstants.DONATION_AMOUNT, donationAmount);
            request.getSession().setAttribute(AttributeConstants.SUCCESS_MESSAGE, MessageConstants.DONATION_ADDED);

        } catch (IllegalArgumentException exception) {
            request.getSession().setAttribute(AttributeConstants.ERROR_MESSAGE, exception.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/cart");
    }
}