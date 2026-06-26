package com.myntrademo.service.impl;

import com.myntrademo.dao.PaymentMethodDao;
import com.myntrademo.dao.impl.JdbcPaymentMethodDao;
import com.myntrademo.dto.checkout.PaymentMethodDto;
import com.myntrademo.dto.checkout.PaymentOptionDto;
import com.myntrademo.service.PaymentMethodService;

import java.sql.SQLException;
import java.util.List;

public class PaymentMethodServiceImpl implements PaymentMethodService {

    private final PaymentMethodDao paymentMethodDao = new JdbcPaymentMethodDao();

    @Override
    public List<PaymentMethodDto> getPaymentMethods() throws SQLException {
        return paymentMethodDao.findActivePaymentMethods();
    }

    @Override
    public PaymentMethodDto getDefaultPaymentMethod(List<PaymentMethodDto> methods) {
        if (methods == null || methods.isEmpty()) {
            return null;
        }

        for (PaymentMethodDto method : methods) {
            if (method.isRecommended()) {
                return method;
            }
        }

        return methods.get(0);
    }

    @Override
    public PaymentMethodDto findSelectedMethod(List<PaymentMethodDto> methods, String selectedCode) {
        if (methods == null || methods.isEmpty()) {
            return null;
        }

        if (selectedCode != null && !selectedCode.isBlank()) {
            for (PaymentMethodDto method : methods) {
                if (method.getCode().equals(selectedCode)) {
                    return method;
                }
            }
        }

        return getDefaultPaymentMethod(methods);
    }

    @Override
    public boolean isValidPaymentMethod(String methodCode) throws SQLException {
        if (methodCode == null || methodCode.isBlank()) {
            return false;
        }

        return paymentMethodDao.existsActiveMethod(methodCode);
    }

    @Override
    public boolean isValidPaymentOption(String methodCode, String optionCode) throws SQLException {
        if (methodCode == null || methodCode.isBlank() || optionCode == null || optionCode.isBlank()) {
            return false;
        }

        return paymentMethodDao.existsActiveOption(methodCode, optionCode);
    }

    @Override
    public PaymentOptionDto findSelectedOption(String methodCode, String optionCode) throws SQLException {
        if (methodCode == null || methodCode.isBlank() || optionCode == null || optionCode.isBlank()) {
            return null;
        }

        return paymentMethodDao.findOption(methodCode, optionCode);
    }
}