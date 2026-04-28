package com.example.VideoRent.dao;

import com.example.VideoRent.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao extends CommonDaoImpl<User, Long> {

    public UserDao() {
        super(User.class);
    }
}