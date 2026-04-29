# 📚 Library Management System

A simple **Java Desktop Application** built using **Java Swing** and **JDBC** to manage a library system with Admin and User functionalities.

---

## 🚀 Features

### 👤 User

* Register & Login
* View available books
* Borrow books
* Return borrowed books
* View personal borrow history

### 🛠️ Admin

* Manage Books (Add / Update / Delete / View)
* Manage Users
* View all borrow records
* Generate reports (MAX / MIN / COUNT)

---

## 🧱 Project Structure

```
LibrarySystem/
│
├── src/
│   ├── db/
│   ├── model/
│   ├── service/
│   └── gui/
│
├── database.sql
└── README.md
```

---

## ⚙️ Technologies Used

* Java
* Java Swing (GUI)
* JDBC
* MySQL

---

## 🗄️ Database

* Import the `database.sql` file into your MySQL server
* Update database credentials in:

```
DBConnection.java
```

Example:

```java
private static final String URL = "jdbc:mysql://localhost:3306/library_db";
private static final String USER = "root";
private static final String PASSWORD = "";
```

---

## ▶️ How to Run

1. Start MySQL (XAMPP or MySQL Server)
2. Import `database.sql`
3. Open the project in your IDE
4. Run `Main.java`

---

## 📌 Notes

* Make sure MySQL is running before launching the app
* Add MySQL Connector (JDBC Driver) to the project libraries

---

## 👨‍💻 Author

**Fahd Sabry**

GitHub: https://github.com/FahdSabryDev

---

## ⭐ Support

If you like this project, feel free to give it a star ⭐
