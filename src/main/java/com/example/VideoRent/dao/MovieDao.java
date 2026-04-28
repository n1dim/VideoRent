package com.example.VideoRent.dao;

import com.example.VideoRent.entity.Movie;
import org.springframework.stereotype.Repository;

@Repository
public class MovieDao extends CommonDaoImpl<Movie, Long> {

    public MovieDao() {
        super(Movie.class);
    }
}