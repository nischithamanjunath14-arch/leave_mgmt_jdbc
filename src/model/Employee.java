package model;

public class Employee {
    private int    id;
    private String name;
    private String email;
    private String password;
    private String role;
    private String department;

    public Employee() {}

    public Employee(int id, String name, String email,
                    String role, String department) {
        this.id         = id;
        this.name       = name;
        this.email      = email;
        this.role       = role;
        this.department = department;
    }

    public int    getId()         { return id; }
    public String getName()       { return name; }
    public String getEmail()      { return email; }
    public String getPassword()   { return password; }
    public String getRole()       { return role; }
    public String getDepartment() { return department; }

    public void setId(int id)               { this.id = id; }
    public void setName(String name)        { this.name = name; }
    public void setEmail(String email)      { this.email = email; }
    public void setPassword(String pw)      { this.password = pw; }
    public void setRole(String role)        { this.role = role; }
    public void setDepartment(String dept)  { this.department = dept; }

    @Override
    public String toString() { return name + " (" + role + ")"; }
}
