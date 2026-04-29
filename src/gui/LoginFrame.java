package gui;

import model.User;
import service.UserService;
import gui.admin.AdminDashboard;
import gui.user.UserDashboard;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField     txtUsername;
    private JPasswordField txtPassword;
    private UserService    userService = new UserService();

    public LoginFrame() {
        // استدعاء الدالة اللي بتبني الواجهة
        initUI();
    }

    private void initUI() {
        setTitle("تسجيل الدخول - نظام المكتبة");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // تطبيق شكل النظام (System Look and Feel)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { }

        // لوحة الرسم الرئيسية (Dark Mode)
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.DARK_GRAY);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(10, 10, 10, 10);
        g.weightx = 1.0; // ده اللي بيخليها تتمط بالعرض

        // --- شعار أو عنوان ---
        JLabel lblTitle = new JLabel("🔑 Library Login", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.CYAN); // لون مميز للعنوان
        g.gridx = 0; g.gridy = 0; g.gridwidth = 2;
        panel.add(lblTitle, g);

        // --- اسم المستخدم ---
        g.gridwidth = 1; g.gridy = 1; g.gridx = 0;
        JLabel lblUser = new JLabel("User ID:");
        lblUser.setForeground(Color.WHITE);
        panel.add(lblUser, g);

        txtUsername = new JTextField(15);
        g.gridx = 1;
        panel.add(txtUsername, g);

        // --- كلمة المرور ---
        g.gridy = 2; g.gridx = 0;
        JLabel lblPass = new JLabel("Password:");
        lblPass.setForeground(Color.WHITE);
        panel.add(lblPass, g);

        txtPassword = new JPasswordField(15);
        g.gridx = 1;
        panel.add(txtPassword, g);

        // --- أزرار الأكشن (Login & Reset) ---
        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        btnPanel.setOpaque(false); // عشان يظهر لون الخلفية الرمادي

        JButton btnLogin = new JButton("Login");
        JButton btnReset = new JButton("Reset");

        // تنسيق الزراير
        btnLogin.setFocusable(false);
        btnReset.setFocusable(false);

        btnPanel.add(btnLogin);
        btnPanel.add(btnReset);

        g.gridy = 3; g.gridx = 0; g.gridwidth = 2;
        panel.add(btnPanel, g);

        // --- زرار الرجوع ---
        JButton btnBack = new JButton("← Back to Home");
        btnBack.setForeground(Color.LIGHT_GRAY);
        btnBack.setContentAreaFilled(false);
        btnBack.setBorderPainted(false);
        g.gridy = 4;
        panel.add(btnBack, g);

        add(new JScrollPane(panel));

        // --- ضبط المقاسات الذكي ---
        pack(); // بتخلي الشاشة على قد المحتوى بالظبط
        setMinimumSize(new Dimension(450, 400)); // أقل مقاس مسموح به
        setLocationRelativeTo(null); // توسيط الشاشة
        setResizable(true); // السماح بالتكبير والتصغير

        // --- الأوامر (Actions) ---
        btnLogin.addActionListener(e -> handleLogin());
        btnReset.addActionListener(e -> {
            txtUsername.setText("");
            txtPassword.setText("");
        });
        btnBack.addActionListener(e -> {
            new HomeFrame().setVisible(true);
            dispose();
        });
    }

    private void handleLogin() {
        String u = txtUsername.getText().trim();
        String p = new String(txtPassword.getPassword()).trim();

        if (u.isEmpty() || p.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter all fields!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        User user = userService.login(u, p);
        if (user == null) {
            JOptionPane.showMessageDialog(this, "Invalid Login Data!", "Error", JOptionPane.ERROR_MESSAGE);
            txtPassword.setText("");
            return;
        }

        if (user.isAdmin()) {
            new AdminDashboard(user).setVisible(true);
        } else {
            new UserDashboard(user).setVisible(true);
        }
        dispose();
    }
}