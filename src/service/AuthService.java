package service;

import db.DBConnection;
import model.Employee;
import java.security.MessageDigest;
import java.sql.*;

public class AuthService {

    public Employee login(String email, String password) {
        String hashed  = sha256(password);
        String sql     = "SELECT id, name, email, role, department " +
                "FROM employees WHERE email=? AND password=?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs   = null;
        try {
            con = DBConnection.getConnection();
            ps  = con.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, hashed);
            rs  = ps.executeQuery();
            if (rs.next()) {
                Employee emp = new Employee();
                emp.setId(rs.getInt("id"));
                emp.setName(rs.getString("name"));
                emp.setEmail(rs.getString("email"));
                emp.setRole(rs.getString("role"));
                emp.setDepartment(rs.getString("department"));
                return emp;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(con, ps, rs);
        }
        return null;
    }

    private String sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash)
                sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
