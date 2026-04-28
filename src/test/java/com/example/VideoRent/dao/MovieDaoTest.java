package com.example.VideoRent.dao;

import com.example.VideoRent.entity.Movie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import jakarta.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class MovieDaoTest {

    @Autowired
    private MovieDao movieDao;

    @Test
    void testSaveAndGet() {
        Movie movie = new Movie();
        movie.setTitle("Test");
        movie.setFilmDirector("Dir");
        movie.setDescription("desc");
        movie.setCompany("comp");
        movie.setReleaseYear(2000);

        movieDao.save(movie);

        assertNotNull(movie.getId());

        Movie fromDb = movieDao.getById(movie.getId());

        assertEquals("Test", fromDb.getTitle());
        assertEquals("Dir", fromDb.getFilmDirector());
        assertEquals("desc", fromDb.getDescription());
        assertEquals("comp", fromDb.getCompany());
        assertEquals(2000, fromDb.getReleaseYear());
    }

    @Test
    void testGetAll() {
        Movie movie = new Movie();
        movie.setTitle("Test");
        movie.setFilmDirector("Dir");
        movie.setDescription("desc");
        movie.setCompany("comp");
        movie.setReleaseYear(1999);

        movieDao.save(movie);

        var list = movieDao.getAll();

        assertFalse(list.isEmpty());
    }

    @Test
    void testDelete() {
        Movie movie = new Movie();
        movie.setTitle("Delete");
        movie.setFilmDirector("Dir");
        movie.setDescription("desc");
        movie.setCompany("comp");
        movie.setReleaseYear(1990);

        movieDao.save(movie);

        Long id = movie.getId();

        movieDao.delete(movie);

        assertNull(movieDao.getById(id));
    }
}