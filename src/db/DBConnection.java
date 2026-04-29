package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // اسم السيرفر بتاعك (مظبوط من الصورة)
    private static final String SERVER_NAME = "DESKTOP-I4A5C65";

    // غيرنا الاسم لـ library_db عشان يطابق الكود اللي لسه مشغلينه في SSMS
    private static final String DB_NAME = "library_db";

    // سطر الاتصال لـ SQL Server باستخدام Windows Authentication
    private static final String URL = "jdbc:sqlserver://" + SERVER_NAME +
            ";databaseName=" + DB_NAME +
            ";encrypt=true;" +
            "trustServerCertificate=true;" +
            "integratedSecurity=true;";

    public static Connection getConnection() throws SQLException {
        try {
            // تعريف SQL Server Driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(URL);
        } catch (ClassNotFoundException e) {
            throw new SQLException("خطأ: مكتبة mssql-jdbc مش موجودة في المشروع! ضيف ملف الـ Jar");
        }
    }
}
/*package db;




import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL      = "jdbc:mysql://localhost:3306/library_db";
    private static final String USER     = "root";
    private static final String PASSWORD = "your_password_here"; // ← غيّر دي

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver مش موجود!", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

  */
