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

    @Test
    void testFindByTitle() {

        Movie movie = new Movie();
        movie.setTitle("The Matrix");
        movie.setFilmDirector("Wachowski");
        movie.setDescription("sci-fi");
        movie.setCompany("WB");
        movie.setReleaseYear(1999);

        movieDao.save(movie);

        var result = movieDao.findByTitle("Matrix");

        assertFalse(result.isEmpty());
        assertTrue(result.get(0).getTitle().contains("Matrix"));
    }

    @Test
    void testFindByYear() {

        Movie movie = new Movie();
        movie.setTitle("Interstellar");
        movie.setFilmDirector("Nolan");
        movie.setDescription("space");
        movie.setCompany("WB");
        movie.setReleaseYear(2014);

        movieDao.save(movie);

        var result = movieDao.findByYear(2014);

        assertFalse(result.isEmpty());
        assertEquals(2014, result.get(0).getReleaseYear());
    }

    @Test
    void testFindByDirector() {

        Movie movie = new Movie();
        movie.setTitle("Matrix");
        movie.setFilmDirector("Test");
        movie.setDescription("desc");
        movie.setCompany("WB");
        movie.setReleaseYear(1999);

        movieDao.save(movie);

        var result = movieDao.findByDirector("Test");

        assertFalse(result.isEmpty());
        assertEquals("Test", result.get(0).getFilmDirector());
    }

    @Test
    void testFindByCompany() {

        Movie movie = new Movie();
        movie.setTitle("Inception");
        movie.setFilmDirector("Nolan");
        movie.setDescription("desc");
        movie.setCompany("Warner Bros");
        movie.setReleaseYear(2010);

        movieDao.save(movie);

        var result = movieDao.findByCompany("Warner");

        assertFalse(result.isEmpty());
        assertTrue(result.get(0).getCompany().contains("Warner"));
    }

    @Test
    void testFindByDirectorNotFound() {

        var result = movieDao.findByDirector("Unknown");

        assertTrue(result.isEmpty());
    }
}