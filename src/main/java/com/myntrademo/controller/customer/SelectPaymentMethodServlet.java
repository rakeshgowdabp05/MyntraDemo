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

@WebServlet("/checkout/payment/select")
public class SelectPaymentMethodServlet extends HttpServlet {

    private final PaymentMethodService paymentMethodService = new PaymentMethodServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String paymentMethod = request.getParameter("paymentMethod");

        try {
            if (!paymentMethodService.isValidPaymentMethod(paymentMethod)) {
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
            request.getSession().removeAttribute(CheckoutAttributeConstants.SELECTED_PAYMENT_OPTION_CODE);

            response.sendRedirect(request.getContextPath() + "/checkout/payment");

        } catch (SQLException exception) {
            request.getSession().setAttribute(AttributeConstants.ERROR_MESSAGE, MessageConstants.SERVER_ERROR);
            response.sendRedirect(request.getContextPath() + "/checkout/payment");
        }
    }
}