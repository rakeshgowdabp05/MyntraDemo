package com.myntrademo.service.impl;

import com.myntrademo.constant.AddressConstants;
import com.myntrademo.constant.MessageConstants;
import com.myntrademo.dao.AddressDao;
import com.myntrademo.dao.impl.JdbcAddressDao;
import com.myntrademo.dto.cart.CartAddressDto;
import com.myntrademo.service.AddressService;
import com.myntrademo.util.ValidationUtil;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AddressServiceImpl implements AddressService {

    private final AddressDao addressDao;

    public AddressServiceImpl() {
        this.addressDao = new JdbcAddressDao();
    }

    public AddressServiceImpl(AddressDao addressDao) {
        this.addressDao = addressDao;
    }

    @Override
    public List<CartAddressDto> getAddresses(Long userId) throws SQLException {
        validateUser(userId);
        return addressDao.findActiveAddressesByUserId(userId);
    }

    @Override
    public Optional<CartAddressDto> getDefaultAddress(Long userId) throws SQLException {
        validateUser(userId);
        return addressDao.findDefaultAddressByUserId(userId);
    }

    @Override
    public Long addAddress(Long userId, CartAddressDto address) throws SQLException {
        validateUser(userId);
        validateAddress(address);
        normalizeAddress(address);

        return addressDao.createAddress(userId, address);
    }

        @Override
    public void updateAddress(Long userId, CartAddressDto address) throws SQLException {
        validateUser(userId);

        if (address == null || address.getAddressId() == null) {
            throw new IllegalArgumentException(MessageConstants.ADDRESS_NOT_FOUND);
        }

        validateAddress(address);
        normalizeAddress(address);

        addressDao.updateAddress(userId, address);
    }

    @Override
    public void setDefaultAddress(Long userId, Long addressId) throws SQLException {
        validateUser(userId);

        if (addressId == null) {
            throw new IllegalArgumentException(MessageConstants.ADDRESS_NOT_FOUND);
        }

        addressDao.setDefaultAddress(userId, addressId);
    }

    @Override
    public void deleteAddress(Long userId, Long addressId) throws SQLException {
        validateUser(userId);

        if (addressId == null) {
            throw new IllegalArgumentException(MessageConstants.ADDRESS_NOT_FOUND);
        }

        addressDao.deactivateAddress(userId, addressId);
    }

    private void validateUser(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException(MessageConstants.AUTH_REQUIRED);
        }
    }

    private void validateAddress(CartAddressDto address) {
        if (address == null) {
            throw new IllegalArgumentException(MessageConstants.ADDRESS_REQUIRED);
        }

        if (ValidationUtil.isBlank(address.getFullName())) {
            throw new IllegalArgumentException(MessageConstants.REQUIRED_FULL_NAME);
        }

        if (ValidationUtil.isBlank(address.getPhone())) {
            throw new IllegalArgumentException(MessageConstants.REQUIRED_PHONE);
        }

        if (ValidationUtil.isBlank(address.getPincode())) {
            throw new IllegalArgumentException(MessageConstants.REQUIRED_PINCODE);
        }

        if (!address.getPincode().matches(AddressConstants.PINCODE_PATTERN)) {
            throw new IllegalArgumentException(MessageConstants.INVALID_PINCODE);
        }

        if (ValidationUtil.isBlank(address.getAddressLine())) {
            throw new IllegalArgumentException(MessageConstants.REQUIRED_ADDRESS_LINE);
        }

        if (ValidationUtil.isBlank(address.getCity())) {
            throw new IllegalArgumentException(MessageConstants.REQUIRED_CITY);
        }

        if (ValidationUtil.isBlank(address.getState())) {
            throw new IllegalArgumentException(MessageConstants.REQUIRED_STATE);
        }
    }

    private void normalizeAddress(CartAddressDto address) {
        address.setFullName(trim(address.getFullName()));
        address.setPhone(trim(address.getPhone()));
        address.setPincode(trim(address.getPincode()));
        address.setAddressLine(trim(address.getAddressLine()));
        address.setLocality(trim(address.getLocality()));
        address.setCity(trim(address.getCity()));
        address.setState(trim(address.getState()));

        if (ValidationUtil.isBlank(address.getCountry())) {
            address.setCountry(AddressConstants.DEFAULT_COUNTRY);
        } else {
            address.setCountry(trim(address.getCountry()));
        }

        if (ValidationUtil.isBlank(address.getAddressType())) {
            address.setAddressType(AddressConstants.DEFAULT_ADDRESS_TYPE);
        } else {
            address.setAddressType(trim(address.getAddressType()).toUpperCase());
        }
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }
}