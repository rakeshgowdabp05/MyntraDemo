package com.myntrademo.dao;

import com.myntrademo.dto.catalog.CategoryDto;

import java.sql.SQLException;
import java.util.List;

public interface CategoryDao {

    List<CategoryDto> findAllActive() throws SQLException;
}