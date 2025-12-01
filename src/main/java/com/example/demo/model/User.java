package com.example.demo.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

@Document("user")
public class User {


    @Id
    private String id;

    @Field
    private String name;

    @Field
    private String phoneNumber;

    @Field
    private String password;

    @Field
    private Role role;

    // Constructors
    public User() {
        // empty constructor required by Spring Data
    }

    public User(String id, String name, String phoneNumber, String password, Role role, PasswordEncoder passwordEncoder) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.password = passwordEncoder.encode(password); // encode password here
        this.role = role;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone_number() { return phoneNumber; }
    public void setPhone_number(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getPassword() { return password; }
    public void setPassword(String rawPassword, PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(rawPassword);
    }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    // Seed users
    public static List<User> seedUser(PasswordEncoder passwordEncoder) {
        return Arrays.asList(
                new User("1", "តារាផល", "087443349", "12345678", Role.Admin, passwordEncoder),
                new User("2", "តារាផល", "087443344", "12345678", Role.Admin, passwordEncoder)
        );
    }
}
