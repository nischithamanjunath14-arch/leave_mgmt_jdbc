package ui;

import model.Employee;
import model.Leave;
import service.LeaveService;
import util.SessionTimeout;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminDashboard extends JFrame {

    private final Employee       admin;
    private final SessionTimeout timeout;
    private final LeaveService   leaveService = new LeaveService();
    private JTable pendingTable;

    public AdminDashboard(Employee admin, SessionTimeout timeout) {
        this.admin   = admin;
        this.timeout = timeout;
        setTitle("Admin Dashboard — " + admin.getName());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        buildUI();
        loadPending();
        setVisible(true);
    }

    private void buildUI() {
        // Top bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        topBar.add(new JLabel("Admin: " + admin.getName()), BorderLayout.WEST);
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> logout());
        topBar.add(logoutBtn, BorderLayout.EAST);

        // Pending leaves table
        String[] cols = {"ID", "Employee", "Type", "From", "To", "Reason", "Applied On"};
        pendingTable = new JTable(new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        });
        pendingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(pendingTable);
        scroll.setBorder(BorderFactory.createTitledBorder("Pending Leave Applications"));

        // Action panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JTextField remarksField = new JTextField(25);
        remarksField.setToolTipText("Enter remarks (optional)");
        JButton approveBtn = new JButton("Approve");
        JButton rejectBtn  = new JButton("Reject");
        approveBtn.setBackground(new Color(70, 160, 70));
        approveBtn.setForeground(Color.WHITE);
        rejectBtn.setBackground(new Color(200, 60, 60));
        rejectBtn.setForeground(Color.WHITE);

        actionPanel.add(new JLabel("Remarks:"));
        actionPanel.add(remarksField);
        actionPanel.add(approveBtn);
        actionPanel.add(rejectBtn);
        actionPanel.setBorder(BorderFactory.createTitledBorder("Action on Selected Leave"));

        approveBtn.addActionListener(e ->
                processSelected("APPROVED", remarksField.getText()));
        rejectBtn.addActionListener(e ->
                processSelected("REJECTED", remarksField.getText()));

        add(topBar,      BorderLayout.NORTH);
        add(scroll,      BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);
    }

    private void loadPending() {
        DefaultTableModel model = (DefaultTableModel) pendingTable.getModel();
        model.setRowCount(0);
        List<Leave> list = leaveService.getAllPendingLeaves();
        for (Leave lv : list) {
            model.addRow(new Object[]{
                    lv.getId(),
                    lv.getEmpName(),
                    lv.getLeaveTypeName(),
                    lv.getStartDate(),
                    lv.getEndDate(),
                    lv.getReason(),
                    lv.getAppliedOn()
            });
        }
    }

    private void processSelected(String action, String remarks) {
        int row = pendingTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a leave application first.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int leaveId = (int) pendingTable.getValueAt(row, 0);
        String result = leaveService.processLeave(
                leaveId, admin.getId(), action, remarks);

        if ("SUCCESS".equals(result)) {
            JOptionPane.showMessageDialog(this,
                    "Leave " + action.toLowerCase() + " successfully!",
                    "Done", JOptionPane.INFORMATION_MESSAGE);
            loadPending();
        } else {
            JOptionPane.showMessageDialog(this,
                    result, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void logout() {
        timeout.stop();
        dispose();
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}