package com.myntrademo.service.impl;

import com.myntrademo.constant.MessageConstants;
import com.myntrademo.dao.UserDao;
import com.myntrademo.dao.impl.JdbcUserDao;
import com.myntrademo.model.User;
import com.myntrademo.service.ProfileService;
import com.myntrademo.util.ValidationUtil;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Locale;

public class ProfileServiceImpl implements ProfileService {

    private static final String GENDER_MALE = "MALE";
    private static final String GENDER_FEMALE = "FEMALE";
    private static final String GENDER_OTHER = "OTHER";

    private final UserDao userDao;

    public ProfileServiceImpl() {
        this.userDao = new JdbcUserDao();
    }

    public ProfileServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User getProfile(Long userId) throws SQLException {
        validateUser(userId);

        return userDao.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found."));
    }

    @Override
    public User updateProfile(Long userId, User profile) throws SQLException {
        validateUser(userId);

        User existingUser = getProfile(userId);

        String fullName = ValidationUtil.clean(profile.getFullName());
        String phone = ValidationUtil.clean(profile.getPhone());
        String gender = normalizeGender(profile.getGender());
        LocalDate dateOfBirth = profile.getDateOfBirth();

        if (ValidationUtil.isBlank(fullName)) {
            throw new IllegalArgumentException(MessageConstants.REQUIRED_FULL_NAME);
        }

        if (!ValidationUtil.isValidFullName(fullName)) {
            throw new IllegalArgumentException(MessageConstants.INVALID_FULL_NAME);
        }

        if (!ValidationUtil.isValidPhone(phone)) {
            throw new IllegalArgumentException(MessageConstants.INVALID_PHONE);
        }

        if (!ValidationUtil.isBlank(phone) && userDao.existsByPhoneForOtherUser(phone, userId)) {
            throw new IllegalArgumentException(MessageConstants.PHONE_ALREADY_EXISTS);
        }

        if (dateOfBirth != null && dateOfBirth.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date of birth cannot be in the future.");
        }

        existingUser.setFullName(fullName);
        existingUser.setPhone(ValidationUtil.isBlank(phone) ? null : phone);
        existingUser.setGender(gender);
        existingUser.setDateOfBirth(dateOfBirth);

        userDao.updateProfile(existingUser);

        return getProfile(userId);
    }

    private void validateUser(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException(MessageConstants.AUTH_REQUIRED);
        }
    }

    private String normalizeGender(String gender) {
        if (ValidationUtil.isBlank(gender)) {
            return null;
        }

        String normalizedGender = gender.trim().toUpperCase(Locale.ROOT);

        return switch (normalizedGender) {
            case GENDER_MALE, GENDER_FEMALE, GENDER_OTHER -> normalizedGender;
            default -> null;
        };
    }
}