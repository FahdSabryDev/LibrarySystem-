package model;

public class User {
    private int    id;
    private String username;
    private String password;
    private String role; // "admin" or "user"

    public User() {}

    public User(int id, String username, String password, String role) {
        this.id       = id;
        this.username = username;
        this.password = password;
        this.role     = role;
    }

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role     = role;
    }

    public int    getId()       { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole()     { return role; }

    public void setId(int id)             { this.id = id; }
    public void setUsername(String u)     { this.username = u; }
    public void setPassword(String p)     { this.password = p; }
    public void setRole(String r)         { this.role = r; }

    public boolean isAdmin() { return "admin".equals(role); }

    @Override
    public String toString() { return username + " (" + role + ")"; }
}
