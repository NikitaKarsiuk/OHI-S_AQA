import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

public class HomePage extends BasePage {

    private static final By TITLE = By.xpath("//h1[@class='o-typography__span home__main-title']");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public boolean isOpenedHomePage() {
        try {
            WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(TITLE));
            return driver.findElement(TITLE).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

}