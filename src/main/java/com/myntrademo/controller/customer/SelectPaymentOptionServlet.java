package com.myntrademo.controller.customer;

import com.myntrademo.constant.AttributeConstants;
import com.myntrademo.constant.CheckoutAttributeConstants;
import com.myntrademo.constant.MessageConstants;
import com.myntrademo.service.PaymentMethodService;
import com.myntrademo.service.impl.PaymentMethodServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/checkout/payment/option")
public class SelectPaymentOptionServlet extends HttpServlet {

    private final PaymentMethodService paymentMethodService = new PaymentMethodServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String paymentMethod = request.getParameter("paymentMethod");
        String optionCode = request.getParameter("optionCode");

        try {
            if (!paymentMethodService.isValidPaymentOption(paymentMethod, optionCode)) {
                request.getSession().setAttribute(
                        AttributeConstants.ERROR_MESSAGE,
                        MessageConstants.PAYMENT_METHOD_REQUIRED
                );
                response.sendRedirect(request.getContextPath() + "/checkout/payment");
                return;
            }

            request.getSession().setAttribute(
                    CheckoutAttributeConstants.SELECTED_PAYMENT_METHOD,
                    paymentMethod
            );
            request.getSession().setAttribute(
                    CheckoutAttributeConstants.SELECTED_PAYMENT_OPTION_CODE,
                    optionCode
            );

            response.sendRedirect(request.getContextPath() + "/checkout/payment");

        } catch (SQLException exception) {
            request.getSession().setAttribute(AttributeConstants.ERROR_MESSAGE, MessageConstants.SERVER_ERROR);
            response.sendRedirect(request.getContextPath() + "/checkout/payment");
        }
    }
}