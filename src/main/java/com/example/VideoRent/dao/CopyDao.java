package com.example.VideoRent.dao;

import com.example.VideoRent.entity.Copy;
import org.springframework.stereotype.Repository;

@Repository
public class CopyDao extends CommonDaoImpl<Copy, Long> {

    public CopyDao() {
        super(Copy.class);
    }
}
