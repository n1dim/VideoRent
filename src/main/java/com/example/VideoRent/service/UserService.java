package com.example.VideoRent.service;

import com.example.VideoRent.dao.UserDao;
import com.example.VideoRent.entity.User;
import com.example.VideoRent.entity.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Transactional
    public void register(User user) {
        user.setRole(Role.USER);
        user.setIsBlocked(false);

        userDao.save(user);
    }

    public List<User> getAllUsers() {
        return userDao.getAll();
    }
}