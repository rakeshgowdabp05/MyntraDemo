package com.myntrademo.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class User {

    private Long userId;
    private Long roleId;
    private String roleName;
    private String fullName;
    private String email;
    private String phone;
    private String passwordHash;
    private String profileImage;
    private String gender;
    private LocalDate dateOfBirth;
    private boolean active;
    private boolean emailVerified;
    private boolean phoneVerified;
    private LocalDateTime lastLoginAt;
    private LocalDateTime createdAt;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getFullName() {
        return fullName == null ? "" : fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email == null ? "" : email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone == null ? "" : phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDisplayPhone() {
        return phone == null || phone.isBlank() ? "Not added" : phone;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getGender() {
        return gender == null ? "" : gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDisplayGender() {
        if (gender == null || gender.isBlank()) {
            return "Not added";
        }

        return switch (gender) {
            case "MALE" -> "Male";
            case "FEMALE" -> "Female";
            case "OTHER" -> "Other";
            default -> gender;
        };
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getDateOfBirthValue() {
        return dateOfBirth == null ? "" : dateOfBirth.toString();
    }

    public String getDisplayDateOfBirth() {
        if (dateOfBirth == null) {
            return "Not added";
        }

        return dateOfBirth.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public boolean isActive() {
        return active;
    }

    public String getDisplayStatus() {
        return active ? "Active" : "Inactive";
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public boolean isPhoneVerified() {
        return phoneVerified;
    }

    public void setPhoneVerified(boolean phoneVerified) {
        this.phoneVerified = phoneVerified;
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public String getDisplayLastLoginAt() {
        if (lastLoginAt == null) {
            return "Not available";
        }

        return lastLoginAt.format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"));
    }

    public void setLastLoginAt(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getDisplayCreatedAt() {
        if (createdAt == null) {
            return "Not available";
        }

        return createdAt.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}