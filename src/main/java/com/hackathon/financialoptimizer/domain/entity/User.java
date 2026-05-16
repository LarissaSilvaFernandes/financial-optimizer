package com.hackathon.financialoptimizer.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;

public class User {

    private UUID id;
    private String email;
    private String password;
    private String name;
    private LocalDateTime createdAt;

    public User(UUID id, String email, String password, String name, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.createdAt = createdAt;
    }

    public static User create(String email, String encodedPassword, String name) {
        return new User(UUID.randomUUID(), email, encodedPassword, name, LocalDateTime.now());
    }

    public UUID getId() { return id; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getName() { return name; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
