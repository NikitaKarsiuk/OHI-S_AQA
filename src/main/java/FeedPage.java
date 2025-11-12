import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

public class FeedPage extends BasePage<FeedPage> {

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
            WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(allTab));
            return driver.findElement(allTab).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

}