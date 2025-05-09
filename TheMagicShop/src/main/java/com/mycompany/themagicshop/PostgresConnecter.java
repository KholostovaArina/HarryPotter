package com.mycompany.themagicshop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresConnecter {
    private static final String URL = "jdbc:postgresql://localhost:5433/study";
    private static final String USER = "arinochka";
    private static final String PASSWORD = "qwerty";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}