package com.mycompany.themagicshop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresConnecter {

    public static void connect() {
        Connection connection = null;
        try {
            String url = "jdbc:postgresql://localhost:5433/study";
            String user = "arinochka";
            String password = "qwerty";
            connection = DriverManager.getConnection(url, user, password);
            
            if (connection != null) {
                System.out.println("Successful connection");
            }
        } catch (SQLException e) {
            System.out.println("Database connection error : " + e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing the connection: " + e.getMessage());
            }
        }
    }
}
