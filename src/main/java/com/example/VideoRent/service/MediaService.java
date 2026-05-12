package com.example.VideoRent.service;

import com.example.VideoRent.dao.MediaDao;
import com.example.VideoRent.entity.Media;
import com.example.VideoRent.entity.Movie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MediaService {

    private final MediaDao mediaDao;

    public MediaService(MediaDao mediaDao) {
        this.mediaDao = mediaDao;
    }

    @Transactional
    public void save(String name) {
        Media media = new Media();
        media.setName(name);
        mediaDao.save(media);
    }
}
