package model;

public class LeaveBalance {
    private int    id;
    private int    empId;
    private int    leaveTypeId;
    private String leaveTypeName;
    private int    totalDays;
    private int    usedDays;

    public int    getId()            { return id; }
    public int    getEmpId()         { return empId; }
    public int    getLeaveTypeId()   { return leaveTypeId; }
    public String getLeaveTypeName() { return leaveTypeName; }
    public int    getTotalDays()     { return totalDays; }
    public int    getUsedDays()      { return usedDays; }
    public int    getAvailable()     { return totalDays - usedDays; }

    public void setId(int id)                 { this.id = id; }
    public void setEmpId(int empId)           { this.empId = empId; }
    public void setLeaveTypeId(int id)        { this.leaveTypeId = id; }
    public void setLeaveTypeName(String name) { this.leaveTypeName = name; }
    public void setTotalDays(int d)           { this.totalDays = d; }
    public void setUsedDays(int d)            { this.usedDays = d; }
}
