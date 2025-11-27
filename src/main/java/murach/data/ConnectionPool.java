package murach.data;

import java.sql.*;

public class ConnectionPool {

    private static ConnectionPool pool = null;

    private ConnectionPool() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver not found: " + e);
        }
    }

    public static synchronized ConnectionPool getInstance() {
        if (pool == null) {
            pool = new ConnectionPool();
        }
        return pool;
    }

    public Connection getConnection() {
        try {
            String dbUrl = System.getenv("DB_URL");
            String dbUser = System.getenv("DB_USER");
            String dbPass = System.getenv("DB_PASSWORD");

            if (dbUrl == null) {
                // Cấu hình Localhost - Ông nhớ check lại pass máy ông nha
                dbUrl = "jdbc:postgresql://localhost:5432/murach_gateway";
                dbUser = "postgres";
                dbPass = "postgres"; 
            }

            return DriverManager.getConnection(dbUrl, dbUser, dbPass);
        } catch (SQLException e) {
            System.out.println("Connection Failed: " + e);
            return null;
        }
    }

    public void freeConnection(Connection c) {
        try {
            if (c != null) {
                c.close();
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
}