package com.myntrademo.dao;

import com.myntrademo.model.User;

import java.sql.SQLException;
import java.util.Optional;

public interface UserDao {

    boolean existsByEmail(String email) throws SQLException;

    boolean existsByPhone(String phone) throws SQLException;

    boolean existsByPhoneForOtherUser(String phone, Long userId) throws SQLException;

    Long createCustomer(User user) throws SQLException;

    Optional<User> findByEmailForLogin(String email) throws SQLException;

    Optional<User> findById(Long userId) throws SQLException;

    void updateProfile(User user) throws SQLException;

    void updateLastLogin(Long userId) throws SQLException;
}