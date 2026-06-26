package com.myntrademo.dao;

import com.myntrademo.dto.checkout.PlaceOrderRequest;
import com.myntrademo.dto.checkout.PlacedOrderDto;

import java.sql.SQLException;

public interface CheckoutOrderDao {

    PlacedOrderDto createOrder(PlaceOrderRequest request) throws SQLException;

    PlacedOrderDto findOrderForUser(Long userId, Long orderId) throws SQLException;

    PlacedOrderDto findOrderForUserByOrderNumber(Long userId, String orderNumber) throws SQLException;
}