package com.zalo.Spring_Zalo.Response;

public class UserInfoResponse {

    private int id;
    private String username;
    private String email;
    private String avatar;
    private int status;
    private String role;
    private String fullname;
    // Other constructors, getters, and setters as needed

    // Constructors, getters, and setters

    public UserInfoResponse(int id, String username, String email, String avatar, int status, String role, String fullname) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.avatar = avatar;
        this.status = status;
        this.role = role;
        this.fullname = fullname;
    }

    // Other constructors, getters, and setters as needed

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRole() {
        return role;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFullname() {
        return fullname;
    }

}
