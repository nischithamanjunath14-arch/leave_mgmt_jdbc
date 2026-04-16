package model;

import java.sql.Date;
import java.sql.Timestamp;

public class Leave {
    private int       id;
    private int       empId;
    private String    empName;
    private int       leaveTypeId;
    private String    leaveTypeName;
    private Date      startDate;
    private Date      endDate;
    private String    reason;
    private String    status;
    private Timestamp appliedOn;

    public Leave() {}

    public int       getId()            { return id; }
    public int       getEmpId()         { return empId; }
    public String    getEmpName()       { return empName; }
    public int       getLeaveTypeId()   { return leaveTypeId; }
    public String    getLeaveTypeName() { return leaveTypeName; }
    public Date      getStartDate()     { return startDate; }
    public Date      getEndDate()       { return endDate; }
    public String    getReason()        { return reason; }
    public String    getStatus()        { return status; }
    public Timestamp getAppliedOn()     { return appliedOn; }

    public void setId(int id)                      { this.id = id; }
    public void setEmpId(int empId)                { this.empId = empId; }
    public void setEmpName(String empName)         { this.empName = empName; }
    public void setLeaveTypeId(int id)             { this.leaveTypeId = id; }
    public void setLeaveTypeName(String name)      { this.leaveTypeName = name; }
    public void setStartDate(Date d)               { this.startDate = d; }
    public void setEndDate(Date d)                 { this.endDate = d; }
    public void setReason(String reason)           { this.reason = reason; }
    public void setStatus(String status)           { this.status = status; }
    public void setAppliedOn(Timestamp t)          { this.appliedOn = t; }
}