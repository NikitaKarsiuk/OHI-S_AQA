package Page.Cart;

import Page.Base.BasePage;
import Page.Homepage.FeedPage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CartPage extends BasePage<CartPage> {

    private static final By CART_TITLE = By.xpath("//*[@class='o-typography__heading1 cart__title']");
    private static final String URL = "https://qa2.ohi-s.com/cart/";

    public CartPage(WebDriver driver) {
        super(driver);
    }

    @Override
    @Step("Open cart page")
    public CartPage open() {
        driver.get(URL);
        return this;
    }

    @Step("Check if cart page is opened")
    public boolean isOpened() {
        try {
            WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(CART_TITLE));
            return title.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Check if item is added in cart: {item}")
    public boolean isItemAddedInCart(String item) {
        try {
            WebElement cartItem = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//p[@class='o-typography__paragraph-16-400 product-title' and contains(text(), '" + item + "')]")
            ));
            return cartItem.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Get item quantity from cart: {item}\"")
    public int getItemQuantity(String item) {
        try {
            if (!isItemAddedInCart(item)) {
                throw new RuntimeException("Item '" + item + "' not found in cart");
            }

            By itemContainerLocator = By.xpath(
                    "//p[contains(text(), '" + item + "')]/ancestor::div[contains(@class, 'market-cart-product')]"
            );

            WebElement itemContainer = wait.until(ExpectedConditions.visibilityOfElementLocated(itemContainerLocator));

            WebElement quantityInput = itemContainer.findElement(
                    By.xpath(".//input[@type='number' and contains(@class, 'o-counter__input')]")
            );

            String quantityText = quantityInput.getAttribute("value");

            if (quantityText == null || quantityText.isEmpty()) {
                String placeholder = quantityInput.getAttribute("placeholder");
                if (placeholder != null && !placeholder.isEmpty()) {
                    return Integer.parseInt(placeholder);
                }
                throw new RuntimeException("Quantity value is empty for item: " + item);
            }

            return Integer.parseInt(quantityText);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get quantity for item: " + item, e);
        }
    }
}
