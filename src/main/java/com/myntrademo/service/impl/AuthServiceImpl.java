package com.myntrademo.service.impl;

import com.myntrademo.constant.MessageConstants;
import com.myntrademo.constant.RoleConstants;
import com.myntrademo.dao.RoleDao;
import com.myntrademo.dao.UserDao;
import com.myntrademo.dao.impl.JdbcRoleDao;
import com.myntrademo.dao.impl.JdbcUserDao;
import com.myntrademo.dto.AuthenticatedUser;
import com.myntrademo.dto.LoginRequest;
import com.myntrademo.dto.RegisterRequest;
import com.myntrademo.model.User;
import com.myntrademo.service.AuthService;
import com.myntrademo.util.PasswordUtil;
import com.myntrademo.util.ValidationUtil;

import java.sql.SQLException;
import java.util.Locale;

public class AuthServiceImpl implements AuthService {

    private final UserDao userDao;
    private final RoleDao roleDao;

    public AuthServiceImpl() {
        this.userDao = new JdbcUserDao();
        this.roleDao = new JdbcRoleDao();
    }

    public AuthServiceImpl(UserDao userDao, RoleDao roleDao) {
        this.userDao = userDao;
        this.roleDao = roleDao;
    }

    @Override
    public void registerCustomer(RegisterRequest registerRequest) throws SQLException {
        String cleanedFullName = ValidationUtil.clean(registerRequest.getFullName());
        String cleanedEmail = ValidationUtil.clean(registerRequest.getEmail());
        String cleanedPhone = ValidationUtil.clean(registerRequest.getPhone());

        validateRegistrationInput(
                cleanedFullName,
                cleanedEmail,
                cleanedPhone,
                registerRequest.getPassword(),
                registerRequest.getConfirmPassword()
        );

        String normalizedEmail = cleanedEmail.toLowerCase(Locale.ROOT);
        String normalizedPhone = ValidationUtil.isBlank(cleanedPhone) ? null : cleanedPhone;

        if (userDao.existsByEmail(normalizedEmail)) {
            throw new IllegalArgumentException(MessageConstants.EMAIL_ALREADY_EXISTS);
        }

        if (normalizedPhone != null && userDao.existsByPhone(normalizedPhone)) {
            throw new IllegalArgumentException(MessageConstants.PHONE_ALREADY_EXISTS);
        }

        Long customerRoleId = roleDao.findRoleIdByName(RoleConstants.CUSTOMER)
                .orElseThrow(() -> new IllegalStateException(MessageConstants.CUSTOMER_ROLE_NOT_FOUND));

        User user = new User();
        user.setRoleId(customerRoleId);
        user.setFullName(cleanedFullName);
        user.setEmail(normalizedEmail);
        user.setPhone(normalizedPhone);
        user.setPasswordHash(PasswordUtil.hashPassword(registerRequest.getPassword()));

        userDao.createCustomer(user);
    }

    @Override
    public AuthenticatedUser login(LoginRequest loginRequest) throws SQLException {
        String cleanedEmail = ValidationUtil.clean(loginRequest.getEmail());

        if (ValidationUtil.isBlank(cleanedEmail) || ValidationUtil.isBlank(loginRequest.getPassword())) {
            throw new IllegalArgumentException(MessageConstants.INVALID_LOGIN);
        }

        String normalizedEmail = cleanedEmail.toLowerCase(Locale.ROOT);

        User user = userDao.findByEmailForLogin(normalizedEmail)
                .orElseThrow(() -> new IllegalArgumentException(MessageConstants.INVALID_LOGIN));

        if (!user.isActive()) {
            throw new IllegalArgumentException(MessageConstants.INACTIVE_ACCOUNT);
        }

        boolean passwordMatches = PasswordUtil.verifyPassword(
                loginRequest.getPassword(),
                user.getPasswordHash()
        );

        if (!passwordMatches) {
            throw new IllegalArgumentException(MessageConstants.INVALID_LOGIN);
        }

        userDao.updateLastLogin(user.getUserId());

        return new AuthenticatedUser(
                user.getUserId(),
                user.getFullName(),
                user.getEmail(),
                user.getRoleName()
        );
    }

    private void validateRegistrationInput(
            String fullName,
            String email,
            String phone,
            String password,
            String confirmPassword
    ) {
        if (ValidationUtil.isBlank(fullName)) {
            throw new IllegalArgumentException(MessageConstants.REQUIRED_FULL_NAME);
        }

        if (!ValidationUtil.isValidFullName(fullName)) {
            throw new IllegalArgumentException(MessageConstants.INVALID_FULL_NAME);
        }

        if (ValidationUtil.isBlank(email)) {
            throw new IllegalArgumentException(MessageConstants.REQUIRED_EMAIL);
        }

        if (!ValidationUtil.isValidEmail(email)) {
            throw new IllegalArgumentException(MessageConstants.INVALID_EMAIL);
        }

        if (!ValidationUtil.isValidPhone(phone)) {
            throw new IllegalArgumentException(MessageConstants.INVALID_PHONE);
        }

        if (ValidationUtil.isBlank(password)) {
            throw new IllegalArgumentException(MessageConstants.REQUIRED_PASSWORD);
        }

        if (ValidationUtil.isBlank(confirmPassword)) {
            throw new IllegalArgumentException(MessageConstants.REQUIRED_CONFIRM_PASSWORD);
        }

        if (!ValidationUtil.isValidPassword(password)) {
            throw new IllegalArgumentException(MessageConstants.INVALID_PASSWORD);
        }

        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException(MessageConstants.PASSWORD_MISMATCH);
        }
    }
}