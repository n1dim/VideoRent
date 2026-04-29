package com.example.VideoRent.dao;

import com.example.VideoRent.entity.CommonEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;


public class CommonDaoImpl<T extends CommonEntity<ID>, ID> implements CommonDao<T, ID> {

    @PersistenceContext
    protected EntityManager entityManager;

    private Class<T> entityClass;

    public CommonDaoImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public T getById(ID id) {
        return entityManager.find(entityClass, id);
    }

    @Override
    public List<T> getAll() {
        return entityManager.createQuery("from " + entityClass.getSimpleName(), entityClass).getResultList();
    }

    @Override
    public void save(T entity) {
        entityManager.persist(entity);
    }

    @Override
    public void delete(T entity) {
        entityManager.remove(
                entityManager.contains(entity) ? entity : entityManager.merge(entity)
        );
    }
}
