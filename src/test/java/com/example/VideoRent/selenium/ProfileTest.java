package com.example.VideoRent.selenium;

import com.example.VideoRent.dao.CopyDao;
import com.example.VideoRent.dao.RentalDao;
import com.example.VideoRent.dao.UserDao;
import com.example.VideoRent.entity.Rental;
import com.example.VideoRent.entity.User;
import com.example.VideoRent.service.RentalService;
import com.example.VideoRent.service.UserService;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProfileTest extends SeleniumBaseTest {

    static final String PROFILE_PHONE    = "+70000000021";
    static final String PROFILE_PASSWORD = "profilepass123";
    static final String PWCHANGE_PHONE   = "+70000000022";

    @Autowired UserService userService;
    @Autowired UserDao     userDao;
    @Autowired RentalService rentalService;
    @Autowired RentalDao   rentalDao;
    @Autowired CopyDao     copyDao;
    @Autowired PlatformTransactionManager txManager;

    @BeforeAll
    void createTestData() {
        deleteIfExists(PROFILE_PHONE);
        deleteIfExists(PWCHANGE_PHONE);

        User user = new User();
        user.setFullName("Profile Test User");
        user.setTelephoneNumber(PROFILE_PHONE);
        user.setHomeAddress("Profile Address");
        user.setPasswordHash(PROFILE_PASSWORD);
        userService.register(user);

        User pwUser = new User();
        pwUser.setFullName("PW Change User");
        pwUser.setTelephoneNumber(PWCHANGE_PHONE);
        pwUser.setHomeAddress("PW Address");
        pwUser.setPasswordHash(PROFILE_PASSWORD);
        userService.register(pwUser);

        // нужна активная аренда, чтобы форма продления появилась в профиле
        copyDao.getAll().stream()
                .filter(c -> c.getCount() > 0)
                .findFirst()
                .ifPresent(c -> rentalService.issue(c.getId(), PROFILE_PHONE, LocalDate.now().plusDays(10)));
    }

    @AfterAll
    void cleanup() {
        User user = userDao.findByPhone(PROFILE_PHONE);
        if (user != null) {
            List<Rental> rentals = rentalDao.findByUserId(user.getId());
            new TransactionTemplate(txManager).execute(status -> {
                rentals.forEach(rentalDao::delete);
                return null;
            });
        }
        deleteIfExists(PROFILE_PHONE);
        deleteIfExists(PWCHANGE_PHONE);
    }

    void deleteIfExists(String phone) {
        User u = userDao.findByPhone(phone);
        if (u == null) return;
        new TransactionTemplate(txManager).execute(status -> {
            userDao.delete(u);
            return null;
        });
    }

    // профиль показывает данные пользователя и историю аренды
    @Test
    void testViewProfile() {
        loginAs(PROFILE_PHONE, PROFILE_PASSWORD);
        navigate("/profile");
        assertTrue(driver.getPageSource().contains(PROFILE_PHONE), "Phone should appear on profile page");
        assertTrue(driver.getPageSource().contains("Personal info"), "Personal info section should be visible");
        assertTrue(driver.getPageSource().contains("Rental history"), "Rental history section should be visible");
    }

    // изменение имени и адреса сохраняется
    @Test
    void testEditProfile() {
        loginAs(PROFILE_PHONE, PROFILE_PASSWORD);
        navigate("/profile");
        driver.findElement(By.cssSelector("button[data-bs-target='#editModal']")).click();
        waitForModal("editModal");
        WebElement nameInput = waitFor(By.cssSelector("#editModal input[name='fullName']"));
        nameInput.clear();
        nameInput.sendKeys("Edited Profile Name");
        WebElement addrInput = driver.findElement(By.cssSelector("#editModal input[name='homeAddress']"));
        addrInput.clear();
        addrInput.sendKeys("Edited Address");
        submit(By.cssSelector("#editModal button[type='submit']"));
        assertTrue(driver.getPageSource().contains("Edited Profile Name"), "Updated name should appear on profile page");
    }

    // неверный старый пароль — редирект с ошибкой
    @Test
    void testChangePasswordWrongOld() {
        loginAs(PROFILE_PHONE, PROFILE_PASSWORD);
        navigate("/profile");
        driver.findElement(By.cssSelector("button[data-bs-target='#passwordModal']")).click();
        waitForModal("passwordModal");
        waitFor(By.cssSelector("#passwordModal input[name='oldPassword']")).sendKeys("wrongpassword");
        driver.findElement(By.cssSelector("#passwordModal input[name='newPassword']")).sendKeys("newpass999");
        submit(By.cssSelector("#passwordModal button[type='submit']"));
        assertTrue(driver.getCurrentUrl().contains("passwordError"),
                "Wrong old password should produce ?passwordError in redirect URL");
    }

    // смена пароля с верным старым — успех
    @Test
    void testChangePassword() {
        loginAs(PWCHANGE_PHONE, PROFILE_PASSWORD);
        navigate("/profile");
        driver.findElement(By.cssSelector("button[data-bs-target='#passwordModal']")).click();
        waitForModal("passwordModal");
        waitFor(By.cssSelector("#passwordModal input[name='oldPassword']")).sendKeys(PROFILE_PASSWORD);
        driver.findElement(By.cssSelector("#passwordModal input[name='newPassword']")).sendKeys("newpass456");
        driver.findElement(By.cssSelector("#passwordModal button[type='submit']")).click();
        assertFalse(driver.getCurrentUrl().contains("passwordError"),
                "Correct old password should not produce error");
        assertEquals(baseUrl() + "/profile", driver.getCurrentUrl(),
                "Should redirect back to /profile on success");
    }

    // запрос продления аренды отображается в профиле
    @Test
    void testRequestExtension() {
        loginAs(PROFILE_PHONE, PROFILE_PASSWORD);
        navigate("/profile");
        WebElement dateInput = waitFor(By.cssSelector("input[name='requestedDate']"));
        String futureDate = LocalDate.now().plusDays(14).toString();
        ((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", dateInput, futureDate);
        submit(By.cssSelector("button.btn-outline-warning"));
        assertTrue(driver.getPageSource().contains("Requested:"),
                "'Requested:' label should appear after requesting extension");
    }
}
