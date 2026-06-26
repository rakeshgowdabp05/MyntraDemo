package com.myntrademo.dao;

import com.myntrademo.dto.cart.CartAddressDto;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface AddressDao {

    List<CartAddressDto> findActiveAddressesByUserId(Long userId) throws SQLException;

    Optional<CartAddressDto> findDefaultAddressByUserId(Long userId) throws SQLException;

    Long createAddress(Long userId, CartAddressDto address) throws SQLException;

    void updateAddress(Long userId, CartAddressDto address) throws SQLException;

    void setDefaultAddress(Long userId, Long addressId) throws SQLException;

    void deactivateAddress(Long userId, Long addressId) throws SQLException;
}