package com.example.VideoRent.dao;

import com.example.VideoRent.entity.CommonEntity;
import java.util.List;

public interface CommonDao<T extends CommonEntity<ID>, ID> {

    T getById(ID id);

    List<T> getAll();

    void save(T entity);

    void delete(T entity);
}
