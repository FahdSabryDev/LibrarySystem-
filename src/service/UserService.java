package service;

import db.DBConnection;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    /** تسجيل دخول — بيرجع User أو null */
    public User login(String username, String password) {
        String sql = "SELECT id, username, password, role FROM users WHERE username=? AND password=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("id"), rs.getString("username"),
                                rs.getString("password"), rs.getString("role"));
            }
        } catch (SQLException e) {
            System.err.println("خطأ Login: " + e.getMessage());
        }
        return null;
    }

    /** تسجيل مستخدم جديد — role = "user" دايماً */
    public boolean register(String username, String password) {
        if (usernameExists(username)) return false;
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, 'user')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("خطأ Register: " + e.getMessage());
            return false;
        }
    }

    /** التحقق من وجود اسم المستخدم */
    public boolean usernameExists(String username) {
        String sql = "SELECT id FROM users WHERE username=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            return stmt.executeQuery().next();
        } catch (SQLException e) { return false; }
    }

    /** جلب كل المستخدمين العاديين (للأدمن) */
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT id, username, password, role FROM users WHERE role='user'";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new User(rs.getInt("id"), rs.getString("username"),
                                  rs.getString("password"), rs.getString("role")));
            }
        } catch (SQLException e) {
            System.err.println("خطأ getAllUsers: " + e.getMessage());
        }
        return list;
    }

    /** حذف مستخدم بالـ ID */
    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE id=? AND role='user'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("خطأ deleteUser: " + e.getMessage());
            return false;
        }
    }
}
