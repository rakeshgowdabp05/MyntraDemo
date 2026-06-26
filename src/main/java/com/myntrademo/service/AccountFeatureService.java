package com.myntrademo.service;

import com.myntrademo.dto.account.AccountFeaturePageDto;

import java.sql.SQLException;

public interface AccountFeatureService {

    AccountFeaturePageDto getPage(Long userId, String path) throws SQLException;
}