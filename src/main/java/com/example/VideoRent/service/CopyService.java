package com.example.VideoRent.service;

import com.example.VideoRent.dao.CopyDao;
import com.example.VideoRent.entity.Copy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CopyService {

    private final CopyDao copyDao;

    public CopyService(CopyDao copyDao) {
        this.copyDao = copyDao;
    }

    public List<Copy> getCopiesByMovie(Long id) {
        return copyDao.findByMovieId(id);
    }

    public Copy getCopyById(Long id) {
        return copyDao.getById(id);
    }

    @Transactional
    public void save(Copy copy) {
        copyDao.save(copy);
    }

    @Transactional
    public void updateCopy(Long id, Copy updated) {
        Copy copy = copyDao.getById(id);
        if (copy == null) return;
        copy.setCost(updated.getCost());
        copy.setCount(updated.getCount());
    }
}
