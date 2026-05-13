package com.example.VideoRent.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.io.File;
import java.time.Duration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class SeleniumBaseTest {

    @LocalServerPort
    protected int port;

    protected WebDriver driver;
    protected WebDriverWait wait;

    @BeforeEach
    void setUpDriver() {
        if (new File("/usr/bin/chromedriver").exists()) {
            System.setProperty("webdriver.chrome.driver", "/usr/bin/chromedriver");
        } else {
            WebDriverManager.chromedriver().setup();
        }
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--no-sandbox", "--disable-dev-shm-usage", "--window-size=1920,1080");
        if (new File("/usr/bin/chromium").exists()) {
            options.setBinary("/usr/bin/chromium");
        }
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    @AfterEach
    void tearDownDriver() {
        if (driver != null) driver.quit();
    }

    protected void navigate(String path) {
        driver.get("http://localhost:" + port + path);
    }

    // телефонное поле имеет маску, поэтому устанавливаем значение через JS
    protected void setPhone(By by, String phone) {
        WebElement el = driver.findElement(by);
        ((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", el, phone);
    }

    protected void loginAs(String phone, String password) {
        navigate("/login");
        setPhone(By.name("telephoneNumber"), phone);
        driver.findElement(By.name("password")).sendKeys(password);
        submit(By.cssSelector("button[type='submit']"));
    }

    protected void waitForModal(String modalId) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("#" + modalId + ".show")));
    }

    protected WebElement waitFor(By by) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    protected void submit(WebElement btn) {
        btn.click();
        try {
            wait.until(ExpectedConditions.stalenessOf(btn));
        } catch (WebDriverException e) {
            // элемент пропал из документа — значит страница уже сменилась
        }
    }

    protected void submit(By by) {
        submit(waitFor(by));
    }

    protected String baseUrl() {
        return "http://localhost:" + port;
    }
}
