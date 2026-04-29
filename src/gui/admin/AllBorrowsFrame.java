package gui.admin;

import model.Borrow;
import model.User;
import service.BorrowService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AllBorrowsFrame extends JFrame {

    private User          admin;
    private BorrowService borrowService = new BorrowService();
    private DefaultTableModel tableModel;

    public AllBorrowsFrame(User admin) {
        this.admin = admin;
        initUI();
        loadBorrows();
    }

    private void initUI() {
        setTitle("📋 كل الاستعارات");
        setSize(680, 430);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel header = makeHeader("📋 سجل الاستعارات");

        String[] cols = {"#", "المستخدم", "الكتاب", "تاريخ الاستعارة", "تاريخ الإرجاع"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.getColumnModel().getColumn(0).setMaxWidth(40);

        JScrollPane scroll = new JScrollPane(table);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        btnPanel.setBackground(new Color(245, 245, 255));
        JButton btnBack = makeBtn("← رجوع للوحة التحكم", new Color(80, 80, 80));
        btnPanel.add(btnBack);

        add(header,   BorderLayout.NORTH);
        add(scroll,   BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        btnBack.addActionListener(e -> { new AdminDashboard(admin).setVisible(true); dispose(); });
    }

    private void loadBorrows() {
        tableModel.setRowCount(0);
        List<Borrow> list = borrowService.getAllBorrows();
        int i = 1;
        for (Borrow b : list) {
            tableModel.addRow(new Object[]{
                i++, b.getUsername(), b.getBookTitle(), b.getBorrowDate(), b.getReturnDate()
            });
        }
    }

    private JPanel makeHeader(String text) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(new Color(100, 60, 20));
        p.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Arial", Font.BOLD, 17));
        lbl.setForeground(Color.WHITE);
        p.add(lbl); return p;
    }

    private JButton makeBtn(String t, Color c) {
        JButton b = new JButton(t);
        b.setBackground(c); b.setForeground(Color.WHITE);
        b.setFocusPainted(false); b.setFont(new Font("Arial", Font.BOLD, 12));
        return b;
    }
}
