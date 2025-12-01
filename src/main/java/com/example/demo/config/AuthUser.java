package com.example.demo.config;

import com.example.demo.model.Role;

import java.util.List;

public class AuthUser {
    private String id;
    private String name;
    private Role role;
//    private String passwordHash; // Optional, usually not needed

    public AuthUser(){

    }
    public AuthUser(String id, Role role) {
        this.id = id;
        this.role = role;
//        this.passwordHash = passwordHash;
    }

    public String getName(){
        return this.name;
    }
       
    public void setName(String name){
        this.name = name;
    }
    
    public void setId( String id){
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setRole(Role role){
        this.role = role;
    }

    public Role getRole() {
        return this.role;
    }

//    public String getPasswordHash() {
//        return passwordHash;
//    }
}
