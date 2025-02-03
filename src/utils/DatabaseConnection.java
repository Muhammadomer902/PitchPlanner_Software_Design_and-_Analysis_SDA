package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/pitch_planner";
    private static final String USER = "root";
    private static final String PASSWORD = "pitchplanner";

    private static Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Database connected successfully!");
            } catch (SQLException e) {
                System.out.println("Database connection Failed!");
                e.printStackTrace();
            }
        }
        return connection;
    }
}
