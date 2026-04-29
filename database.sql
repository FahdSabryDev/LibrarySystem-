-- كريت الداتا بيز لو مش موجودة
IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'library_db')
BEGIN
    CREATE DATABASE library_db;
END
GO

USE library_db;
GO

-- جدول المستخدمين
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[users]') AND type in (N'U'))
BEGIN
    CREATE TABLE users (
        id       INT IDENTITY(1,1) PRIMARY KEY, -- هنا IDENTITY بدل AUTO_INCREMENT
        username NVARCHAR(50)  NOT NULL UNIQUE,
        password NVARCHAR(100) NOT NULL,
        role     NVARCHAR(10)  NOT NULL DEFAULT 'user'
    );
END

-- جدول الكتب
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[books]') AND type in (N'U'))
BEGIN
    CREATE TABLE books (
        id       INT IDENTITY(1,1) PRIMARY KEY,
        title    NVARCHAR(150) NOT NULL,
        author   NVARCHAR(100) NOT NULL,
        quantity INT           NOT NULL DEFAULT 1
    );
END

-- جدول الاستعارات
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[borrows]') AND type in (N'U'))
BEGIN
    CREATE TABLE borrows (
        id          INT IDENTITY(1,1) PRIMARY KEY,
        user_id     INT  NOT NULL,
        book_id     INT  NOT NULL,
        borrow_date DATE NOT NULL DEFAULT (GETDATE()), -- هنا GETDATE بدل CURDATE
        return_date DATE,
        FOREIGN KEY (user_id) REFERENCES users(id),
        FOREIGN KEY (book_id) REFERENCES books(id)
    );
END

-- إضافة البيانات (لو الجدول فاضي)
IF NOT EXISTS (SELECT * FROM users WHERE username = 'admin')
BEGIN
    INSERT INTO users (username, password, role) VALUES ('admin', 'admin123', 'admin');
	INSERT INTO users (username, password, role) VALUES ('Fahd', 'Fahd', 'admin');

END

IF NOT EXISTS (SELECT * FROM books)
BEGIN
    INSERT INTO books (title, author, quantity) VALUES 
    (N'مقدمة ابن خلدون', N'ابن خلدون', 3),
    (N'ألف ليلة وليلة', N'مجهول', 2),
    (N'في عقلك قوة خارقة', N'إبراهيم الفقي', 5);
END
GO
USE library_db;
INSERT INTO users (username, password, role) 
VALUES ('ali', 'Fahd', 'admin');

select * from users ;