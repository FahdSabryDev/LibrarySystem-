package gui.user;

import model.Borrow;
import model.User;
import service.BorrowService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MyBorrowsFrame extends JFrame {

    private User          currentUser;
    private BorrowService borrowService = new BorrowService();
    private DefaultTableModel tableModel;
    private List<Borrow>  borrowList;

    public MyBorrowsFrame(User user) {
        this.currentUser = user;
        initUI();
        loadMyBorrows();
    }

    private void initUI() {
        setTitle("📋 استعاراتي");
        setSize(620, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(60, 80, 160));
        header.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        JLabel lbl = new JLabel("📋 استعاراتي — " + currentUser.getUsername());
        lbl.setFont(new Font("Arial", Font.BOLD, 17));
        lbl.setForeground(Color.WHITE);
        header.add(lbl);

        String[] cols = {"#", "الكتاب", "تاريخ الاستعارة", "تاريخ الإرجاع"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.getColumnModel().getColumn(0).setMaxWidth(40);

        // اللون الأحمر للسطور اللي لسه مش اترجعت
        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            public java.awt.Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                String ret = (String) tableModel.getValueAt(row, 3);
                if ("لم يُرجع بعد".equals(ret)) {
                    setBackground(sel ? new Color(255, 200, 200) : new Color(255, 235, 235));
                } else {
                    setBackground(sel ? table.getSelectionBackground() : Color.WHITE);
                }
                return this;
            }
        });

        JScrollPane scroll = new JScrollPane(table);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        btnPanel.setBackground(new Color(240, 244, 255));

        JButton btnReturn = makeBtn("↩ إرجاع الكتاب المحدد", new Color(180, 100, 20));
        JButton btnBack   = makeBtn("← رجوع",                 new Color(80, 80, 80));
        btnPanel.add(btnReturn);
        btnPanel.add(btnBack);

        add(header,   BorderLayout.NORTH);
        add(scroll,   BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        btnReturn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "اختار استعارة الأول"); return; }
            String ret = (String) tableModel.getValueAt(row, 3);
            if (!"لم يُرجع بعد".equals(ret)) {
                JOptionPane.showMessageDialog(this, "الكتاب ده اترجع بالفعل"); return;
            }
            Borrow b = borrowList.get(row);
            if (borrowService.returnBook(b.getId(), b.getBookId())) {
                JOptionPane.showMessageDialog(this, "تم إرجاع الكتاب ✅");
                loadMyBorrows();
            }
        });

        btnBack.addActionListener(e -> {
            new UserDashboard(currentUser).setVisible(true);
            dispose();
        });
    }

    private void loadMyBorrows() {
        tableModel.setRowCount(0);
        borrowList = borrowService.getUserBorrows(currentUser.getId());
        int i = 1;
        for (Borrow b : borrowList) {
            tableModel.addRow(new Object[]{i++, b.getBookTitle(), b.getBorrowDate(), b.getReturnDate()});
        }
    }

    private JButton makeBtn(String t, Color c) {
        JButton b = new JButton(t);
        b.setBackground(c); b.setForeground(Color.WHITE);
        b.setFocusPainted(false); b.setFont(new Font("Arial", Font.BOLD, 12));
        return b;
    }
}
