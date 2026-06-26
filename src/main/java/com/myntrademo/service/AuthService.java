package com.myntrademo.service;

import com.myntrademo.dto.AuthenticatedUser;
import com.myntrademo.dto.LoginRequest;
import com.myntrademo.dto.RegisterRequest;

import java.sql.SQLException;

public interface AuthService {

    void registerCustomer(RegisterRequest registerRequest) throws SQLException;

    AuthenticatedUser login(LoginRequest loginRequest) throws SQLException;
}