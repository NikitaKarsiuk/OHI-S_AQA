package Page.Market.MyOrders;

import Page.Base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class OrderDetail extends BasePage<OrderDetail>  {

    private static final String URL_PATTERN = "/market/my-orders";
    private static final By SPECIAL_OFFERS_TITLE =
            By.xpath("//p[@class='o-typography__paragraph-16-500 my-order__back-button-text']");
    private static final By ORDER_NUMBER =
            By.xpath("//span[contains(@class, 'my-order-details__title')]");

    public OrderDetail(WebDriver driver) {
        super(driver);
    }

    @Override
    @Step("Open special offers for product  page")
    public OrderDetail open() {
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
    @Step("Get order number from header")
    public String getOrderNumberFromHeader() {
        try {
            WebElement titleElement = wait.until(ExpectedConditions.visibilityOfElementLocated(ORDER_NUMBER));
            String fullText = titleElement.getText().trim();

            if (fullText.startsWith("Order")) {
                return fullText.replace("Order", "").trim();
            }
            return fullText;

        } catch (Exception e) {
            throw new RuntimeException("Failed to get order number from header", e);
        }
    }
}
