package com.example.VideoRent.entity;

import lombok.*;

import jakarta.persistence.*;

@Entity
@Table(name = "copies")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Copy implements CommonEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "copy_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "film_id")
    private Movie film;

    @ManyToOne
    @JoinColumn(name = "media_id")
    private Media media;

    @Column(nullable = false, name = "cost")
    private Integer cost;

    @Column(nullable = false, name = "count")
    private Integer count;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;
}
