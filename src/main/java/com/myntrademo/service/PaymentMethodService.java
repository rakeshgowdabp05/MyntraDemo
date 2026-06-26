package com.myntrademo.service;

import com.myntrademo.dto.checkout.PaymentMethodDto;
import com.myntrademo.dto.checkout.PaymentOptionDto;

import java.sql.SQLException;
import java.util.List;

public interface PaymentMethodService {

    List<PaymentMethodDto> getPaymentMethods() throws SQLException;

    PaymentMethodDto getDefaultPaymentMethod(List<PaymentMethodDto> methods);

    PaymentMethodDto findSelectedMethod(List<PaymentMethodDto> methods, String selectedCode);

    boolean isValidPaymentMethod(String methodCode) throws SQLException;

    boolean isValidPaymentOption(String methodCode, String optionCode) throws SQLException;

    PaymentOptionDto findSelectedOption(String methodCode, String optionCode) throws SQLException;
}