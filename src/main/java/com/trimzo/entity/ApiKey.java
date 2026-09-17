package com.trimzo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "api_keys")
@Getter
@Setter
@NoArgsConstructor
public class ApiKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // Stored as BCrypt hash — raw key shown only once
    @Column(name = "key_value", nullable = false, unique = true)
    private String keyValue;

    // Label to identify the key (e.g., "My Website")
    private String name;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "last_used")
    private LocalDateTime lastUsed;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
} 