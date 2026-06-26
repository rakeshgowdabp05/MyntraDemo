package com.myntrademo.service;

import com.myntrademo.dto.cart.CartAddressDto;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface AddressService {

    List<CartAddressDto> getAddresses(Long userId) throws SQLException;

    Optional<CartAddressDto> getDefaultAddress(Long userId) throws SQLException;

    Long addAddress(Long userId, CartAddressDto address) throws SQLException;

    void updateAddress(Long userId, CartAddressDto address) throws SQLException;

    void setDefaultAddress(Long userId, Long addressId) throws SQLException;

    void deleteAddress(Long userId, Long addressId) throws SQLException;
}