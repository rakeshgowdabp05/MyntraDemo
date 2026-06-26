package com.myntrademo.service.impl;

import com.myntrademo.dao.CheckoutOrderDao;
import com.myntrademo.dao.impl.JdbcCheckoutOrderDao;
import com.myntrademo.dto.checkout.PlaceOrderRequest;
import com.myntrademo.dto.checkout.PlacedOrderDto;
import com.myntrademo.service.CheckoutOrderService;

import java.sql.SQLException;

public class CheckoutOrderServiceImpl implements CheckoutOrderService {

    private final CheckoutOrderDao checkoutOrderDao = new JdbcCheckoutOrderDao();

    @Override
    public PlacedOrderDto placeOrder(PlaceOrderRequest request) throws SQLException {
        if (request == null
                || request.getUserId() == null
                || request.getCartPage() == null
                || request.getCartPage().isEmpty()
                || request.getDeliveryAddress() == null
                || request.getPaymentMethodCode() == null
                || request.getPaymentMethodCode().isBlank()) {
            throw new IllegalArgumentException("Invalid order request.");
        }

        return checkoutOrderDao.createOrder(request);
    }

    @Override
    public PlacedOrderDto getOrderForUser(Long userId, Long orderId) throws SQLException {
        if (userId == null || orderId == null) {
            return null;
        }

        return checkoutOrderDao.findOrderForUser(userId, orderId);
    }

    @Override
    public PlacedOrderDto getOrderForUserByOrderNumber(Long userId, String orderNumber) throws SQLException {
        if (userId == null || orderNumber == null || orderNumber.isBlank()) {
            return null;
        }

        return checkoutOrderDao.findOrderForUserByOrderNumber(userId, orderNumber);
    }
}