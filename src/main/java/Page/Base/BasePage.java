package Page.Base;

import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;


@Slf4j
public abstract class BasePage<T extends BasePage<T>>  {

    protected WebDriver driver;
    protected WebDriverWait wait;
    private static final By ACCEPT_COOKIE_BUTTON = By.xpath("//button[contains(@class,'o-cookie-banner__agree')]");


    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
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
    public void openWebLink(String link)
    {
        driver.get(link);
    }

    public abstract T open();

    protected WebElement waitVisible(By by) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }


    protected WebElement waitClickable(By by) {
        return wait.until(ExpectedConditions.elementToBeClickable(by));
    }

    protected void waitGone(By by) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
    }

    protected void waitGone(WebElement el) {
        wait.until(ExpectedConditions.invisibilityOf(el));
    }

    protected void safeClick(By by) { safeClick(waitClickable(by)); }

    protected void safeClick(WebElement el) {
        try {
            el.click();
        } catch (WebDriverException e) {
            log.warn("Native click failed ({}). Using JS click.", e.getClass().getSimpleName());
            jsClick(el);
        }
    }

    protected void jsClick(WebElement el) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click()", el);
    }

    protected void clearAndType(WebElement input, String text) {
        try {
            input.clear();
            input.sendKeys(text);
        } catch (ElementNotInteractableException e) {
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].value=arguments[1]; arguments[0].dispatchEvent(new Event('input',{bubbles:true}))",
                    input, text
            );
        }
    }

    protected void scrollIntoView(WebElement el) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'})", el);
    }

}