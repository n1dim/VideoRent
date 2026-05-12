package com.example.VideoRent.dao;

import com.example.VideoRent.entity.User;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class UserDao extends CommonDaoImpl<User, Long> {

    public UserDao() {
        super(User.class);
    }

    public List<User> findByName(String name) {
        return entityManager
                .createQuery("SELECT u FROM User u WHERE u.fullName LIKE :name", User.class)
                .setParameter("name", "%" + name + "%")
                .getResultList();
    }

    public User findByPhone(String number) {
        return entityManager
                .createQuery("SELECT u FROM User u WHERE u.telephoneNumber = :number", User.class)
                .setParameter("number", number)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    public User findByPhoneAndPassword(String number, String passwd) {
        return entityManager
                .createQuery("SELECT u FROM User u WHERE u.telephoneNumber = :number AND u.passwordHash = :passwd", User.class)
                .setParameter("number", number)
                .setParameter("passwd", passwd)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }
}