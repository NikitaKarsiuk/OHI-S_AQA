import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class OHIsTests extends BaseTest {
    private HomePage homePage;
    private BasePage basePage;
    private static final String webLink = "https://qa2.ohi-s.com/";

    @BeforeMethod
    public void initPages() {
        homePage = new HomePage(driver);
        basePage = new BasePage(driver);
    }

    @Test
    @Story("Home page check")
    @Description("The test checks whether the home page opens")
    public void testOpenHomepage() {
        basePage.OpenWebLink(webLink);
        basePage.acceptCookies();
        Assert.assertTrue(homePage.isOpenedHomePage(), "The page didn't load");
    }
}
