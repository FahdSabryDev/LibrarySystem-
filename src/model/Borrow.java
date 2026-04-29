package model;

public class Borrow {
    private int    id;
    private int    userId;
    private int    bookId;
    private String borrowDate;
    private String returnDate;

    // للعرض في الجداول
    private String username;
    private String bookTitle;

    public Borrow() {}

    public Borrow(int id, int userId, int bookId, String borrowDate, String returnDate) {
        this.id         = id;
        this.userId     = userId;
        this.bookId     = bookId;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }

    public int    getId()         { return id; }
    public int    getUserId()     { return userId; }
    public int    getBookId()     { return bookId; }
    public String getBorrowDate() { return borrowDate; }
    public String getReturnDate() { return returnDate == null ? "لم يُرجع بعد" : returnDate; }
    public String getUsername()   { return username; }
    public String getBookTitle()  { return bookTitle; }

    public void setId(int id)               { this.id = id; }
    public void setUserId(int uid)          { this.userId = uid; }
    public void setBookId(int bid)          { this.bookId = bid; }
    public void setBorrowDate(String d)     { this.borrowDate = d; }
    public void setReturnDate(String d)     { this.returnDate = d; }
    public void setUsername(String u)       { this.username = u; }
    public void setBookTitle(String t)      { this.bookTitle = t; }
}
