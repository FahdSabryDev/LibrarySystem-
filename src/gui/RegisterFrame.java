package gui;

import service.UserService;
import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {

    private JTextField     txtUsername;
    private JPasswordField txtPassword, txtConfirm;
    private UserService    userService = new UserService();

    public RegisterFrame() {
        initUI();
    }

    private void initUI() {
        setTitle("إنشاء حساب جديد - نظام المكتبة");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // تطبيق شكل النظام
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { }

        // لوحة الرسم الرئيسية (Dark Mode متناسق مع Login)
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.DARK_GRAY);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(10, 10, 10, 10);
        g.weightx = 1.0;

        // --- العنوان ---
        JLabel lblTitle = new JLabel("✏ Create Account", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.GREEN); // لون أخضر هادي للإنشاء
        g.gridx = 0; g.gridy = 0; g.gridwidth = 2;
        panel.add(lblTitle, g);

        // --- اسم المستخدم ---
        g.gridwidth = 1; g.gridy = 1; g.gridx = 0;
        JLabel lblUser = new JLabel("Username:");
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

        // --- تأكيد كلمة المرور ---
        g.gridy = 3; g.gridx = 0;
        JLabel lblConfirm = new JLabel("Confirm:");
        lblConfirm.setForeground(Color.WHITE);
        panel.add(lblConfirm, g);

        txtConfirm = new JPasswordField(15);
        g.gridx = 1;
        panel.add(txtConfirm, g);

        // --- أزرار الأكشن (Register & Reset) ---
        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        btnPanel.setOpaque(false);

        JButton btnReg = new JButton("Register");
        JButton btnReset = new JButton("Reset");

        btnReg.setFocusable(false);
        btnReset.setFocusable(false);

        btnPanel.add(btnReg);
        btnPanel.add(btnReset);

        g.gridy = 4; g.gridx = 0; g.gridwidth = 2;
        panel.add(btnPanel, g);

        // --- زرار الرجوع ---
        JButton btnBack = new JButton("← Back to Home");
        btnBack.setForeground(Color.LIGHT_GRAY);
        btnBack.setContentAreaFilled(false);
        btnBack.setBorderPainted(false);
        g.gridy = 5;
        panel.add(btnBack, g);

        // إضافة ScrollPane عشان لو الشاشة صغرت العناصر متختفيش
        add(new JScrollPane(panel));

        // --- ضبط المقاسات الذكي ---
        pack();
        setMinimumSize(new Dimension(480, 450));
        setLocationRelativeTo(null);
        setResizable(true);

        // --- الأوامر (Actions) ---
        btnReg.addActionListener(e -> handleRegister());
        btnReset.addActionListener(e -> {
            txtUsername.setText("");
            txtPassword.setText("");
            txtConfirm.setText("");
        });
        btnBack.addActionListener(e -> {
            new HomeFrame().setVisible(true);
            dispose();
        });
    }

    private void handleRegister() {
        String u = txtUsername.getText().trim();
        String p = new String(txtPassword.getPassword()).trim();
        String c = new String(txtConfirm.getPassword()).trim();

        if (u.isEmpty() || p.isEmpty() || c.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!p.equals(c)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
            txtConfirm.setText("");
            return;
        }

        // محاولة التسجيل في الداتابيز
        if (userService.register(u, p)) {
            JOptionPane.showMessageDialog(this, "Account Created! 🎉\nPlease Login.", "Success", JOptionPane.INFORMATION_MESSAGE);
            new LoginFrame().setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed! Username might exist.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}