package com.myntrademo.dao;

import com.myntrademo.dto.checkout.PaymentMethodDto;
import com.myntrademo.dto.checkout.PaymentOptionDto;

import java.sql.SQLException;
import java.util.List;

public interface PaymentMethodDao {

    List<PaymentMethodDto> findActivePaymentMethods() throws SQLException;

    boolean existsActiveMethod(String methodCode) throws SQLException;

    boolean existsActiveOption(String methodCode, String optionCode) throws SQLException;

    PaymentOptionDto findOption(String methodCode, String optionCode) throws SQLException;
}