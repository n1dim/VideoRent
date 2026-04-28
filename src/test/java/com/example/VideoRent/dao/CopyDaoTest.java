package com.example.VideoRent.dao;

import com.example.VideoRent.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import jakarta.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CopyDaoTest {

    @Autowired
    private CopyDao copyDao;

    @Autowired
    private MovieDao movieDao;

    @Autowired
    private MediaDao mediaDao;

    @Test
    void testSaveAndGet() {

        Movie movie = new Movie();
        movie.setTitle("Film");
        movie.setFilmDirector("Director");
        movie.setDescription("desc");
        movie.setCompany("company");
        movie.setReleaseYear(2020);

        movieDao.save(movie);

        Media media = new Media();
        media.setName("DVD");

        mediaDao.save(media);

        Copy copy = new Copy();
        copy.setFilm(movie);
        copy.setMedia(media);
        copy.setCost(100);
        copy.setCount(5);
        copy.setStatus(Status.AVAILABLE);

        copyDao.save(copy);

        assertNotNull(copy.getId());

        Copy fromDb = copyDao.getById(copy.getId());

        assertEquals(100, fromDb.getCost());
        assertEquals(5, fromDb.getCount());
        assertEquals(Status.AVAILABLE, fromDb.getStatus());

        assertEquals("Film", fromDb.getFilm().getTitle());
        assertEquals("DVD", fromDb.getMedia().getName());
    }

    @Test
    void testGetAll() {
        Movie movie = new Movie();
        movie.setTitle("Film2");
        movie.setFilmDirector("Dir");
        movie.setDescription("desc");
        movie.setCompany("company");
        movie.setReleaseYear(2021);
        movieDao.save(movie);

        Media media = new Media();
        media.setName("Blu-ray");
        mediaDao.save(media);

        Copy copy = new Copy();
        copy.setFilm(movie);
        copy.setMedia(media);
        copy.setCost(200);
        copy.setCount(2);
        copy.setStatus(Status.UNAVAILABLE);

        copyDao.save(copy);

        var list = copyDao.getAll();

        assertFalse(list.isEmpty());
    }

    @Test
    void testDelete() {
        Movie movie = new Movie();
        movie.setTitle("Film3");
        movie.setFilmDirector("Dir");
        movie.setDescription("desc");
        movie.setCompany("company");
        movie.setReleaseYear(2022);
        movieDao.save(movie);

        Media media = new Media();
        media.setName("VHS");
        mediaDao.save(media);

        Copy copy = new Copy();
        copy.setFilm(movie);
        copy.setMedia(media);
        copy.setCost(50);
        copy.setCount(1);
        copy.setStatus(Status.AVAILABLE);

        copyDao.save(copy);

        Long id = copy.getId();

        copyDao.delete(copy);

        assertNull(copyDao.getById(id));
    }
}
