package com.myntrademo.service;

import com.myntrademo.dto.checkout.PlaceOrderRequest;
import com.myntrademo.dto.checkout.PlacedOrderDto;

import java.sql.SQLException;

public interface CheckoutOrderService {

    PlacedOrderDto placeOrder(PlaceOrderRequest request) throws SQLException;

    PlacedOrderDto getOrderForUser(Long userId, Long orderId) throws SQLException;

    PlacedOrderDto getOrderForUserByOrderNumber(Long userId, String orderNumber) throws SQLException;
}