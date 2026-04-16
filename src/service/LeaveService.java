package service;

import db.DBConnection;
import model.Leave;
import model.LeaveBalance;
import java.sql.*;
import java.util.*;

public class LeaveService {

    // Calls stored procedure sp_apply_leave
    public String applyLeave(int empId, int leaveTypeId,
                             String startDate, String endDate, String reason) {
        Connection con = null;
        CallableStatement cs = null;
        try {
            con = DBConnection.getConnection();
            cs  = con.prepareCall("{call sp_apply_leave(?,?,?,?,?,?)}");
            cs.setInt(1, empId);
            cs.setInt(2, leaveTypeId);
            cs.setDate(3, java.sql.Date.valueOf(startDate));
            cs.setDate(4, java.sql.Date.valueOf(endDate));
            cs.setString(5, reason);
            cs.registerOutParameter(6, Types.VARCHAR);
            cs.execute();
            return cs.getString(6);
        } catch (SQLException e) {
            return "ERROR: " + e.getMessage();
        } finally {
            DBConnection.close(con, cs, null);
        }
    }

    // Calls stored procedure sp_approve_leave
    public String processLeave(int leaveId, int adminId,
                               String action, String remarks) {
        Connection con = null;
        CallableStatement cs = null;
        try {
            con = DBConnection.getConnection();
            cs  = con.prepareCall("{call sp_approve_leave(?,?,?,?,?)}");
            cs.setInt(1, leaveId);
            cs.setInt(2, adminId);
            cs.setString(3, action);
            cs.setString(4, remarks);
            cs.registerOutParameter(5, Types.VARCHAR);
            cs.execute();
            return cs.getString(5);
        } catch (SQLException e) {
            return "ERROR: " + e.getMessage();
        } finally {
            DBConnection.close(con, cs, null);
        }
    }

    // Get all leaves for an employee
    public List<Leave> getLeavesForEmployee(int empId) {
        List<Leave> list = new ArrayList<>();
        String sql = "SELECT l.id, l.emp_id, e.name AS emp_name, " +
                "lt.type_name, l.start_date, l.end_date, " +
                "l.reason, l.status, l.applied_on " +
                "FROM leaves l " +
                "JOIN employees e  ON l.emp_id = e.id " +
                "JOIN leave_types lt ON l.leave_type_id = lt.id " +
                "WHERE l.emp_id = ? ORDER BY l.applied_on DESC";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DBConnection.getConnection();
            ps  = con.prepareStatement(sql);
            ps.setInt(1, empId);
            rs  = ps.executeQuery();
            while (rs.next()) {
                Leave lv = new Leave();
                lv.setId(rs.getInt("id"));
                lv.setEmpId(rs.getInt("emp_id"));
                lv.setEmpName(rs.getString("emp_name"));
                lv.setLeaveTypeName(rs.getString("type_name"));
                lv.setStartDate(rs.getDate("start_date"));
                lv.setEndDate(rs.getDate("end_date"));
                lv.setReason(rs.getString("reason"));
                lv.setStatus(rs.getString("status"));
                lv.setAppliedOn(rs.getTimestamp("applied_on"));
                list.add(lv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(con, ps, rs);
        }
        return list;
    }

    // Get all PENDING leaves — for admin
    public List<Leave> getAllPendingLeaves() {
        List<Leave> list = new ArrayList<>();
        String sql = "SELECT l.id, l.emp_id, e.name AS emp_name, " +
                "lt.type_name, l.start_date, l.end_date, " +
                "l.reason, l.status, l.applied_on " +
                "FROM leaves l " +
                "JOIN employees e  ON l.emp_id = e.id " +
                "JOIN leave_types lt ON l.leave_type_id = lt.id " +
                "WHERE l.status = 'PENDING' ORDER BY l.applied_on";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DBConnection.getConnection();
            ps  = con.prepareStatement(sql);
            rs  = ps.executeQuery();
            while (rs.next()) {
                Leave lv = new Leave();
                lv.setId(rs.getInt("id"));
                lv.setEmpId(rs.getInt("emp_id"));
                lv.setEmpName(rs.getString("emp_name"));
                lv.setLeaveTypeName(rs.getString("type_name"));
                lv.setStartDate(rs.getDate("start_date"));
                lv.setEndDate(rs.getDate("end_date"));
                lv.setReason(rs.getString("reason"));
                lv.setStatus(rs.getString("status"));
                lv.setAppliedOn(rs.getTimestamp("applied_on"));
                list.add(lv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(con, ps, rs);
        }
        return list;
    }

    // Get leave balances for an employee
    public List<LeaveBalance> getBalances(int empId) {
        List<LeaveBalance> list = new ArrayList<>();
        String sql = "SELECT lb.id, lb.emp_id, lb.leave_type_id, " +
                "lt.type_name, lb.total_days, lb.used_days " +
                "FROM leave_balance lb " +
                "JOIN leave_types lt ON lb.leave_type_id = lt.id " +
                "WHERE lb.emp_id = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DBConnection.getConnection();
            ps  = con.prepareStatement(sql);
            ps.setInt(1, empId);
            rs  = ps.executeQuery();
            while (rs.next()) {
                LeaveBalance lb = new LeaveBalance();
                lb.setId(rs.getInt("id"));
                lb.setEmpId(rs.getInt("emp_id"));
                lb.setLeaveTypeId(rs.getInt("leave_type_id"));
                lb.setLeaveTypeName(rs.getString("type_name"));
                lb.setTotalDays(rs.getInt("total_days"));
                lb.setUsedDays(rs.getInt("used_days"));
                list.add(lb);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(con, ps, rs);
        }
        return list;
    }
}