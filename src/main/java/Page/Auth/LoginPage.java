package Page.Auth;

import Page.Base.BasePage;
import Page.Homepage.FeedPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage<LoginPage> {
    private final By TITLE = By.xpath("//p[@class='o-typography__paragraph-16-400 login__divider-text']");
    private final By LOGIN_FIELD = By.cssSelector("[data-test = 'email-field']");
    private final By PASSWORD_FIELD = By.cssSelector("[data-test='password-field']");
    private final By LOGIN_BUTTON = By.cssSelector("[data-test = 'signIn-btn']");
    private final String URL = "https://example/login/";
    public LoginPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public LoginPage open() {
        driver.get("https://example/login/");
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
    public FeedPage login(String email, String password){
        driver.findElement(LOGIN_FIELD).sendKeys(email);
        driver.findElement(PASSWORD_FIELD).sendKeys(password);
        driver.findElement(LOGIN_BUTTON).click();
        FeedPage feedPage = new FeedPage(driver);
        boolean isOpened = wait.until(d -> feedPage.hasLoaded());
        if (isOpened) {
            return feedPage;
        } else {
            throw new IllegalStateException("Home page did not open after login");
        }
    }
}
