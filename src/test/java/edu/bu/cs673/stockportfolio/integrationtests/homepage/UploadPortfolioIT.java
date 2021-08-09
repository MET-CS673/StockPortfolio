package edu.bu.cs673.stockportfolio.integrationtests.homepage;

import edu.bu.cs673.stockportfolio.integrationtests.homepage.HomePage;
import edu.bu.cs673.stockportfolio.integrationtests.login.LoginPage;
import edu.bu.cs673.stockportfolio.integrationtests.utilityPages.ResultPage;
import edu.bu.cs673.stockportfolio.integrationtests.signup.SignupPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.*;

/**********************************************************************************************************************
 * Test user story: "As a user, I can signup, login, and upload my portfolio."
 *********************************************************************************************************************/
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UploadPortfolioIT {

    @LocalServerPort
    private int port;

    private static WebDriver driver;
    private String baseURL;
    private SignupPage signupPage;
    private LoginPage loginPage;
    private HomePage homePage;
    private ResultPage resultPage;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        driver = new ChromeDriver();
        baseURL = "http://localhost:" + port;
        signupPage = new SignupPage(driver);
        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        resultPage = new ResultPage(driver);
    }

    @Test
    @DisplayName("Test portfolio upload.")
    public void testPortfolioUpload() {
        String filePath = "/Users/mlewis/Downloads/TestPortfolio.csv";
        String testTickerOne = "GS";
        String testTickerTwo = "FB";
        String testCompanyNameOne = "Goldman Sachs Group, Inc.";
        String testCompanyNameTwo = "Facebook Inc - Class A";

        driver.get(baseURL + "/signup");
        signupPage.signup("money@spd.com", "John", "10DigitPassword!");

        driver.get(baseURL + "/login");
        loginPage.login(driver, "John", "10DigitPassword!");

        // Go to homepage and upload file
        driver.get(baseURL + "/home");
        homePage.clickUploadPortfolio(driver, filePath);
        homePage.clickUploadPortfolioButton(driver);

        boolean result = resultPage.isSuccessMessageDisplayed(driver);
        resultPage.clickNavLink(driver);

        driver.get(baseURL + "/home");
        String actualTickerOne = homePage.find(driver, testTickerOne);
        String actualTickerTwo = homePage.find(driver, testTickerTwo);
        String actualCompanyNameOne = homePage.find(driver, testCompanyNameOne);
        String actualCompanyNameTwo = homePage.find(driver, testCompanyNameTwo);

        assertAll("Upload portfolio",
                () -> assertTrue(result, "Portfolio upload failed."),
                () -> assertEquals(testTickerOne, actualTickerOne, "GS symbol is incorrect."),
                () -> assertEquals(testTickerTwo, actualTickerTwo, "FB symbol is incorrect."),
                () -> assertEquals(testCompanyNameOne, actualCompanyNameOne, "Company name is incorrect."),
                () -> assertEquals(testCompanyNameTwo, actualCompanyNameTwo, "Company name is incorrect."));
    }

    @AfterEach
    public void afterEach() {
        if (driver != null) {
            driver.quit();
        }
        driver = null;
    }
}
