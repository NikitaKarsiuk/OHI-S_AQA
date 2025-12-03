package Page.Homepage;

import Page.Base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class FeedPage extends BasePage<FeedPage> {

    private static final By TITLE = By.xpath("//h1[@class='o-typography__heading1 home__main-title']");
    private static final By allTab = By.cssSelector("[id= 'tab-all']");
    private static final String url = "https://qa2.ohi-s.com/feed/";

    public FeedPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public FeedPage open() {
        driver.get("https://qa2.ohi-s.com/feed/");
        return this;
    }

    public boolean hasLoaded() {
        try {
            WebElement title = wait.until(ExpectedConditions.elementToBeClickable(allTab));
            return driver.findElement(allTab).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickElementInHeaderMenu(String elementText) {
        try {
            By element = By.xpath("//a[contains(@class, 'o-header-nav-links__link') and normalize-space()='Market']");
            WebElement webElement = wait.until(ExpectedConditions.elementToBeClickable(element));
            webElement.click();
        } catch (Exception e) {
            throw e;
        }
    }


}