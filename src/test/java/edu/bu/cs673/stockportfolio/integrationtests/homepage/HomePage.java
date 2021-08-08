package edu.bu.cs673.stockportfolio.integrationtests.homepage;

import edu.bu.cs673.stockportfolio.integrationtests.utilityPages.WaitPage;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

/**********************************************************************************************************************
 * The HomePage is a Selenium Page Object that serves as an interface to a this products home page. Integration tests
 * use the methods of this page object class whenever they need to interact with the UI of the page. The benefit is
 * that if the UI changes for the page, the tests themselves don’t need to change, only the code within the page object
 * needs to change. Subsequently all changes to support that new UI are located in one place.
 *
 * The Page Object Design Pattern provides the following advantages:
 *
 * There is a clean separation between test code and page specific code such as locators (or their use if you’re using
 * a UI Map) and layout. There is a single repository for the services or operations offered by the page rather than
 * having these services scattered throughout the tests.
 *********************************************************************************************************************/
public class HomePage extends WaitPage {

    private static final String UPLOAD_PORTFOLIO = "csvUpload";
    @FindBy(id = UPLOAD_PORTFOLIO)
    private WebElement addPortfolio;

    private static final String UPLOAD_PORTFOLIO_BUTTON = "fileUploadButton";
    @FindBy(id = UPLOAD_PORTFOLIO_BUTTON)
    private WebElement savePortfolio;

    private static final String NAV_STOCK_BREAKDOWN = "nav-stock-breakdown";
    @FindBy(id = NAV_STOCK_BREAKDOWN)
    private WebElement stockBreakdown;

    private static final String NAV_SECTOR_BREAKDOWN = "nav-sector-breakdown";
    @FindBy(id = NAV_SECTOR_BREAKDOWN)
    private WebElement sectorBreakdown;

    private static final String NAV_MARKET_CAP_BREAKDOWN = "nav-marketCap-breakdown";
    @FindBy(id = NAV_MARKET_CAP_BREAKDOWN)
    private WebElement marketCapBreakdown;

    private static final String PORTFOLIO_TABLE = "portfolioTable";
    @FindBy(id = PORTFOLIO_TABLE)
    private WebElement portfolioTable;

    private static final String LOGOUT_BTN = "logout-btn";
    @FindBy(id = LOGOUT_BTN)
    private WebElement logoutButton;

    public HomePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void clickUploadPortfolio(WebDriver driver, String filepath) {
        waitForElement(driver, UPLOAD_PORTFOLIO).sendKeys(filepath);
    }

    public void clickUploadPortfolioButton(WebDriver driver) {
        waitForElement(driver, UPLOAD_PORTFOLIO_BUTTON).click();
    }

    public void clickSectorBreakdown(WebDriver driver) {
        waitForElement(driver, NAV_SECTOR_BREAKDOWN).click();
    }

    public void clickMarketCapBreakdown(WebDriver driver) {
        waitForElement(driver, NAV_MARKET_CAP_BREAKDOWN).click();
    }

    public void clickStockBreakdown(WebDriver driver) {
        waitForElement(driver, NAV_STOCK_BREAKDOWN).click();
    }

    public String find(WebDriver driver, String text) {
        return waitForPageSearch(driver, text);
    }

    public void logout(WebDriver driver) {
        waitForElement(driver, LOGOUT_BTN).click();
    }

    private String getMostRecentAddedElementId(List<WebElement> buttons) {
        String mostRecentId = null;
        for (WebElement button : buttons) {
            mostRecentId = button.getAttribute("id");
        }

        return mostRecentId;
    }

    private void clickButton(List<WebElement> buttons, String id) {
        for (WebElement button : buttons) {
            if (button.getAttribute("id").equals(id)) {
                button.click();
                break;
            }
        }
    }

    private boolean isElementDisplayed(List<WebElement> buttons, String elementId) {
        for (WebElement button : buttons) {
            String id = button.getAttribute("id");
            if (id.equals(elementId)) {
                return true;
            }
        }

        return false;
    }
}