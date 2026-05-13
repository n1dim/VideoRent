package com.example.VideoRent.selenium;

import com.example.VideoRent.dao.CopyDao;
import com.example.VideoRent.dao.MediaDao;
import com.example.VideoRent.dao.RentalDao;
import com.example.VideoRent.dao.UserDao;
import com.example.VideoRent.entity.Copy;
import com.example.VideoRent.entity.Movie;
import com.example.VideoRent.entity.Rental;
import com.example.VideoRent.entity.Role;
import com.example.VideoRent.entity.User;
import com.example.VideoRent.service.CopyService;
import com.example.VideoRent.service.MovieService;
import com.example.VideoRent.service.RentalService;
import com.example.VideoRent.service.UserService;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AdminTest extends SeleniumBaseTest {

    static final String ADMIN_PHONE        = "+70000000031";
    static final String ADMIN_PASSWORD     = "adminpass123";
    static final String REG_USER_PHONE     = "+70000000032";
    static final String BLOCK_USER_PHONE   = "+70000000033";
    static final String UNBLOCK_USER_PHONE = "+70000000034";
    static final String TEST_USER_PASS     = "testpass123";

    @Autowired UserService   userService;
    @Autowired UserDao       userDao;
    @Autowired MovieService  movieService;
    @Autowired CopyService   copyService;
    @Autowired CopyDao       copyDao;
    @Autowired MediaDao      mediaDao;
    @Autowired RentalService rentalService;
    @Autowired RentalDao     rentalDao;
    @Autowired PlatformTransactionManager txManager;

    private Long testMovieId;
    private Long deleteMovieId;
    private Long testCopyId;
    private Long regUserId;
    private Long blockUserId;
    private Long unblockUserId;
    private Long returnRentalId;
    private Long approveRentalId;
    private Long rejectRentalId;

    @BeforeAll
    void createTestData() {
        deleteIfExists(ADMIN_PHONE);
        User admin = new User();
        admin.setFullName("Admin Test User");
        admin.setTelephoneNumber(ADMIN_PHONE);
        admin.setHomeAddress("Admin Address");
        admin.setPasswordHash(ADMIN_PASSWORD);
        userService.register(admin);
        setAdminRole(ADMIN_PHONE);

        deleteIfExists(REG_USER_PHONE);
        User regUser = new User();
        regUser.setFullName("Regular Test User");
        regUser.setTelephoneNumber(REG_USER_PHONE);
        regUser.setHomeAddress("Regular Address");
        regUser.setPasswordHash(TEST_USER_PASS);
        userService.register(regUser);
        regUserId = userDao.findByPhone(REG_USER_PHONE).getId();

        deleteIfExists(BLOCK_USER_PHONE);
        User blockUser = new User();
        blockUser.setFullName("Block Target User");
        blockUser.setTelephoneNumber(BLOCK_USER_PHONE);
        blockUser.setHomeAddress("Block Address");
        blockUser.setPasswordHash(TEST_USER_PASS);
        userService.register(blockUser);
        blockUserId = userDao.findByPhone(BLOCK_USER_PHONE).getId();

        deleteIfExists(UNBLOCK_USER_PHONE);
        User unblockUser = new User();
        unblockUser.setFullName("Unblock Target User");
        unblockUser.setTelephoneNumber(UNBLOCK_USER_PHONE);
        unblockUser.setHomeAddress("Unblock Address");
        unblockUser.setPasswordHash(TEST_USER_PASS);
        userService.register(unblockUser);
        unblockUserId = userDao.findByPhone(UNBLOCK_USER_PHONE).getId();
        userService.blockUser(unblockUserId, "Pre-blocked for test");

        Movie testMovie = new Movie();
        testMovie.setTitle("Admin Test Movie");
        testMovie.setFilmDirector("Admin Director");
        testMovie.setDescription("Admin test description");
        testMovie.setCompany("Admin Company");
        testMovie.setReleaseYear(2023);
        movieService.addMovie(testMovie);
        testMovieId = movieService.getAllMovies().stream()
                .filter(m -> "Admin Test Movie".equals(m.getTitle()))
                .findFirst().map(Movie::getId).orElseThrow();

        movieService.getAllMovies().stream()
                .filter(m -> "Delete Test Movie".equals(m.getTitle()))
                .forEach(m -> movieService.deleteMovie(m.getId()));

        Movie delMovie = new Movie();
        delMovie.setTitle("Delete Test Movie");
        delMovie.setFilmDirector("Delete Director");
        delMovie.setDescription("This movie will be deleted in test");
        delMovie.setCompany("Delete Company");
        delMovie.setReleaseYear(2022);
        movieService.addMovie(delMovie);
        deleteMovieId = movieService.getAllMovies().stream()
                .filter(m -> "Delete Test Movie".equals(m.getTitle()))
                .findFirst().map(Movie::getId).orElseThrow();

        Copy copy = new Copy();
        copy.setFilm(movieService.getMovie(testMovieId));
        copy.setMedia(mediaDao.getAll().get(0));
        copy.setCost(10);
        copy.setCount(5);
        copyService.save(copy);
        testCopyId = copyService.getCopiesByMovie(testMovieId).get(0).getId();

        rentalService.issue(testCopyId, REG_USER_PHONE, LocalDate.now().plusDays(7));
        returnRentalId = rentalDao.findByUserId(regUserId).stream()
                .filter(r -> LocalDate.now().plusDays(7).equals(r.getDueDate()))
                .findFirst().map(Rental::getId).orElseThrow();

        rentalService.issue(testCopyId, REG_USER_PHONE, LocalDate.now().plusDays(14));
        approveRentalId = rentalDao.findByUserId(regUserId).stream()
                .filter(r -> LocalDate.now().plusDays(14).equals(r.getDueDate()))
                .findFirst().map(Rental::getId).orElseThrow();
        rentalService.requestExtension(approveRentalId, LocalDate.now().plusDays(22));

        rentalService.issue(testCopyId, REG_USER_PHONE, LocalDate.now().plusDays(30));
        rejectRentalId = rentalDao.findByUserId(regUserId).stream()
                .filter(r -> LocalDate.now().plusDays(30).equals(r.getDueDate()))
                .findFirst().map(Rental::getId).orElseThrow();
        rentalService.requestExtension(rejectRentalId, LocalDate.now().plusDays(40));
    }

    @AfterAll
    void cleanup() {
        List<Rental> rentals = rentalDao.findByUserId(regUserId);
        if (!rentals.isEmpty()) {
            new TransactionTemplate(txManager).execute(status -> {
                rentals.forEach(rentalDao::delete);
                return null;
            });
        }
        if (testMovieId != null) movieService.deleteMovie(testMovieId);
        if (deleteMovieId != null) movieService.deleteMovie(deleteMovieId);
        // фильм, добавленный через UI в testAddMovie
        movieService.getAllMovies().stream()
                .filter(m -> "Test Add Movie Title".equals(m.getTitle()))
                .forEach(m -> movieService.deleteMovie(m.getId()));
        deleteIfExists(ADMIN_PHONE);
        deleteIfExists(REG_USER_PHONE);
        deleteIfExists(BLOCK_USER_PHONE);
        deleteIfExists(UNBLOCK_USER_PHONE);
    }

    void deleteIfExists(String phone) {
        User u = userDao.findByPhone(phone);
        if (u == null) return;
        new TransactionTemplate(txManager).execute(status -> {
            userDao.delete(u);
            return null;
        });
    }

    void setAdminRole(String phone) {
        Long id = userDao.findByPhone(phone).getId();
        userService.setRole(id, Role.ADMIN);
    }

    // панель показывает статистику: пользователи, фильмы, аренды
    @Test
    void testAdminPanel() {
        loginAs(ADMIN_PHONE, ADMIN_PASSWORD);
        navigate("/admin");
        assertTrue(driver.getPageSource().contains("Users"), "Admin panel should show Users stat");
        assertTrue(driver.getPageSource().contains("Movies"), "Admin panel should show Movies stat");
        assertTrue(driver.getPageSource().contains("Active rentals"), "Admin panel should show Active rentals stat");
        assertFalse(driver.findElements(By.cssSelector(".stat-value")).isEmpty(),
                "Stat value elements should be present");
    }

    // добавление фильма через форму
    @Test
    void testAddMovie() {
        loginAs(ADMIN_PHONE, ADMIN_PASSWORD);
        navigate("/movies/add");
        driver.findElement(By.name("title")).sendKeys("Test Add Movie Title");
        driver.findElement(By.name("filmDirector")).sendKeys("Test Director");
        driver.findElement(By.name("description")).sendKeys("Test description text");
        driver.findElement(By.name("company")).sendKeys("Test Company");
        driver.findElement(By.name("releaseYear")).sendKeys("2024");
        submit(By.cssSelector("button[type='submit']"));
        assertEquals(baseUrl() + "/", driver.getCurrentUrl(),
                "After adding movie should redirect to home page");
        assertTrue(driver.getPageSource().contains("Test Add Movie Title"),
                "New movie should appear on home page");
    }

    // редактирование названия фильма сохраняется
    @Test
    void testEditMovie() {
        loginAs(ADMIN_PHONE, ADMIN_PASSWORD);
        navigate("/movie/" + testMovieId);
        submit(By.cssSelector("a.btn-outline-light[href*='/movies/edit/']"));
        WebElement titleInput = waitFor(By.name("title"));
        titleInput.clear();
        titleInput.sendKeys("Edited Movie Title");
        submit(By.cssSelector("button[type='submit']"));
        assertTrue(driver.getCurrentUrl().contains("/movie/" + testMovieId),
                "Should redirect to movie detail page after edit");
        assertTrue(driver.getPageSource().contains("Edited Movie Title"),
                "Edited title should appear on movie page");
    }

    // удалённый фильм не появляется на главной
    @Test
    void testDeleteMovie() {
        loginAs(ADMIN_PHONE, ADMIN_PASSWORD);
        navigate("/movie/" + deleteMovieId);
        driver.findElement(By.cssSelector("button[data-bs-target='#deleteModal']")).click();
        waitForModal("deleteModal");
        submit(By.cssSelector("#deleteModal button[form='deleteForm']"));
        assertEquals(baseUrl() + "/", driver.getCurrentUrl(),
                "After delete should redirect to home");
        assertFalse(driver.getPageSource().contains("Delete Test Movie"),
                "Soft-deleted movie should not appear on home page");
    }

    // выдача фильма пользователю
    @Test
    void testIssueFilm() {
        loginAs(ADMIN_PHONE, ADMIN_PASSWORD);
        navigate("/movie/" + testMovieId);
        driver.findElement(By.cssSelector("button[data-bs-target='#issueModal']")).click();
        waitForModal("issueModal");
        WebElement phoneInput = waitFor(By.id("issuePhone"));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].value = arguments[1];", phoneInput, REG_USER_PHONE);
        new Select(driver.findElement(By.cssSelector("#issueModal select[name='copyId']")))
                .selectByIndex(1);
        String tomorrow = LocalDate.now().plusDays(1).toString();
        WebElement dateInput = driver.findElement(By.id("issueDueDate"));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].value = arguments[1];", dateInput, tomorrow);
        driver.findElement(By.cssSelector("#issueModal button[type='submit']")).click();
        assertFalse(driver.getCurrentUrl().contains("issueError"),
                "Film issue should succeed without error");
    }

    // возврат фильма — аренда помечается как завершённая
    @Test
    void testReturnFilm() {
        loginAs(ADMIN_PHONE, ADMIN_PASSWORD);
        navigate("/admin/users/" + regUserId);
        waitFor(By.cssSelector("form[action*='/rentals/" + returnRentalId + "/return'] button")).click();
        assertTrue(driver.getCurrentUrl().contains("/admin/users/" + regUserId),
                "Should redirect back to admin user page after return");
        assertTrue(driver.getPageSource().contains("badge-returned"),
                "Returned badge should appear after returning the film");
        assertTrue(driver.findElements(By.cssSelector(
                "form[action*='/rentals/" + returnRentalId + "/return'] button")).isEmpty(),
                "Return button should be gone for the returned rental");
    }

    // блокировка пользователя с указанием причины
    @Test
    void testBlockUser() {
        loginAs(ADMIN_PHONE, ADMIN_PASSWORD);
        navigate("/admin/users/" + blockUserId);
        driver.findElement(By.cssSelector("button[data-bs-target='#blockModal']")).click();
        waitForModal("blockModal");
        waitFor(By.cssSelector("#blockModal input[name='reason']")).sendKeys("Violation of terms");
        submit(By.cssSelector("#blockModal button[type='submit']"));
        assertTrue(driver.getPageSource().contains("Blocked"),
                "User should show Blocked status after blocking");
        assertTrue(driver.getPageSource().contains("Violation of terms"),
                "Block reason should be displayed on user page");
    }

    // разблокировка — статус меняется на Active
    @Test
    void testUnblockUser() {
        loginAs(ADMIN_PHONE, ADMIN_PASSWORD);
        navigate("/admin/users/" + unblockUserId);
        assertTrue(driver.getPageSource().contains("Pre-blocked for test"),
                "Pre-blocked user should show block reason");
        submit(By.cssSelector("form[action*='/unblock'] button"));
        assertTrue(driver.getPageSource().contains("Active"),
                "User should show Active status after unblocking");
    }

    // одобрение продления — кнопка исчезает
    @Test
    void testApproveExtension() {
        loginAs(ADMIN_PHONE, ADMIN_PASSWORD);
        navigate("/admin/users/" + regUserId);
        submit(By.cssSelector(
                "form[action*='/rentals/" + approveRentalId + "/approve-extension'] button"));
        assertTrue(driver.getCurrentUrl().contains("/admin/users/" + regUserId),
                "Should redirect back to admin user page after approval");
        assertTrue(driver.findElements(By.cssSelector(
                "form[action*='/rentals/" + approveRentalId + "/approve-extension'] button")).isEmpty(),
                "Approve button should disappear after extension is approved");
    }

    // отклонение продления — кнопка исчезает
    @Test
    void testRejectExtension() {
        loginAs(ADMIN_PHONE, ADMIN_PASSWORD);
        navigate("/admin/users/" + regUserId);
        submit(By.cssSelector(
                "form[action*='/rentals/" + rejectRentalId + "/reject-extension'] button"));
        assertTrue(driver.getCurrentUrl().contains("/admin/users/" + regUserId),
                "Should redirect back to admin user page after rejection");
        assertTrue(driver.findElements(By.cssSelector(
                "form[action*='/rentals/" + rejectRentalId + "/reject-extension'] button")).isEmpty(),
                "Reject button should disappear after extension is rejected");
    }
}
