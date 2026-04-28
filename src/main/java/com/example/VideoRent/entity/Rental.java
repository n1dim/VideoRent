package com.example.VideoRent.entity;

import lombok.*;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "rentals")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Rental implements CommonEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "copy_id")
    private Copy copy;

    @Column(nullable = false, name = "issue_date")
    private LocalDate issueDate;

    @Column(nullable = false, name = "due_date")
    private LocalDate dueDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Column(name = "extension_requested")
    private Boolean extensionRequested;

    @Column(name = "requested_date")
    private LocalDate requestedDate;
}
