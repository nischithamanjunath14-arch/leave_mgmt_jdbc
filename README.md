# Leave Management System (JDBC)

A robust, console-based Leave Management System built using **Java** and **JDBC (Java Database Connectivity)**. This application manages employee data and streamlines the process of requesting and approving leaves, maintaining real-time updates directly in a relational database.

## 🚀 Features

*   **Employee Management:** Add, update, and view employee profiles and their department details.
*   **Leave Application:** Employees can apply for different types of leave (e.g., Casual Leave, Sick Leave, Earned Leave) by specifying dates and reasons.
*   **Approval Workflow:** Managers/Admins can review pending leave applications and mark them as *Approved* or *Rejected*.
*   **Leave Balance Tracking:** Automatically tracks and updates remaining leave balances for employees upon approval.
*   **Persistent Storage:** Uses JDBC to perform safe, structural CRUD operations directly on a SQL database.

---

## 🛠️ Tech Stack & Prerequisites

*   **Java Development Kit (JDK):** Version 8 or higher.
*   **Database:** MySQL or PostgreSQL.
*   **Driver:** JDBC Database Connector jar (e.g., `mysql-connector-j`).
*   **IDE:** IntelliJ IDEA, Eclipse, or VS Code.

---

## 📋 Database Setup

Before running the application, you need to set up the relational database tables. Run the following SQL schema in your database client:

```sql
CREATE DATABASE leave_mgmt_db;
USE leave_mgmt_db;

-- 1. Employees Table
CREATE TABLE employees (
    employee_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    department VARCHAR(50),
    role VARCHAR(30) DEFAULT 'Employee'
);

-- 2. Leave Requests Table
CREATE TABLE leave_requests (
    request_id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id INT,
    leave_type VARCHAR(50) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    reason TEXT,
    status VARCHAR(20) DEFAULT 'Pending',
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE
);  

-- 3. Leave Balance Table
CREATE TABLE leave_balances (
    employee_id INT PRIMARY KEY,
    sick_leave INT DEFAULT 10,
    casual_leave INT DEFAULT 10,
    earned_leave INT DEFAULT 15,
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE
);
