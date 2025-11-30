package com.example.demo.config;

import java.util.List;

public class AuthUser {
    private Long id;
    private List<String> roles;
//    private String passwordHash; // Optional, usually not needed

    public AuthUser(Long id, List<String> roles, String passwordHash) {
        this.id = id;
        this.roles = roles;
//        this.passwordHash = passwordHash;
    }

    public Long getId() {
        return id;
    }

    public List<String> getRoles() {
        return roles;
    }

//    public String getPasswordHash() {
//        return passwordHash;
//    }
}
