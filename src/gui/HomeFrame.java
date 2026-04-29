package gui;

import javax.swing.*;
import java.awt.*;

/**
 * HomeFrame — الشاشة الرئيسية عند فتح التطبيق
 * فيها زرارين: Login و Sign Up
 */
public class HomeFrame extends JFrame {

    public HomeFrame() {
        initUI();
    }

    private void initUI() {
        setTitle("📚 نظام إدارة المكتبة");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(480, 380);
        setLocationRelativeTo(null);
        setResizable(false);

        // الخلفية
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(20, 40, 80));

        // ===== الجزء العلوي — الأيقونة والعنوان =====
        JPanel topPanel = new JPanel(new GridLayout(3, 1, 0, 5));
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(45, 30, 20, 30));

        JLabel lblIcon = new JLabel("📚", SwingConstants.CENTER);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 52));

        JLabel lblTitle = new JLabel("نظام إدارة المكتبة", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Library Management System", SwingConstants.CENTER);
        lblSub.setFont(new Font("Arial", Font.PLAIN, 13));
        lblSub.setForeground(new Color(180, 200, 240));

        topPanel.add(lblIcon);
        topPanel.add(lblTitle);
        topPanel.add(lblSub);

        // ===== الجزء السفلي — الأزرار =====
        JPanel btnPanel = new JPanel(new GridLayout(2, 1, 0, 12));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(15, 60, 50, 60));

        JButton btnLogin    = createButton("تسجيل الدخول  🔑", new Color(50, 130, 220));
        JButton btnRegister = createButton("إنشاء حساب جديد  ✏", new Color(50, 160, 90));

        btnPanel.add(btnLogin);
        btnPanel.add(btnRegister);

        mainPanel.add(topPanel, BorderLayout.CENTER);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);
        add(mainPanel);

        // ===== الأحداث =====
        btnLogin.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });

        btnRegister.addActionListener(e -> {
            new RegisterFrame().setVisible(true);
            dispose();
        });
    }

    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 15));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(0, 48));
        return btn;
    }
}
