package com.example.VideoRent.entity;

import lombok.*;

import jakarta.persistence.*;

@Entity
@Table(name = "media")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Media implements CommonEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "media_id")
    private Long id;

    @Column(nullable = false, name = "name")
    private String name;
}