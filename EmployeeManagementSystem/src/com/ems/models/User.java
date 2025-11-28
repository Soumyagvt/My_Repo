package com.ems.models;

public class User {
    private int id;
    private String username;
    private String password;
    private String role;
    private Integer linkedId;

    public User() {}

    public User(int id, String username, String password, String role, Integer linkedId) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.linkedId = linkedId;
    }

    // getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Integer getLinkedId() { return linkedId; }
    public void setLinkedId(Integer linkedId) { this.linkedId = linkedId; }
}
