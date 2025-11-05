// src/Controller/DBContext.java
package Controller;

import java.sql.*;

public class DBContext {
    public static final String URL = "jdbc:mysql://localhost:3306/sims?serverTimezone=UTC";
    public static final String USER = "root";
    public static final String PASSWORD = "";

    // Remove static connection and create new connection each time
    public static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database Connection: Established");
            return connection;
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            return null;
        }
    }

    // Optional: Close connection method for manual control
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connection Closed");
            } catch (SQLException e) {
                System.err.println("Failed to close connection: " + e.getMessage());
            }
        }
    }
}