package com.example.VideoRent.dao;

import com.example.VideoRent.entity.Copy;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CopyDao extends CommonDaoImpl<Copy, Long> {

    public CopyDao() {
        super(Copy.class);
    }

    public List<Copy> findByMovieId(Long movieId) {
        return entityManager
                .createQuery("SELECT c FROM Copy c WHERE c.film.id = :movieId", Copy.class)
                .setParameter("movieId", movieId)
                .getResultList();
    }
}
