package gui.admin;

import model.User;
import service.BookService;

import javax.swing.*;
import java.awt.*;

/**
 * ReportsFrame — التقارير: max / min / count
 */
public class ReportsFrame extends JFrame {

    private User        admin;
    private BookService bookService = new BookService();

    public ReportsFrame(User admin) {
        this.admin = admin;
        initUI();
    }

    private void initUI() {
        setTitle("📊 التقارير");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(new Color(15, 25, 50));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(80, 30, 100));
        header.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));
        JLabel lbl = new JLabel("📊 تقارير الاستعارات");
        lbl.setFont(new Font("Arial", Font.BOLD, 18));
        lbl.setForeground(Color.WHITE);
        header.add(lbl);

        // بطاقات التقارير
        JPanel cardsPanel = new JPanel(new GridLayout(3, 1, 0, 12));
        cardsPanel.setOpaque(false);
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 20, 30));

        String maxBook   = bookService.getMostBorrowedBook();
        String minBook   = bookService.getLeastBorrowedBook();
        int    total     = bookService.getTotalBorrows();

        cardsPanel.add(makeCard("📈  أكتر كتاب اتستعار  (MAX)",   maxBook,           new Color(40, 120, 60)));
        cardsPanel.add(makeCard("📉  أقل كتاب اتستعار  (MIN)",    minBook,           new Color(40, 80, 160)));
        cardsPanel.add(makeCard("🔢  إجمالي الاستعارات  (COUNT)", total + " استعارة", new Color(140, 60, 20)));

        // Footer
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        footer.setBackground(new Color(20, 35, 70));
        JButton btnBack = new JButton("← رجوع");
        btnBack.setBackground(new Color(80, 80, 80));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFocusPainted(false);
        footer.add(btnBack);

        main.add(header,     BorderLayout.NORTH);
        main.add(cardsPanel, BorderLayout.CENTER);
        main.add(footer,     BorderLayout.SOUTH);
        add(main);

        btnBack.addActionListener(e -> { new AdminDashboard(admin).setVisible(true); dispose(); });
    }

    private JPanel makeCard(String label, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout(10, 5));
        card.setBackground(color);
        card.setBorder(BorderFactory.createEmptyBorder(14, 18, 14, 18));

        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("Arial", Font.BOLD, 14));
        lblLabel.setForeground(new Color(220, 230, 255));

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Arial", Font.BOLD, 17));
        lblValue.setForeground(Color.WHITE);

        card.add(lblLabel, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);
        return card;
    }
}
