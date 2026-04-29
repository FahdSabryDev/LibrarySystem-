package gui.admin;

import model.User;
import service.UserService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ManageUsersFrame extends JFrame {

    private User        admin;
    private UserService userService = new UserService();

    private DefaultTableModel tableModel;
    private JTable            table;

    public ManageUsersFrame(User admin) {
        this.admin = admin;
        initUI();
        loadUsers();
    }

    private void initUI() {
        setTitle("👥 إدارة الأعضاء");
        setSize(550, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel header = makeHeader("👥 إدارة الأعضاء");

        String[] cols = {"#", "اسم المستخدم", "الدور"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.getColumnModel().getColumn(0).setMaxWidth(40);

        JScrollPane scroll = new JScrollPane(table);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        btnPanel.setBackground(new Color(240, 242, 255));

        JButton btnDelete = makeBtn("🗑 حذف المستخدم المحدد", new Color(180, 40, 40));
        JButton btnBack   = makeBtn("← رجوع",                  new Color(80, 80, 80));
        btnPanel.add(btnDelete);
        btnPanel.add(btnBack);

        add(header,   BorderLayout.NORTH);
        add(scroll,   BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        btnDelete.addActionListener(e -> handleDelete());
        btnBack.addActionListener(e -> { new AdminDashboard(admin).setVisible(true); dispose(); });
    }

    private void loadUsers() {
        tableModel.setRowCount(0);
        List<User> users = userService.getAllUsers();
        for (User u : users) {
            tableModel.addRow(new Object[]{u.getId(), u.getUsername(), u.getRole()});
        }
    }

    private void handleDelete() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "اختار مستخدم الأول"); return; }
        int id   = (int) tableModel.getValueAt(row, 0);
        String name = (String) tableModel.getValueAt(row, 1);
        int confirm = JOptionPane.showConfirmDialog(this,
            "هل تريد حذف المستخدم: " + name + "؟", "تأكيد", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (userService.deleteUser(id)) { JOptionPane.showMessageDialog(this, "تم الحذف ✅"); loadUsers(); }
            else JOptionPane.showMessageDialog(this, "مش قدر يتمسح", "خطأ", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel makeHeader(String text) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(new Color(25, 80, 50));
        p.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Arial", Font.BOLD, 17));
        lbl.setForeground(Color.WHITE);
        p.add(lbl); return p;
    }

    private JButton makeBtn(String text, Color color) {
        JButton b = new JButton(text);
        b.setBackground(color); b.setForeground(Color.WHITE);
        b.setFocusPainted(false); b.setFont(new Font("Arial", Font.BOLD, 12));
        return b;
    }
}
