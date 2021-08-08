package edu.bu.cs673.stockportfolio.integrationtests.utilityPages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**********************************************************************************************************************
 * A Selenium Page Object representing the result of user actions.
 *
 * "As a user, I can upload a CSV based portfolio and get notified whether or not the upload was successful."
 *********************************************************************************************************************/
public class ResultPage extends WaitPage {

    private static final String SUCCESS_MESSAGE = "success-message";
    @FindBy(id = SUCCESS_MESSAGE)
    private WebElement successMessage;

    private static final String ERROR_MESSAGE = "error-message";
    @FindBy(id = ERROR_MESSAGE)
    private WebElement errorMessage;

    private static final String NAV_LINK = "navlink";
    @FindBy(id = NAV_LINK)
    private WebElement navLink;

    public ResultPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public boolean isSuccessMessageDisplayed(WebDriver driver) {
        return isElementDisplayed(driver, SUCCESS_MESSAGE);
    }

    public boolean isErrorMessageDisplayed() {
        return errorMessage.isDisplayed();
    }

    public void clickNavLink(WebDriver driver) {
        waitForElement(driver, NAV_LINK);
    }
}
