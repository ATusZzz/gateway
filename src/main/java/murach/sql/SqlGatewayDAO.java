package murach.sql;

import java.sql.*;
// Đã xóa dòng import murach.util.SQLUtil vì nó nằm cùng gói

public class SqlGatewayDAO {

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL Driver not found: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws SQLException {
        String dbUrl = System.getenv("DB_URL");
        String dbUser = System.getenv("DB_USER");
        String dbPass = System.getenv("DB_PASSWORD");

        if (dbUrl == null || dbUrl.isEmpty()) {
            dbUrl = "jdbc:postgresql://localhost:5432/murach_gateway";
            dbUser = "postgres";
            dbPass = "postgres"; 
        }

        return DriverManager.getConnection(dbUrl, dbUser, dbPass);
    }

    public String executeSql(String sql) {
        String result = "";
        if (sql == null || sql.trim().isEmpty()) {
            return "<p>No SQL statement provided.</p>";
        }
        sql = sql.trim();

        try (Connection conn = this.getConnection();
             Statement stmt = conn.createStatement()) {

            if (sql.toLowerCase().startsWith("select")) {
                try (ResultSet rs = stmt.executeQuery(sql)) {
                    result = SQLUtil.getHtmlTable(rs);
                }
            } else {
                int affected = stmt.executeUpdate(sql);
                result = "<p>Statement executed successfully.<br>" + affected + " row(s) affected.</p>";
            }
        } catch (SQLException ex) {
            result = "<p>Error executing SQL:<br>" + ex.getMessage() + "</p>";
            ex.printStackTrace();
        }
        return result;
    }
}