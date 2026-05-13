package com.example.VideoRent.selenium;

import com.example.VideoRent.dao.UserDao;
import com.example.VideoRent.entity.User;
import com.example.VideoRent.service.UserService;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthTest extends SeleniumBaseTest {

    static final String USER_PHONE    = "+70000000011";
    static final String USER_PASSWORD = "testpass123";
    static final String BLOCKED_PHONE = "+70000000012";

    @Autowired UserService userService;
    @Autowired UserDao     userDao;
    @Autowired PlatformTransactionManager txManager;

    @BeforeAll
    void createTestData() {
        deleteIfExists(USER_PHONE);
        deleteIfExists(BLOCKED_PHONE);

        User user = new User();
        user.setFullName("Auth Test User");
        user.setTelephoneNumber(USER_PHONE);
        user.setHomeAddress("Test Address");
        user.setPasswordHash(USER_PASSWORD);
        userService.register(user);

        User blocked = new User();
        blocked.setFullName("Blocked User");
        blocked.setTelephoneNumber(BLOCKED_PHONE);
        blocked.setHomeAddress("Blocked Address");
        blocked.setPasswordHash(USER_PASSWORD);
        userService.register(blocked);
        userService.blockUser(userDao.findByPhone(BLOCKED_PHONE).getId(), "Test block reason");
    }

    @AfterAll
    void cleanupTestData() {
        deleteIfExists("+70000000013");
        deleteIfExists(USER_PHONE);
        deleteIfExists(BLOCKED_PHONE);
    }

    void deleteIfExists(String phone) {
        User u = userDao.findByPhone(phone);
        if (u == null) return;
        new TransactionTemplate(txManager).execute(status -> {
            userDao.delete(u);
            return null;
        });
    }

    // успешная регистрация — редирект на страницу с успехом
    @Test
    void testRegistration() {
        navigate("/register");
        driver.findElement(By.name("fullName")).sendKeys("New Selenium User");
        setPhone(By.name("telephoneNumber"), "+70000000013");
        driver.findElement(By.name("homeAddress")).sendKeys("Some Address");
        driver.findElement(By.name("passwordHash")).sendKeys("password123");
        submit(By.cssSelector("button[type='submit']"));
        assertTrue(driver.getCurrentUrl().contains("register?success"), "После регистрации должен быть редирект на /register?success");
    }

    // регистрация с занятым номером — ошибка
    @Test
    void testRegistrationPhoneTaken() {
        navigate("/register");
        driver.findElement(By.name("fullName")).sendKeys("Duplicate User");
        setPhone(By.name("telephoneNumber"), USER_PHONE);
        driver.findElement(By.name("homeAddress")).sendKeys("Address");
        driver.findElement(By.name("passwordHash")).sendKeys("pass");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        assertTrue(driver.getPageSource().contains("already taken") || driver.getCurrentUrl().contains("register"),
                "При дублировании телефона должна отображаться ошибка");
    }

    // успешный вход — редирект на главную с именем пользователя
    @Test
    void testLoginSuccess() {
        loginAs(USER_PHONE, USER_PASSWORD);
        assertEquals(baseUrl() + "/", driver.getCurrentUrl(), "После входа должен быть редирект на /");
        assertTrue(driver.getPageSource().contains("Auth Test User"), "Должно отображаться имя пользователя");
    }

    // неверный пароль — ошибка в URL и на странице
    @Test
    void testLoginWrongPassword() {
        loginAs(USER_PHONE, "wrongpassword");
        assertTrue(driver.getCurrentUrl().contains("error"), "При неверном пароле должен быть параметр error в URL");
        assertTrue(driver.getPageSource().contains("Invalid phone or password"));
    }

    // вход заблокированного — показывается причина блокировки
    @Test
    void testLoginBlockedUser() {
        loginAs(BLOCKED_PHONE, USER_PASSWORD);
        assertTrue(driver.getPageSource().contains("Account blocked"), "Должно отображаться сообщение о блокировке");
        assertTrue(driver.getPageSource().contains("Test block reason"), "Должна отображаться причина блокировки");
    }
}
