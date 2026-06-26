package com.myntrademo.dao;

import java.sql.SQLException;
import java.util.Optional;

public interface RoleDao {

    Optional<Long> findRoleIdByName(String roleName) throws SQLException;
}