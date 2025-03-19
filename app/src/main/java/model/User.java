package model;

public class User {
    public String userId, name, email, role;

    public User() {}

    public User(String userId, String name, String email, String role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.role = role;
    }
}

