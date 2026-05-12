package com.example.VideoRent.dao;

import com.example.VideoRent.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import jakarta.transaction.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class RentalDaoTest {

    @Autowired
    private RentalDao rentalDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private MovieDao movieDao;

    @Autowired
    private MediaDao mediaDao;

    @Autowired
    private CopyDao copyDao;

    @Test
    void testSaveAndGet() {

        // 1. User
        User user = new User();
        user.setTelephoneNumber("111");
        user.setFullName("Rental User");
        user.setHomeAddress("Addr");
        user.setPasswordHash("hash");
        user.setRole(Role.USER);
        user.setIsBlocked(false);
        userDao.save(user);

        // 2. Movie
        Movie movie = new Movie();
        movie.setTitle("Rental Movie");
        movie.setFilmDirector("Dir");
        movie.setDescription("desc");
        movie.setCompany("comp");
        movie.setReleaseYear(2020);
        movieDao.save(movie);

        // 3. Media
        Media media = new Media();
        media.setName("DVD");
        mediaDao.save(media);

        // 4. Copy
        Copy copy = new Copy();
        copy.setFilm(movie);
        copy.setMedia(media);
        copy.setCost(10);
        copy.setCount(1);
        copyDao.save(copy);

        // 5. Rental
        Rental rental = new Rental();
        rental.setUser(user);
        rental.setCopy(copy);
        rental.setIssueDate(LocalDate.of(2026, 4, 1));
        rental.setDueDate(LocalDate.of(2026, 4, 10));
        rental.setReturnDate(null);
        rental.setExtensionRequested(false);
        rental.setRequestedDate(null);

        rentalDao.save(rental);

        // проверки
        assertNotNull(rental.getId());

        Rental fromDb = rentalDao.getById(rental.getId());

        assertEquals(user.getId(), fromDb.getUser().getId());
        assertEquals(copy.getId(), fromDb.getCopy().getId());

        assertEquals(LocalDate.of(2026, 4, 1), fromDb.getIssueDate());
        assertEquals(LocalDate.of(2026, 4, 10), fromDb.getDueDate());
        assertFalse(fromDb.getExtensionRequested());
    }

    @Test
    void testGetAll() {

        User user = new User();
        user.setTelephoneNumber("222");
        user.setFullName("User2");
        user.setHomeAddress("Addr");
        user.setPasswordHash("hash");
        user.setRole(Role.USER);
        user.setIsBlocked(false);
        userDao.save(user);

        Movie movie = new Movie();
        movie.setTitle("M");
        movie.setFilmDirector("D");
        movie.setDescription("d");
        movie.setCompany("c");
        movie.setReleaseYear(2021);
        movieDao.save(movie);

        Media media = new Media();
        media.setName("Blu-ray");
        mediaDao.save(media);

        Copy copy = new Copy();
        copy.setFilm(movie);
        copy.setMedia(media);
        copy.setCost(20);
        copy.setCount(2);
        copyDao.save(copy);

        Rental rental = new Rental();
        rental.setUser(user);
        rental.setCopy(copy);
        rental.setIssueDate(LocalDate.of(2026, 4, 2));
        rental.setDueDate(LocalDate.of(2026, 4, 12));
        rental.setExtensionRequested(false);

        rentalDao.save(rental);

        var list = rentalDao.getAll();

        assertFalse(list.isEmpty());
    }

    @Test
    void testDelete() {

        User user = new User();
        user.setTelephoneNumber("333");
        user.setFullName("Del User");
        user.setHomeAddress("Addr");
        user.setPasswordHash("hash");
        user.setRole(Role.USER);
        user.setIsBlocked(false);
        userDao.save(user);

        Movie movie = new Movie();
        movie.setTitle("Del Movie");
        movie.setFilmDirector("D");
        movie.setDescription("d");
        movie.setCompany("c");
        movie.setReleaseYear(2022);
        movieDao.save(movie);

        Media media = new Media();
        media.setName("VHS");
        mediaDao.save(media);

        Copy copy = new Copy();
        copy.setFilm(movie);
        copy.setMedia(media);
        copy.setCost(30);
        copy.setCount(1);
        copyDao.save(copy);

        Rental rental = new Rental();
        rental.setUser(user);
        rental.setCopy(copy);
        rental.setIssueDate(LocalDate.of(2026, 4, 3));
        rental.setDueDate(LocalDate.of(2026, 4, 13));
        rental.setExtensionRequested(false);

        rentalDao.save(rental);

        Long id = rental.getId();

        rentalDao.delete(rental);

        assertNull(rentalDao.getById(id));
    }
}