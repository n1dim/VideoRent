package com.example.VideoRent.entity;

import lombok.*;

import jakarta.persistence.*;


@Entity
@Table(name = "movies")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Movie implements CommonEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "film_id")
    private Long id;

    @Column(nullable = false, name = "title")
    private String title;

    @Column(nullable = false, name = "film_director")
    private String filmDirector;

    @Column(nullable = false, name = "description")
    private String description;

    @Column(nullable = false, name = "company")
    private String company;

    @Column(nullable = false, name = "release_year")
    private Integer releaseYear;
}