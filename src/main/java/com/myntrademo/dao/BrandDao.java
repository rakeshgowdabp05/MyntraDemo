package com.myntrademo.dao;

import com.myntrademo.dto.catalog.BrandDto;

import java.sql.SQLException;
import java.util.List;

public interface BrandDao {

    List<BrandDto> findAllActive() throws SQLException;
}