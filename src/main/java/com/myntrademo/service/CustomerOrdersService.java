package com.myntrademo.service;

import com.myntrademo.dto.checkout.CustomerOrderCardDto;

import java.sql.SQLException;
import java.util.List;

public interface CustomerOrdersService {

    List<CustomerOrderCardDto> getOrdersForUser(Long userId, String statusFilter, String searchText)
            throws SQLException;
}