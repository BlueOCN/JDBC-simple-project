package main.java.com.blueocn.data.util;

import java.sql.*;
import java.util.logging.Logger;

public class DatabaseUtils {
    private static final String URL = System.getenv("DB_URL");
    private static final String USERNAME = System.getenv("DB_USERNAME");
    private static final String PASSWORD = System.getenv("DB_PASSWORD");
    private static final Logger LOGGER = Logger.getLogger(DatabaseUtils.class.getName());
    private static final String exceptionFormat = "exception in %s, message: %s, code: %s";
    private static Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            synchronized (DatabaseUtils.class) {
                if (connection == null) {
                    try {
                        connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                    } catch (SQLException e) {
                        handleSqlException("DatabaseUtils.getConnection", e, LOGGER);
                    }
                }
            }
        }
        return connection;
    }

    public static void handleSqlException(String method, SQLException e, Logger logger) {
        logger.warning(String.format(exceptionFormat, method, e.getSQLState(), e.getErrorCode()));
        throw new RuntimeException(e);
    }
}
