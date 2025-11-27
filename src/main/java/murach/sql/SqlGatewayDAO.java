package murach.sql;

import java.sql.*;

public class SqlGatewayDAO {
    
    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private final String DB_URL = "jdbc:postgresql://localhost:5432/murach_gateway";
    private final String DB_USER = "postgres";
    private final String DB_PASS = "postgres";

    public String executeSql(String sql) {
        String result = "";

        if (sql == null || sql.trim().isEmpty()) {
            return "<p>No SQL statement provided.</p>";
        }

        sql = sql.trim();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             Statement stmt = conn.createStatement()) {

            if (sql.toLowerCase().startsWith("select")) {

                // SELECT → trả bảng HTML
                try (ResultSet rs = stmt.executeQuery(sql)) {
                    result = SQLUtil.getHtmlTable(rs);
                }

            } else {

                // INSERT / UPDATE / DELETE / DDL
                int affected = stmt.executeUpdate(sql);

                if (affected == 0) {
                    result = "<p>The statement executed successfully.</p>";
                } else {
                    result = "<p>The statement executed successfully.<br>"
                            + affected + " row(s) affected.</p>";
                }
            }

        } catch (SQLException ex) {
            result = "<p>Error executing SQL:<br>" + ex.getMessage() + "</p>";
        }

        return result;
    }
}