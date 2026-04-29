package com.example.VideoRent.dao;

import com.example.VideoRent.entity.Movie;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class MovieDao extends CommonDaoImpl<Movie, Long> {

    public MovieDao() {
        super(Movie.class);
    }

    public List<Movie> findByTitle(String title) {
        return entityManager
                .createQuery("SELECT m FROM Movie m WHERE LOWER(m.title) LIKE LOWER(:title)", Movie.class)
                .setParameter("title", "%" + title + "%")
                .getResultList();
    }

    public List<Movie> findByYear(Integer year) {
        return entityManager
                .createQuery("SELECT m FROM Movie m WHERE m.releaseYear = :year", Movie.class)
                .setParameter("year", year)
                .getResultList();
    }

    public List<Movie> findByDirector(String director) {
        return entityManager
                .createQuery("SELECT m FROM Movie m WHERE LOWER(m.filmDirector) LIKE LOWER(:director)", Movie.class)
                .setParameter("director", "%" + director + "%")
                .getResultList();
    }

    public List<Movie> findByCompany(String company) {
        return entityManager
                .createQuery("SELECT m FROM Movie m WHERE LOWER(m.company) LIKE LOWER(:company)", Movie.class)
                .setParameter("company", "%" + company + "%")
                .getResultList();
    }
}