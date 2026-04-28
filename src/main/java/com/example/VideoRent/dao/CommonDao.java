package com.example.VideoRent.dao;

import com.example.VideoRent.entity.CommonEntity;
import java.util.Collection;

public interface CommonDao<T extends CommonEntity<ID>, ID> {

    T getById(ID id);

    Collection<T> getAll();

    void save(T entity);

    void delete(T entity);
}
