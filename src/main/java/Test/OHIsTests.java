package Test;

import Page.Homepage.HomePage;
import io.qameta.allure.Description;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class OHIsTests extends BaseTest {
    private HomePage homePage;


    @BeforeMethod
    public void initPages() {
        homePage = new HomePage(driver);
    }

    @Test
    @Story("Home page check")
    @Description("The test checks whether the home page opens")
    public void testOpenHomepage() {
        homePage.open()
                .acceptCookies();
        Assert.assertTrue(homePage.isOpened(), "The page didn't load");
    }
}
