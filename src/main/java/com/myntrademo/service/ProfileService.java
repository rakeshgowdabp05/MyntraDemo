package com.myntrademo.service;

import com.myntrademo.model.User;

import java.sql.SQLException;

public interface ProfileService {

    User getProfile(Long userId) throws SQLException;

    User updateProfile(Long userId, User profile) throws SQLException;
}