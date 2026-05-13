package com.example.VideoRent.dao;

import com.example.VideoRent.entity.Rental;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class RentalDao extends CommonDaoImpl<Rental, Long> {

    public RentalDao() {
        super(Rental.class);
    }

    public List<Rental> findByUserId(Long userId) {
        return entityManager
                .createQuery("SELECT r FROM Rental r WHERE r.user.id = :userId ORDER BY r.issueDate DESC", Rental.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public long countActive() {
        return entityManager
                .createQuery("SELECT COUNT(r) FROM Rental r WHERE r.returnDate IS NULL", Long.class)
                .getSingleResult();
    }

    public Rental findActiveByMovieAndPhone(Long movieId, String phone) {
        return entityManager
                .createQuery("SELECT r FROM Rental r WHERE r.copy.film.id = :movieId AND r.user.telephoneNumber = :phone AND r.returnDate IS NULL", Rental.class)
                .setParameter("movieId", movieId)
                .setParameter("phone", phone)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);
    }
}
