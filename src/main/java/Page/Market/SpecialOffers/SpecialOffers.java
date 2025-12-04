package Page.Market.SpecialOffers;

import Page.Base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class SpecialOffers extends BasePage<SpecialOffers>  {
    private static final String URL_PATTERN = "/market/offers";
    private static final By SPECIAL_OFFERS_TITLE =
            By.xpath("//h1[@class='o-typography__heading1 market-special-offers__title']");

    public SpecialOffers(WebDriver driver) {
        super(driver);
    }

    @Override
    @Step("Open special offers for product  page")
    public SpecialOffers open() {
        return this;
    }

    @Step("Check if special offers is opened")
    public boolean isOpened() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(SPECIAL_OFFERS_TITLE));
            return driver.getCurrentUrl().contains(URL_PATTERN);
        } catch (Exception e) {
            return false;
        }
    }
}
