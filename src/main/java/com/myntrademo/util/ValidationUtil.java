package com.myntrademo.util;

import com.myntrademo.constant.SecurityConstants;

import java.util.regex.Pattern;

public final class ValidationUtil {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^[6-9][0-9]{9}$");

    private static final Pattern FULL_NAME_PATTERN =
            Pattern.compile("^[A-Za-z][A-Za-z .'-]{1,118}[A-Za-z]$");

    private ValidationUtil() {
    }

    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static String clean(String value) {
        return value == null ? null : value.trim();
    }

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isValidPhone(String phone) {
        return phone == null || phone.trim().isEmpty() || PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    public static boolean isValidFullName(String fullName) {
        return fullName != null && FULL_NAME_PATTERN.matcher(fullName.trim()).matches();
    }

    public static boolean isValidPassword(String password) {
        return password != null
                && password.length() >= SecurityConstants.MIN_PASSWORD_LENGTH
                && password.length() <= SecurityConstants.MAX_PASSWORD_LENGTH;
    }
}