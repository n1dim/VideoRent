package com.example.VideoRent.service;

import com.example.VideoRent.dao.UserDao;
import com.example.VideoRent.entity.User;
import com.example.VideoRent.entity.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.security.MessageDigest;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class UserService {

    private static final Pattern PHONE_PATTERN = Pattern.compile("\\+7[0-9]{10}");

    private String hash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Transactional
    public RegisterResult register(User user) {
        if (isBlank(user.getFullName()) || isBlank(user.getTelephoneNumber())
                || isBlank(user.getHomeAddress()) || isBlank(user.getPasswordHash())) {
            return RegisterResult.MISSING_FIELDS;
        }
        if (!PHONE_PATTERN.matcher(user.getTelephoneNumber()).matches()) {
            return RegisterResult.INVALID_PHONE;
        }
        if (userDao.findByPhone(user.getTelephoneNumber()) != null) {
            return RegisterResult.PHONE_TAKEN;
        }
        user.setPasswordHash(hash(user.getPasswordHash()));
        user.setRole(Role.USER);
        user.setIsBlocked(false);
        userDao.save(user);
        return RegisterResult.SUCCESS;
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }

    public User login(String number, String passwd) {
        return userDao.findByPhoneAndPassword(number, hash(passwd));
    }

    public List<User> getAllUsers() {
        return userDao.getAll();
    }
}