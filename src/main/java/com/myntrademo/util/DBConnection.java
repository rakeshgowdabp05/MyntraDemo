package com.myntrademo.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnection {

    private static final String DB_DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";

    private static final String DB_URL_ENV = "MYNTRADEMO_DB_URL";
    private static final String DB_HOST_ENV = "MYNTRADEMO_DB_HOST";
    private static final String DB_PORT_ENV = "MYNTRADEMO_DB_PORT";
    private static final String DB_NAME_ENV = "MYNTRADEMO_DB_NAME";
    private static final String DB_USERNAME_ENV = "MYNTRADEMO_DB_USERNAME";
    private static final String DB_PASSWORD_ENV = "MYNTRADEMO_DB_PASSWORD";

    private static final String DEFAULT_DB_PORT = "3306";
    private static final String DEFAULT_DB_NAME = "myntrademo_db";

    private DBConnection() {
    }

    public static Connection getConnection() throws SQLException {
        loadDriver();

        String dbUrl = buildDatabaseUrl();
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

    private static String buildDatabaseUrl() {
        String explicitUrl = getOptionalEnv(DB_URL_ENV);

        if (!explicitUrl.isBlank()) {
            return explicitUrl;
        }

        String host = getRequiredEnv(DB_HOST_ENV);
        String port = getOptionalEnv(DB_PORT_ENV);
        String databaseName = getOptionalEnv(DB_NAME_ENV);

        if (port.isBlank()) {
            port = DEFAULT_DB_PORT;
        }

        if (databaseName.isBlank()) {
            databaseName = DEFAULT_DB_NAME;
        }

        return "jdbc:mysql://"
                + host
                + ":"
                + port
                + "/"
                + databaseName
                + "?sslMode=REQUIRED"
                + "&useUnicode=true"
                + "&characterEncoding=UTF-8"
                + "&serverTimezone=Asia/Kolkata"
                + "&allowPublicKeyRetrieval=true";
    }

    private static String getRequiredEnv(String key) {
        String value = getOptionalEnv(key);

        if (value.isBlank()) {
            throw new IllegalStateException("Missing required environment variable: " + key);
        }

        return value;
    }

    private static String getOptionalEnv(String key) {
        String value = System.getenv(key);

        if (value == null) {
            return "";
        }

        return value.trim();
    }
}