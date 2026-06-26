package com.myntrademo.dto;

public class AuthenticatedUser {

    private Long userId;
    private String fullName;
    private String email;
    private String roleName;

    public AuthenticatedUser() {
    }

    public AuthenticatedUser(Long userId, String fullName, String email, String roleName) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.roleName = roleName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
  
    public String getFullName() {
        return fullName;
    }
  
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
  
    public String getEmail() {
        return email;
    }
  
    public void setEmail(String email) {
        this.email = email;
    }
  
    public String getRoleName() {
        return roleName;
    }
  
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}