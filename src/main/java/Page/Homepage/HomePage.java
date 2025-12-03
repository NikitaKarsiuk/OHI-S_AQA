package Page.Homepage;

import Page.Base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class HomePage extends BasePage<HomePage> {

    private static final By TITLE = By.xpath("//h1[@class='o-typography__span home__main-title']");
    private static final String url = "https://qa2.ohi-s.com/";
    private static final By LOGIN_BUTTON = By.xpath("//button[@data-test='login-btn']");
    public HomePage(WebDriver driver) {
        super(driver);
    }

    @Override
    public HomePage open() {
        driver.get(url);
        return this;
    }

    public boolean isOpened() {
        try {
            WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(TITLE));
            return driver.findElement(TITLE).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickElementInHeaderMenu(String elementText) {
        try {
            By element = By.xpath("//*[text()='" + elementText + "']");
            WebElement webElement = wait.until(ExpectedConditions.elementToBeClickable(element));
            webElement.click();
        } catch (Exception e) {
            throw e;
        }
    }

    public void clickLogInButton() {
        try {
            WebElement webElement = wait.until(ExpectedConditions.elementToBeClickable(LOGIN_BUTTON));
            webElement.click();
        } catch (Exception e) {
            throw e;
        }
    }
}