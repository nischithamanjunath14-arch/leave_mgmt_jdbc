package db;

import java.sql.*;

public class DBConnection {
    private static final String URL  = "jdbc:mysql://localhost:3306/leave_mgmt";
    private static final String USER = "root";
    private static final String PASS = "password"; // your password here

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL Driver not found!", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static void close(Connection con, Statement stmt, ResultSet rs) {
        try { if (rs   != null) rs.close();   } catch (SQLException ignored) {}
        try { if (stmt != null) stmt.close(); } catch (SQLException ignored) {}
        try { if (con  != null) con.close();  } catch (SQLException ignored) {}
    }
}