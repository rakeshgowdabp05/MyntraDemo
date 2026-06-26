package com.myntrademo.controller.customer;

import com.myntrademo.constant.AttributeConstants;
import com.myntrademo.constant.CheckoutAttributeConstants;
import com.myntrademo.constant.MessageConstants;
import com.myntrademo.dto.checkout.GiftCardDto;
import com.myntrademo.service.GiftCardService;
import com.myntrademo.service.impl.GiftCardServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

@WebServlet("/checkout/gift-card/apply")
public class ApplyGiftCardServlet extends HttpServlet {

    private final GiftCardService giftCardService = new GiftCardServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String giftCardCode = request.getParameter("giftCardCode");
        String orderAmountText = request.getParameter("orderAmount");

        try {
            BigDecimal orderAmount = new BigDecimal(orderAmountText);
            GiftCardDto giftCard = giftCardService.findUsableGiftCard(giftCardCode, orderAmount);

            if (giftCard == null) {
                request.getSession().setAttribute(AttributeConstants.ERROR_MESSAGE, MessageConstants.GIFT_CARD_INVALID);
                response.sendRedirect(request.getContextPath() + "/checkout/payment");
                return;
            }

            BigDecimal discount = giftCardService.calculateDiscount(giftCard, orderAmount);

            request.getSession().setAttribute(
                    CheckoutAttributeConstants.APPLIED_GIFT_CARD_CODE,
                    giftCard.getGiftCardCode()
            );
            request.getSession().setAttribute(
                    CheckoutAttributeConstants.APPLIED_GIFT_CARD_DISCOUNT,
                    discount
            );
            request.getSession().setAttribute(AttributeConstants.SUCCESS_MESSAGE, MessageConstants.GIFT_CARD_APPLIED);

            response.sendRedirect(request.getContextPath() + "/checkout/payment");

        } catch (NumberFormatException | SQLException exception) {
            request.getSession().setAttribute(AttributeConstants.ERROR_MESSAGE, MessageConstants.GIFT_CARD_INVALID);
            response.sendRedirect(request.getContextPath() + "/checkout/payment");
        }
    }
}