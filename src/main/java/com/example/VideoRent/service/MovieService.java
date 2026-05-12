package com.example.VideoRent.service;

import com.example.VideoRent.dao.MovieDao;
import com.example.VideoRent.entity.Movie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class MovieService {

    private final MovieDao movieDao;

    public MovieService(MovieDao movieDao) {
        this.movieDao = movieDao;
    }

    public Movie getMovie(Long id) {
        return movieDao.getById(id);
    }

    public List<Movie> getAllMovies() {
        return movieDao.getAll();
    }

    public List<Movie> search(String title) {
        if (title == null || title.isBlank()) return movieDao.getAll();
        return movieDao.findByTitle(title);
    }

    @Transactional
    public void addMovie(Movie movie) {
        movieDao.save(movie);
    }

    @Transactional
    public void updateMovie(Long id, Movie updated) {
        Movie movie = movieDao.getById(id);
        if (movie == null) return;
        movie.setTitle(updated.getTitle());
        movie.setFilmDirector(updated.getFilmDirector());
        movie.setCompany(updated.getCompany());
        movie.setReleaseYear(updated.getReleaseYear());
        movie.setDescription(updated.getDescription());
    }

    @Transactional
    public void deleteMovie(Long id) {
        Movie movie = movieDao.getById(id);
        if (movie == null) return;
        movie.setIsActive(false);
    }
}
