package gui.admin;

import model.Book;
import model.User;
import service.BookService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * ManageBooksFrame — إدارة الكتب (Add / Update / Delete / View All)
 */
public class ManageBooksFrame extends JFrame {

    private User        admin;
    private BookService bookService = new BookService();

    private DefaultTableModel tableModel;
    private JTable            table;
    private JTextField        txtTitle, txtAuthor, txtQty;
    private JButton           btnAdd, btnUpdate, btnDelete, btnClear;

    public ManageBooksFrame(User admin) {
        this.admin = admin;
        initUI();
        loadBooks();
    }

    private void initUI() {
        setTitle("📖 إدارة الكتب");
        setSize(700, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ===== Header =====
        JPanel header = makeHeader("📖 إدارة الكتب");

        // ===== الجدول =====
        String[] cols = {"#", "عنوان الكتاب", "المؤلف", "الكمية"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.setSelectionBackground(new Color(190, 210, 255));
        table.getColumnModel().getColumn(0).setMaxWidth(40);
        table.getColumnModel().getColumn(3).setMaxWidth(70);

        JScrollPane scroll = new JScrollPane(table);

        // ===== فورم الإدخال =====
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("بيانات الكتاب"));
        form.setBackground(new Color(248, 249, 255));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 8, 6, 8);
        g.fill   = GridBagConstraints.HORIZONTAL;

        txtTitle  = new JTextField(18);
        txtAuthor = new JTextField(18);
        txtQty    = new JTextField(5);

        g.gridx=0; g.gridy=0; form.add(new JLabel("عنوان الكتاب:"), g);
        g.gridx=1;             form.add(txtTitle, g);
        g.gridx=2;             form.add(new JLabel("المؤلف:"), g);
        g.gridx=3;             form.add(txtAuthor, g);
        g.gridx=4;             form.add(new JLabel("الكمية:"), g);
        g.gridx=5;             form.add(txtQty, g);

        // ===== الأزرار =====
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        btnPanel.setBackground(new Color(240, 242, 255));

        btnAdd    = makeBtn("➕ إضافة",  new Color(40, 140, 70));
        btnUpdate = makeBtn("✏ تعديل",  new Color(40, 100, 190));
        btnDelete = makeBtn("🗑 حذف",    new Color(180, 40, 40));
        btnClear  = makeBtn("🔄 مسح",   new Color(120, 120, 120));
        JButton btnBack = makeBtn("← رجوع", new Color(80, 80, 80));

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);
        btnPanel.add(btnBack);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(form,     BorderLayout.CENTER);
        bottom.add(btnPanel, BorderLayout.SOUTH);

        add(header, BorderLayout.NORTH);
        add(scroll,  BorderLayout.CENTER);
        add(bottom,  BorderLayout.SOUTH);

        // ===== الأحداث =====

        // اختيار صف يملأ الفورم
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                txtTitle.setText( (String) tableModel.getValueAt(row, 1));
                txtAuthor.setText((String) tableModel.getValueAt(row, 2));
                txtQty.setText(   tableModel.getValueAt(row, 3).toString());
            }
        });

        btnAdd.addActionListener(e -> handleAdd());
        btnUpdate.addActionListener(e -> handleUpdate());
        btnDelete.addActionListener(e -> handleDelete());
        btnClear.addActionListener(e -> clearForm());

        btnBack.addActionListener(e -> {
            new AdminDashboard(admin).setVisible(true);
            dispose();
        });
    }

    private void loadBooks() {
        tableModel.setRowCount(0);
        List<Book> books = bookService.getAllBooks();
        for (Book b : books) {
            tableModel.addRow(new Object[]{b.getId(), b.getTitle(), b.getAuthor(), b.getQuantity()});
        }
    }

    private void handleAdd() {
        String title  = txtTitle.getText().trim();
        String author = txtAuthor.getText().trim();
        String qtyStr = txtQty.getText().trim();
        if (title.isEmpty() || author.isEmpty() || qtyStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "اكتب كل الحقول", "تنبيه", JOptionPane.WARNING_MESSAGE); return;
        }
        try {
            int qty = Integer.parseInt(qtyStr);
            if (bookService.addBook(title, author, qty)) {
                JOptionPane.showMessageDialog(this, "تمت إضافة الكتاب ✅");
                loadBooks(); clearForm();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "الكمية لازم تكون رقم!", "خطأ", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleUpdate() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "اختار كتاب الأول"); return; }
        int id = (int) tableModel.getValueAt(row, 0);
        String title  = txtTitle.getText().trim();
        String author = txtAuthor.getText().trim();
        String qtyStr = txtQty.getText().trim();
        if (title.isEmpty() || author.isEmpty() || qtyStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "اكتب كل الحقول", "تنبيه", JOptionPane.WARNING_MESSAGE); return;
        }
        try {
            int qty = Integer.parseInt(qtyStr);
            if (bookService.updateBook(id, title, author, qty)) {
                JOptionPane.showMessageDialog(this, "تم التعديل ✅");
                loadBooks(); clearForm();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "الكمية لازم تكون رقم!", "خطأ", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDelete() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "اختار كتاب الأول"); return; }
        int id    = (int) tableModel.getValueAt(row, 0);
        String title = (String) tableModel.getValueAt(row, 1);
        int confirm = JOptionPane.showConfirmDialog(this,
            "هل تريد حذف كتاب: " + title + "؟", "تأكيد الحذف", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (bookService.deleteBook(id)) {
                JOptionPane.showMessageDialog(this, "تم الحذف ✅");
                loadBooks(); clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "مش هينفع الحذف — الكتاب عنده استعارات", "خطأ", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        txtTitle.setText(""); txtAuthor.setText(""); txtQty.setText("");
        table.clearSelection();
    }

    private JPanel makeHeader(String text) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(new Color(25, 50, 100));
        p.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Arial", Font.BOLD, 17));
        lbl.setForeground(Color.WHITE);
        p.add(lbl);
        return p;
    }

    private JButton makeBtn(String text, Color color) {
        JButton b = new JButton(text);
        b.setBackground(color); b.setForeground(Color.WHITE);
        b.setFocusPainted(false); b.setFont(new Font("Arial", Font.BOLD, 12));
        return b;
    }
}
