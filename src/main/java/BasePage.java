import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;
    private static final By ACCEPT_COOKIE_BUTTON = By.xpath("//button[contains(@class,'o-cookie-banner__agree')]");


    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, 10);
    }

    @Step("Accept cookie")
    public void acceptCookies() {
        try {
            WebElement acceptCookieButton = wait.until(ExpectedConditions.visibilityOfElementLocated(ACCEPT_COOKIE_BUTTON));
            Assert.assertTrue(acceptCookieButton.isDisplayed(), "Button 'Got it' is not founded");
            acceptCookieButton.click();
        } catch (Exception var2) {
            System.out.println("Cookie has already been accepted");
        }
    }

    @Step("Open link")
    public void OpenWebLink(String link)
    {
        driver.get(link);
    }
}