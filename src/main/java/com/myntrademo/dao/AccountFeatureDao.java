package com.myntrademo.dao;

import com.myntrademo.dto.account.AccountFeatureItemDto;

import java.sql.SQLException;
import java.util.List;

public interface AccountFeatureDao {

    List<AccountFeatureItemDto> findGiftCards(Long userId) throws SQLException;

    List<AccountFeatureItemDto> findCreditTransactions(Long userId) throws SQLException;

    List<AccountFeatureItemDto> findCoupons(Long userId) throws SQLException;

    List<AccountFeatureItemDto> findSavedCards(Long userId) throws SQLException;

    List<AccountFeatureItemDto> findSavedVpa(Long userId) throws SQLException;
}