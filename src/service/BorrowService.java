package service;

import db.DBConnection;
import model.Borrow;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BorrowService {

    /** استعارة كتاب */
    public boolean borrowBook(int userId, int bookId) {
        // تحقق إن الكتاب متاح
        String checkSql = "SELECT quantity FROM books WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement check = conn.prepareStatement(checkSql)) {
            check.setInt(1, bookId);
            ResultSet rs = check.executeQuery();
            if (!rs.next() || rs.getInt("quantity") < 1) return false;
        } catch (SQLException e) {
            return false;
        }

        // تم تغيير CURDATE() إلى GETDATE() لتناسب SQL Server
        String insertSql = "INSERT INTO borrows (user_id, book_id, borrow_date) VALUES (?, ?, GETDATE())";
        String updateSql = "UPDATE books SET quantity = quantity - 1 WHERE id=?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Transaction
            try (PreparedStatement ins = conn.prepareStatement(insertSql);
                 PreparedStatement upd = conn.prepareStatement(updateSql)) {
                ins.setInt(1, userId);
                ins.setInt(2, bookId);
                ins.executeUpdate();

                upd.setInt(1, bookId);
                upd.executeUpdate();

                conn.commit();
                return true;
            } catch (SQLException ex) {
                conn.rollback();
                System.err.println("خطأ borrowBook: " + ex.getMessage());
                return false;
            }
        } catch (SQLException e) {
            System.err.println("خطأ connection: " + e.getMessage());
            return false;
        }
    }

    /** إرجاع كتاب */
    public boolean returnBook(int borrowId, int bookId) {
        // تم تغيير CURDATE() إلى GETDATE() هنا أيضاً
        String updateBorrow = "UPDATE borrows SET return_date = GETDATE() WHERE id=? AND return_date IS NULL";
        String updateBook   = "UPDATE books SET quantity = quantity + 1 WHERE id=?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ub = conn.prepareStatement(updateBorrow);
                 PreparedStatement ubk = conn.prepareStatement(updateBook)) {
                ub.setInt(1, borrowId);
                int rows = ub.executeUpdate();
                if (rows == 0) { conn.rollback(); return false; }

                ubk.setInt(1, bookId);
                ubk.executeUpdate();

                conn.commit();
                return true;
            } catch (SQLException ex) {
                conn.rollback();
                return false;
            }
        } catch (SQLException e) { return false; }
    }

    /** استعارات مستخدم معين */
    public List<Borrow> getUserBorrows(int userId) {
        List<Borrow> list = new ArrayList<>();
        // استخدام SQL القياسي المتوافق مع الطرفين
        String sql = "SELECT bw.id, bw.user_id, bw.book_id, bw.borrow_date, bw.return_date, " +
                "b.title AS book_title " +
                "FROM borrows bw JOIN books b ON bw.book_id = b.id " +
                "WHERE bw.user_id = ? " +
                "ORDER BY bw.borrow_date DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Borrow bw = new Borrow(rs.getInt("id"), rs.getInt("user_id"),
                        rs.getInt("book_id"), rs.getString("borrow_date"),
                        rs.getString("return_date"));
                bw.setBookTitle(rs.getString("book_title"));
                list.add(bw);
            }
        } catch (SQLException e) {
            System.err.println("خطأ getUserBorrows: " + e.getMessage());
        }
        return list;
    }

    /** كل الاستعارات (للأدمن) */
    public List<Borrow> getAllBorrows() {
        List<Borrow> list = new ArrayList<>();
        String sql = "SELECT bw.id, bw.user_id, bw.book_id, bw.borrow_date, bw.return_date, " +
                "u.username, b.title AS book_title " +
                "FROM borrows bw " +
                "JOIN users u ON bw.user_id = u.id " +
                "JOIN books b ON bw.book_id = b.id " +
                "ORDER BY bw.borrow_date DESC";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Borrow bw = new Borrow(rs.getInt("id"), rs.getInt("user_id"),
                        rs.getInt("book_id"), rs.getString("borrow_date"),
                        rs.getString("return_date"));
                bw.setUsername(rs.getString("username"));
                bw.setBookTitle(rs.getString("book_title"));
                list.add(bw);
            }
        } catch (SQLException e) {
            System.err.println("خطأ getAllBorrows: " + e.getMessage());
        }
        return list;
    }
}