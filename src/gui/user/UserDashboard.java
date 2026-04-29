package gui.user;

import model.Book;
import model.User;
import service.BookService;
import service.BorrowService;
import gui.HomeFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * UserDashboard — الشاشة الرئيسية للمستخدم العادي
 * بيعرض الكتب المتاحة ويقدر يستعير
 */
public class UserDashboard extends JFrame {

    private User          currentUser;
    private BookService   bookService   = new BookService();
    private BorrowService borrowService = new BorrowService();

    private DefaultTableModel tableModel;
    private JTable            table;

    public UserDashboard(User user) {
        this.currentUser = user;
        initUI();
        loadBooks();
    }

    private void initUI() {
        setTitle("📚 المكتبة — " + currentUser.getUsername());
        setSize(640, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(20, 70, 140));
        header.setBorder(BorderFactory.createEmptyBorder(13, 20, 13, 20));

        JLabel lblTitle = new JLabel("📚 الكتب المتاحة");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblUser = new JLabel("مرحباً، " + currentUser.getUsername());
        lblUser.setForeground(new Color(180, 215, 255));

        header.add(lblTitle, BorderLayout.WEST);
        header.add(lblUser,  BorderLayout.EAST);

        // الجدول
        String[] cols = {"#", "عنوان الكتاب", "المؤلف", "المتاح"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.setSelectionBackground(new Color(190, 220, 255));
        table.getColumnModel().getColumn(0).setMaxWidth(40);
        table.getColumnModel().getColumn(3).setMaxWidth(70);

        JScrollPane scroll = new JScrollPane(table);

        // الأزرار
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        btnPanel.setBackground(new Color(240, 244, 255));

        JButton btnBorrow   = makeBtn("📖 استعارة الكتاب المحدد", new Color(30, 120, 60));
        JButton btnMyBorrow = makeBtn("📋 استعاراتي",             new Color(80, 100, 180));
        JButton btnLogout   = makeBtn("خروج",                      new Color(150, 40, 40));

        btnPanel.add(btnBorrow);
        btnPanel.add(btnMyBorrow);
        btnPanel.add(btnLogout);

        add(header,   BorderLayout.NORTH);
        add(scroll,   BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        // أحداث
        btnBorrow.addActionListener(e -> handleBorrow());

        btnMyBorrow.addActionListener(e -> {
            new MyBorrowsFrame(currentUser).setVisible(true);
            dispose();
        });

        btnLogout.addActionListener(e -> {
            new HomeFrame().setVisible(true);
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

    private void handleBorrow() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "اختار كتاب الأول"); return; }

        int qty = (int) tableModel.getValueAt(row, 3);
        if (qty < 1) {
            JOptionPane.showMessageDialog(this, "الكتاب ده مش متاح دلوقتي", "تنبيه", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int bookId    = (int) tableModel.getValueAt(row, 0);
        String title  = (String) tableModel.getValueAt(row, 1);
        int confirm = JOptionPane.showConfirmDialog(this,
            "هل تريد استعارة كتاب:\n\"" + title + "\"؟",
            "تأكيد الاستعارة", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (borrowService.borrowBook(currentUser.getId(), bookId)) {
                JOptionPane.showMessageDialog(this, "تمت الاستعارة بنجاح! ✅\nإرجاع الكتاب في أقرب وقت ممكن.");
                loadBooks();
            } else {
                JOptionPane.showMessageDialog(this, "حصل خطأ في الاستعارة", "خطأ", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JButton makeBtn(String t, Color c) {
        JButton b = new JButton(t);
        b.setBackground(c); b.setForeground(Color.WHITE);
        b.setFocusPainted(false); b.setFont(new Font("Arial", Font.BOLD, 12));
        return b;
    }
}
