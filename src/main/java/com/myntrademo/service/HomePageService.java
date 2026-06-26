package com.myntrademo.service;

import com.myntrademo.dto.home.HomePageDto;

import java.sql.SQLException;

public interface HomePageService {

    HomePageDto getHomePage() throws SQLException;
}