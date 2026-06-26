package com.myntrademo.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnection {

    private static final String DB_DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";

    private static final String DB_URL_ENV = "MYNTRADEMO_DB_URL";
    private static final String DB_USERNAME_ENV = "MYNTRADEMO_DB_USERNAME";
    private static final String DB_PASSWORD_ENV = "MYNTRADEMO_DB_PASSWORD";

    private DBConnection() {
    }

    public static Connection getConnection() throws SQLException {
        loadDriver();

        String dbUrl = getRequiredEnv(DB_URL_ENV);
        String dbUsername = getRequiredEnv(DB_USERNAME_ENV);
        String dbPassword = getRequiredEnv(DB_PASSWORD_ENV);

        return DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
    }

    private static void loadDriver() {
        try {
            Class.forName(DB_DRIVER_CLASS);
        } catch (ClassNotFoundException exception) {
            throw new IllegalStateException("MySQL JDBC driver not found in application classpath.", exception);
        }
    }

    private static String getRequiredEnv(String key) {
        String value = System.getenv(key);

        if (value == null || value.trim().isEmpty()) {
            throw new IllegalStateException("Missing required environment variable: " + key);
        }

        return value.trim();
    }
}