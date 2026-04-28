package com.example.VideoRent.dao;

import com.example.VideoRent.entity.Media;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import jakarta.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class MediaDaoTest {

    @Autowired
    private MediaDao mediaDao;

    @Test
    void testSaveAndGet() {
        Media media = new Media();
        media.setName("DVD");

        mediaDao.save(media);

        assertNotNull(media.getId());

        Media fromDb = mediaDao.getById(media.getId());

        assertEquals("DVD", fromDb.getName());
    }

    @Test
    void testGetAll() {
        Media media = new Media();
        media.setName("Blu-ray");

        mediaDao.save(media);

        var list = mediaDao.getAll();

        assertFalse(list.isEmpty());
    }

    @Test
    void testDelete() {
        Media media = new Media();
        media.setName("VHS");

        mediaDao.save(media);

        Long id = media.getId();

        mediaDao.delete(media);

        assertNull(mediaDao.getById(id));
    }
}