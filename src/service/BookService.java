package service;

import db.DBConnection;
import model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookService {

    /** جلب كل الكتب */
    public List<Book> getAllBooks() {
        List<Book> list = new ArrayList<>();
        String sql = "SELECT id, title, author, quantity FROM books ORDER BY id";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Book(rs.getInt("id"), rs.getString("title"),
                                  rs.getString("author"), rs.getInt("quantity")));
            }
        } catch (SQLException e) {
            System.err.println("خطأ getAllBooks: " + e.getMessage());
        }
        return list;
    }

    /** إضافة كتاب جديد */
    public boolean addBook(String title, String author, int quantity) {
        String sql = "INSERT INTO books (title, author, quantity) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.setString(2, author);
            stmt.setInt(3, quantity);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("خطأ addBook: " + e.getMessage());
            return false;
        }
    }

    /** تعديل كتاب */
    public boolean updateBook(int id, String title, String author, int quantity) {
        String sql = "UPDATE books SET title=?, author=?, quantity=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.setString(2, author);
            stmt.setInt(3, quantity);
            stmt.setInt(4, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("خطأ updateBook: " + e.getMessage());
            return false;
        }
    }

    /** حذف كتاب */
    public boolean deleteBook(int id) {
        String sql = "DELETE FROM books WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("خطأ deleteBook: " + e.getMessage());
            return false;
        }
    }

    /** جلب كتاب بالـ ID */
    public Book getBookById(int id) {
        String sql = "SELECT id, title, author, quantity FROM books WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Book(rs.getInt("id"), rs.getString("title"),
                                rs.getString("author"), rs.getInt("quantity"));
            }
        } catch (SQLException e) {
            System.err.println("خطأ getBookById: " + e.getMessage());
        }
        return null;
    }

    // ===== Reports =====

    /** الكتاب الأكتر استعارةً */
    public String getMostBorrowedBook() {
        String sql = """
            SELECT b.title, COUNT(bw.id) AS total
            FROM books b JOIN borrows bw ON b.id = bw.book_id
            GROUP BY b.id ORDER BY total DESC LIMIT 1
            """;
        return getReportResult(sql, "لا توجد استعارات بعد");
    }

    /** الكتاب الأقل استعارةً */
    public String getLeastBorrowedBook() {
        String sql = """
            SELECT b.title, COUNT(bw.id) AS total
            FROM books b JOIN borrows bw ON b.id = bw.book_id
            GROUP BY b.id ORDER BY total ASC LIMIT 1
            """;
        return getReportResult(sql, "لا توجد استعارات بعد");
    }

    /** إجمالي عدد الاستعارات */
    public int getTotalBorrows() {
        String sql = "SELECT COUNT(*) FROM borrows";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("خطأ getTotalBorrows: " + e.getMessage());
        }
        return 0;
    }

    private String getReportResult(String sql, String fallback) {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getString("title") + "  (" + rs.getInt("total") + " مرة)";
            }
        } catch (SQLException e) {
            System.err.println("خطأ Report: " + e.getMessage());
        }
        return fallback;
    }
}
