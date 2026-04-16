package ui;

import model.Employee;
import service.AuthService;
import util.SessionTimeout;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField     emailField = new JTextField(20);
    private JPasswordField passField  = new JPasswordField(20);

    public LoginFrame() {
        setTitle("Leave Management System — Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(420, 280);
        setLocationRelativeTo(null);
        setResizable(false);
        buildUI();
        setVisible(true);
    }

    private void buildUI() {
        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Title
        JLabel title = new JLabel("Leave Management System", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        main.add(title, BorderLayout.NORTH);

        // Form
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8);
        g.anchor = GridBagConstraints.WEST;

        g.gridx = 0; g.gridy = 0;
        form.add(new JLabel("Email:"), g);
        g.gridx = 1;
        form.add(emailField, g);

        g.gridx = 0; g.gridy = 1;
        form.add(new JLabel("Password:"), g);
        g.gridx = 1;
        form.add(passField, g);

        main.add(form, BorderLayout.CENTER);

        // Button
        JButton loginBtn = new JButton("Login");
        loginBtn.setPreferredSize(new Dimension(120, 35));
        loginBtn.addActionListener(e -> handleLogin());

        // Allow Enter key to trigger login
        passField.addActionListener(e -> handleLogin());

        JPanel btnPanel = new JPanel();
        btnPanel.add(loginBtn);
        main.add(btnPanel, BorderLayout.SOUTH);

        add(main);
    }

    private void handleLogin() {
        String email    = emailField.getText().trim();
        String password = new String(passField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter both email and password.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        AuthService auth = new AuthService();
        Employee emp = auth.login(email, password);

        if (emp == null) {
            JOptionPane.showMessageDialog(this,
                    "Invalid email or password. Please try again.",
                    "Login Failed", JOptionPane.ERROR_MESSAGE);
            passField.setText("");
            return;
        }

        dispose(); // close login window

        SessionTimeout timeout = new SessionTimeout(this::showLogin);

        if ("ADMIN".equals(emp.getRole())) {
            AdminDashboard dash = new AdminDashboard(emp, timeout);
            timeout.attachTo(dash);
        } else {
            EmployeeDashboard dash = new EmployeeDashboard(emp, timeout);
            timeout.attachTo(dash);
        }
        timeout.start();
    }

    private void showLogin() {
        SwingUtilities.invokeLater(LoginFrame::new);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}