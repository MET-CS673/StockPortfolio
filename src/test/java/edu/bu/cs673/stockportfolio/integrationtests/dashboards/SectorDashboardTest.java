package edu.bu.cs673.stockportfolio.integrationtests.dashboards;

import edu.bu.cs673.stockportfolio.integrationtests.homepage.HomePage;
import edu.bu.cs673.stockportfolio.integrationtests.login.LoginPage;
import edu.bu.cs673.stockportfolio.integrationtests.signup.SignupPage;
import edu.bu.cs673.stockportfolio.integrationtests.utilityPages.ResultPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.*;

/**********************************************************************************************************************
 * Test user story: "As a user, I can signup, login, and view the sector dashboard for my portfolio."
 *********************************************************************************************************************/
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SectorDashboardTest {

    @LocalServerPort
    private int port;
    private static WebDriver driver;
    private String baseUrl;
    private SignupPage signupPage;
    private LoginPage loginPage;
    private HomePage homePage;
    private ResultPage resultPage;

    @BeforeAll
    public static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        driver = new ChromeDriver();
        baseUrl = "http://localhost:" + port;
        signupPage = new SignupPage(driver);
        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        resultPage = new ResultPage(driver);

        Dimension d = new Dimension(1382,744);
        driver.manage().window().setSize(d);
    }

    @Test
    @DisplayName("Test sector dashboard link")
    public void testMarketCapLink() {
        String filePath = "/Users/mlewis/Downloads/TestPortfolio.csv";

        driver.get(baseUrl + "/signup");
        signupPage.signup("wallstreet@spd.com", "wall", "wallStreet");

        driver.get(baseUrl + "/login");
        loginPage.login(driver, "wall", "wallStreet");

        // First upload a portfolio to ensure full integration testing with IEX Cloud
        driver.get(baseUrl + "/home");
        homePage.clickUploadPortfolio(driver, filePath);
        homePage.clickUploadPortfolioButton(driver);

        boolean result = resultPage.isSuccessMessageDisplayed(driver);
        resultPage.clickNavLink(driver);

        driver.get(baseUrl + "/home");
        homePage.clickSectorBreakdown(driver);
        String currentUrl = driver.getCurrentUrl();

        assertAll("Sector Breakdown Dashboard",
                () -> assertTrue(result, "Portfolio upload failed."),
                () -> assertEquals(baseUrl + "/sector_breakdown", currentUrl,
                        "Incorrect endpoint for sector dashboard")
        );
    }

    @AfterEach
    public void afterEach() {
        if (driver != null) {
            driver.quit();
        }

        driver = null;
    }
}
