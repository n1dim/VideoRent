package com.example.VideoRent.dao;

import com.example.VideoRent.entity.Media;
import org.springframework.stereotype.Repository;

@Repository
public class MediaDao extends CommonDaoImpl<Media, Long> {

    public MediaDao() {
        super(Media.class);
    }
}