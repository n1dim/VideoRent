package com.example.VideoRent.selenium;

import com.example.VideoRent.entity.Movie;
import com.example.VideoRent.service.MovieService;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MovieBrowsingTest extends SeleniumBaseTest {

    @Autowired
    private MovieService movieService;

    private Long testMovieId;

    @BeforeAll
    void createTestData() {
        Movie movie = new Movie();
        movie.setTitle("Selenium Test Movie");
        movie.setFilmDirector("Test Director");
        movie.setDescription("Test description for Selenium");
        movie.setCompany("Test Company");
        movie.setReleaseYear(2020);
        movieService.addMovie(movie);
        testMovieId = movieService.getAllMovies().stream()
                .filter(m -> "Selenium Test Movie".equals(m.getTitle()))
                .findFirst().map(Movie::getId).orElseThrow();
    }

    @AfterAll
    void cleanupTestData() {
        movieService.deleteMovie(testMovieId);
    }

    // карточки фильмов отображаются на главной
    @Test
    void testMovieListDisplayed() {
        navigate("/");
        List<WebElement> cards = driver.findElements(By.cssSelector(".movie-card"));
        assertFalse(cards.isEmpty(), "На главной странице должны отображаться карточки фильмов");
    }

    // поиск по названию возвращает нужный фильм
    @Test
    void testMovieSearch() {
        navigate("/?search=Selenium+Test+Movie");
        List<WebElement> cards = driver.findElements(By.cssSelector(".movie-card"));
        assertFalse(cards.isEmpty(), "Поиск должен вернуть хотя бы один результат");
        assertTrue(driver.getPageSource().contains("Selenium Test Movie"));
    }

    // страница фильма показывает все данные
    @Test
    void testMovieCardDisplayed() {
        navigate("/movie/" + testMovieId);
        assertTrue(driver.findElement(By.cssSelector(".movie-title")).getText().contains("Selenium Test Movie"));
        assertTrue(driver.getPageSource().contains("Test Director"));
        assertTrue(driver.getPageSource().contains("Test Company"));
        assertTrue(driver.getPageSource().contains("2020"));
        assertTrue(driver.getPageSource().contains("Test description for Selenium"));
    }

    // кнопка details ведёт на страницу фильма
    @Test
    void testNavigateToMovieCard() {
        navigate("/");
        WebElement detailsBtn = driver.findElement(
                By.xpath("//p[contains(text(),'Selenium Test Movie')]/ancestor::div[contains(@class,'movie-card')]//a[contains(@class,'btn-details')]"));
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                "arguments[0].click();", detailsBtn);
        assertTrue(driver.getCurrentUrl().contains("/movie/"));
        assertTrue(driver.getPageSource().contains("Selenium Test Movie"));
    }
}
