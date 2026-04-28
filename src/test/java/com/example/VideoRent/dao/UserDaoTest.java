package com.example.VideoRent.dao;

import com.example.VideoRent.entity.Role;
import com.example.VideoRent.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import jakarta.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class UserDaoTest {

    @Autowired
    private UserDao userDao;

    @Test
    void testSaveAndGet() {
        User user = new User();
        user.setTelephoneNumber("123");
        user.setFullName("Test");
        user.setHomeAddress("Addr");
        user.setPasswordHash("hash");
        user.setRole(Role.USER);
        user.setIsBlocked(false);

        userDao.save(user);

        assertNotNull(user.getId());

        User fromDb = userDao.getById(user.getId());

        assertEquals("Test", fromDb.getFullName());
        assertEquals("123", fromDb.getTelephoneNumber());
        assertEquals("Addr", fromDb.getHomeAddress());
        assertEquals("hash", fromDb.getPasswordHash());
        assertEquals(Role.USER, fromDb.getRole());
        assertEquals(false, fromDb.getIsBlocked());
    }

    @Test
    void testGetAll() {
        User user = new User();
        user.setTelephoneNumber("111");
        user.setFullName("List Test");
        user.setHomeAddress("addr");
        user.setPasswordHash("hash");
        user.setRole(Role.USER);
        user.setIsBlocked(false);

        userDao.save(user);

        var list = userDao.getAll();

        assertFalse(list.isEmpty());
    }

    @Test
    void testDelete() {
        User user = new User();
        user.setTelephoneNumber("999");
        user.setFullName("Delete Me");
        user.setHomeAddress("addr");
        user.setPasswordHash("hash");
        user.setRole(Role.USER);
        user.setIsBlocked(false);

        userDao.save(user);

        Long id = user.getId();

        userDao.delete(user);

        assertNull(userDao.getById(id));
    }
}
