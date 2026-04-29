package gui.admin;

import model.User;
import gui.HomeFrame;

import javax.swing.*;
import java.awt.*;

/**
 * AdminDashboard — الشاشة الرئيسية للأدمن
 * فيها أزرار للانتقال لكل قسم
 */
public class AdminDashboard extends JFrame {

    private User admin;

    public AdminDashboard(User admin) {
        this.admin = admin;
        initUI();
    }

    private void initUI() {
        setTitle("لوحة الأدمن — " + admin.getUsername());
        setSize(520, 460);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(new Color(15, 30, 60));

        // ===== Header =====
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(25, 50, 100));
        header.setBorder(BorderFactory.createEmptyBorder(18, 25, 18, 25));

        JLabel lblTitle = new JLabel("📚 لوحة التحكم");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblAdmin = new JLabel("مرحباً " + admin.getUsername() + " 👑");
        lblAdmin.setForeground(new Color(255, 215, 0));
        lblAdmin.setFont(new Font("Arial", Font.BOLD, 13));

        header.add(lblTitle, BorderLayout.WEST);
        header.add(lblAdmin, BorderLayout.EAST);

        // ===== Grid of buttons =====
        JPanel grid = new JPanel(new GridLayout(2, 2, 15, 15));
        grid.setOpaque(false);
        grid.setBorder(BorderFactory.createEmptyBorder(30, 35, 30, 35));

        grid.add(makeTile("📖", "إدارة الكتب",    "إضافة / تعديل / حذف الكتب", new Color(30, 100, 180)));
        grid.add(makeTile("👥", "إدارة الأعضاء",  "عرض وحذف المستخدمين",        new Color(80, 150, 50)));
        grid.add(makeTile("📋", "الاستعارات",      "عرض كل الاستعارات",           new Color(160, 80, 20)));
        grid.add(makeTile("📊", "التقارير",        "max / min / count",           new Color(120, 30, 140)));

        // ===== Footer =====
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(new Color(20, 40, 80));
        footer.setBorder(BorderFactory.createEmptyBorder(5, 15, 10, 15));

        JButton btnLogout = new JButton("تسجيل الخروج");
        btnLogout.setFocusPainted(false);
        btnLogout.setBackground(new Color(180, 40, 40));
        btnLogout.setForeground(Color.WHITE);
        footer.add(btnLogout);

        main.add(header, BorderLayout.NORTH);
        main.add(grid,   BorderLayout.CENTER);
        main.add(footer, BorderLayout.SOUTH);
        add(main);

        // ===== أحداث الأزرار =====
        // بنجيب الأزرار من الـ tiles بالترتيب
        Component[] tiles = grid.getComponents();

        // كتب
        ((JButton)((JPanel)tiles[0]).getClientProperty("btn")).addActionListener(e -> {
            new ManageBooksFrame(admin).setVisible(true);
            dispose();
        });

        // أعضاء
        ((JButton)((JPanel)tiles[1]).getClientProperty("btn")).addActionListener(e -> {
            new ManageUsersFrame(admin).setVisible(true);
            dispose();
        });

        // استعارات
        ((JButton)((JPanel)tiles[2]).getClientProperty("btn")).addActionListener(e -> {
            new AllBorrowsFrame(admin).setVisible(true);
            dispose();
        });

        // تقارير
        ((JButton)((JPanel)tiles[3]).getClientProperty("btn")).addActionListener(e -> {
            new ReportsFrame(admin).setVisible(true);
            dispose();
        });

        btnLogout.addActionListener(e -> {
            new HomeFrame().setVisible(true);
            dispose();
        });
    }

    /** بيعمل كارت زرار للـ dashboard */
    private JPanel makeTile(String icon, String title, String sub, Color color) {
        JPanel tile = new JPanel(new BorderLayout(5, 5));
        tile.setBackground(color);
        tile.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        tile.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel lblIcon  = new JLabel(icon, SwingConstants.LEFT);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 26));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSub   = new JLabel(sub);
        lblSub.setFont(new Font("Arial", Font.PLAIN, 11));
        lblSub.setForeground(new Color(220, 230, 255));

        JButton btn = new JButton("فتح ←");
        btn.setFocusPainted(false);
        btn.setBackground(new Color(0,0,0,60));
        btn.setForeground(Color.WHITE);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);

        JPanel text = new JPanel(new GridLayout(2,1));
        text.setOpaque(false);
        text.add(lblTitle);
        text.add(lblSub);

        tile.add(lblIcon, BorderLayout.WEST);
        tile.add(text,    BorderLayout.CENTER);
        tile.add(btn,     BorderLayout.SOUTH);

        tile.putClientProperty("btn", btn); // نخزن الزرار عشان نوصله بعدين
        return tile;
    }
}
