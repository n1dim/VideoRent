package com.example.VideoRent.entity;

import lombok.*;

import jakarta.persistence.*;


@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User implements CommonEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "user_id")
    private Long id;

    @Column(nullable = false, name = "telephone_number")
    private String telephoneNumber;

    @Column(nullable = false, name = "full_name")
    private String fullName;

    @Column(nullable = false, name = "home_address")
    private String homeAddress;

    @Column(nullable = false, name = "password_hash")
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "role")
    private Role role;

    @Column(nullable = false, name = "is_blocked")
    private Boolean isBlocked;

    @Column(name = "block_reason")
    private String blockReason;
}