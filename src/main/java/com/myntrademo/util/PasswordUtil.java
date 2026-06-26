package com.myntrademo.util;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.myntrademo.constant.SecurityConstants;

public final class PasswordUtil {

    private PasswordUtil() {
    }

    public static String hashPassword(String plainPassword) {
        return BCrypt.withDefaults()
                .hashToString(SecurityConstants.BCRYPT_COST, plainPassword.toCharArray());
    }

    public static boolean verifyPassword(String plainPassword, String passwordHash) {
        BCrypt.Result result = BCrypt.verifyer()
                .verify(plainPassword.toCharArray(), passwordHash);

        return result.verified;
    }
}