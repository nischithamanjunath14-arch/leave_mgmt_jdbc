package ui;

import model.Employee;
import model.Leave;
import model.LeaveBalance;
import service.LeaveService;
import util.SessionTimeout;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EmployeeDashboard extends JFrame {

    private final Employee       emp;
    private final SessionTimeout timeout;
    private final LeaveService   leaveService = new LeaveService();

    private JTable balanceTable;
    private JTable leaveTable;

    public EmployeeDashboard(Employee emp, SessionTimeout timeout) {
        this.emp     = emp;
        this.timeout = timeout;
        setTitle("Employee Dashboard — " + emp.getName());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        buildUI();
        loadData();
        setVisible(true);
    }

    private void buildUI() {
        // Top bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        topBar.add(new JLabel("Welcome, " + emp.getName() +
                "  |  Dept: " + emp.getDepartment()), BorderLayout.WEST);
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> logout());
        topBar.add(logoutBtn, BorderLayout.EAST);

        // Leave balance table
        String[] balCols = {"Leave Type", "Total", "Used", "Available"};
        balanceTable = new JTable(new DefaultTableModel(balCols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        });
        JScrollPane balScroll = new JScrollPane(balanceTable);
        balScroll.setBorder(BorderFactory.createTitledBorder("My Leave Balance"));
        balScroll.setPreferredSize(new Dimension(760, 120));

        // Apply leave form
        JPanel applyPanel = new JPanel(new GridBagLayout());
        applyPanel.setBorder(BorderFactory.createTitledBorder("Apply for Leave"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 8, 6, 8);

        String[] types = {"1 - Casual", "2 - Sick", "3 - Annual"};
        JComboBox<String> typeCombo = new JComboBox<>(types);
        JTextField startField  = new JTextField(10);
        JTextField endField    = new JTextField(10);
        JTextField reasonField = new JTextField(20);

        g.gridx=0; g.gridy=0; applyPanel.add(new JLabel("Leave Type:"), g);
        g.gridx=1;             applyPanel.add(typeCombo, g);
        g.gridx=2;             applyPanel.add(new JLabel("Start (YYYY-MM-DD):"), g);
        g.gridx=3;             applyPanel.add(startField, g);
        g.gridx=0; g.gridy=1; applyPanel.add(new JLabel("End (YYYY-MM-DD):"), g);
        g.gridx=1;             applyPanel.add(endField, g);
        g.gridx=2;             applyPanel.add(new JLabel("Reason:"), g);
        g.gridx=3;             applyPanel.add(reasonField, g);

        JButton applyBtn = new JButton("Submit Application");
        g.gridx=3; g.gridy=2;
        applyPanel.add(applyBtn, g);

        applyBtn.addActionListener(e -> {
            int typeId = typeCombo.getSelectedIndex() + 1;
            String result = leaveService.applyLeave(
                    emp.getId(), typeId,
                    startField.getText().trim(),
                    endField.getText().trim(),
                    reasonField.getText().trim()
            );
            if ("SUCCESS".equals(result)) {
                JOptionPane.showMessageDialog(this,
                        "Leave applied successfully!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                loadData();
                startField.setText(""); endField.setText(""); reasonField.setText("");
            } else {
                JOptionPane.showMessageDialog(this,
                        result, "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Leave history table
        String[] leaveCols = {"ID", "Type", "From", "To", "Reason", "Status", "Applied On"};
        leaveTable = new JTable(new DefaultTableModel(leaveCols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        });
        JScrollPane leaveScroll = new JScrollPane(leaveTable);
        leaveScroll.setBorder(BorderFactory.createTitledBorder("My Leave History"));

        // Layout
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        center.add(balScroll);
        center.add(Box.createVerticalStrut(10));
        center.add(applyPanel);
        center.add(Box.createVerticalStrut(10));
        center.add(leaveScroll);

        add(topBar,  BorderLayout.NORTH);
        add(new JScrollPane(center), BorderLayout.CENTER);
    }

    private void loadData() {
        // Load balance
        DefaultTableModel balModel =
                (DefaultTableModel) balanceTable.getModel();
        balModel.setRowCount(0);
        List<LeaveBalance> balances = leaveService.getBalances(emp.getId());
        for (LeaveBalance lb : balances) {
            balModel.addRow(new Object[]{
                    lb.getLeaveTypeName(),
                    lb.getTotalDays(),
                    lb.getUsedDays(),
                    lb.getAvailable()
            });
        }

        // Load history
        DefaultTableModel leaveModel =
                (DefaultTableModel) leaveTable.getModel();
        leaveModel.setRowCount(0);
        List<Leave> leaves = leaveService.getLeavesForEmployee(emp.getId());
        for (Leave lv : leaves) {
            leaveModel.addRow(new Object[]{
                    lv.getId(),
                    lv.getLeaveTypeName(),
                    lv.getStartDate(),
                    lv.getEndDate(),
                    lv.getReason(),
                    lv.getStatus(),
                    lv.getAppliedOn()
            });
        }
    }

    private void logout() {
        timeout.stop();
        dispose();
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}