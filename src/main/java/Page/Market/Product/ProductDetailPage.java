package Page.Market.Product;

import Page.Base.BasePage;
import Page.Market.MyOrders.MyOrders;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class ProductDetailPage extends BasePage<ProductDetailPage> {
    private static final By PRODUCT_TITLE =
            By.xpath("//h1[@class='o-typography__heading1 product__title']");
    private static final By PRODUCT_PRICE =
            By.xpath("//*[@class='o-typography__span-number-18-700 price-block__current-price']");
    private static final By ADD_TO_CART_BUTTON =
            By.xpath("//div[@class='prices main__price-desktop']//button[@class='el-button el-button--danger buttons__cart']");
    private static final By CART_ICON =
            By.xpath("//*[@class='header-icon-controls__item el-tooltip__trigger el-tooltip__trigger']");
    private static final By ADD_TO_CART_SUCCESS_MESSAGE =
            By.cssSelector(".notification-success");
    private static final By ADD_PRODUCT =
            By.xpath("//div[@class='prices main__price-desktop']//button[contains(@class,'button-plus')]");
    private static final By AMOUNT_OF_PRODUCT =
            By.xpath("//div[@class='prices main__price-desktop']//input[@class='o-counter__input']");
    private static final String URL_PATTERN = "/market/product/";

    public ProductDetailPage(WebDriver driver) {
        super(driver);
    }

    @Override
    @Step("Open product detail page")
    public ProductDetailPage open() {
        return this;
    }

    @Step("Check if product detail page is opened")
    public boolean isOpened() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(PRODUCT_TITLE));
            return driver.getCurrentUrl().contains(URL_PATTERN);
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Get product title")
    public String getProductTitle() {
        try {
            WebElement titleElement = wait.until(ExpectedConditions.visibilityOfElementLocated(PRODUCT_TITLE));
            return titleElement.getText().trim();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get product title", e);
        }
    }

    @Step("Get product price")
    public String getProductPrice() {
        try {
            WebElement priceElement = wait.until(ExpectedConditions.visibilityOfElementLocated(PRODUCT_PRICE));
            return priceElement.getText().trim();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get product price", e);
        }
    }

    @Step("Add product to cart")
    public ProductDetailPage AddProduct() {
        try {
            WebElement addProduct = wait.until(ExpectedConditions.elementToBeClickable(ADD_PRODUCT));
            addProduct.click();
            return this;
        } catch (Exception e) {
            throw new RuntimeException("Failed to add product to cart", e);
        }
    }

    @Step("Get amount of product")
    public int getAmountOfProduct() {
        try {
            WebElement amountOfProduct = wait.until(ExpectedConditions.elementToBeClickable(AMOUNT_OF_PRODUCT));
            return Integer.parseInt(amountOfProduct.getAttribute("value"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to get amount of product", e);
        }
    }

    @Step("Add product to cart")
    public ProductDetailPage addToCart() {
        try {
            WebElement addToCartButton = wait.until(ExpectedConditions.elementToBeClickable(ADD_TO_CART_BUTTON));
            addToCartButton.click();
            return this;
        } catch (Exception e) {
            throw new RuntimeException("Failed to add product to cart", e);
        }
    }

    @Step("Go to cart")
    public void goToCart() {
        try {
            WebElement iconCart = wait.until(ExpectedConditions.elementToBeClickable(CART_ICON));
            iconCart.click();
        } catch (Exception e) {
            throw new RuntimeException("Failed to go to cart", e);
        }
    }

    @Step("Add product to cart and go to cart")
    public void addToCartAndGoToCart() {
        try {
            addToCart();
            wait.until(ExpectedConditions.invisibilityOfElementLocated(ADD_TO_CART_SUCCESS_MESSAGE));
            goToCart();
        } catch (Exception e) {
            throw new RuntimeException("Failed to add product to cart and go to cart", e);
        }
    }

    @Step("Get product info")
    public ProductInfo getProductInfo() {
        try {
            String title = getProductTitle();
            String price = getProductPrice();
            return new ProductInfo(title, price);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get product info", e);
        }
    }

    @Step("Verify product detail page is opened")
    public ProductDetailPage verifyPageOpened() {
        if (!isOpened()) {
            throw new RuntimeException("Product detail page is not opened");
        }
        return this;
    }

    @Step("Check if add to cart button is displayed")
    public boolean isAddToCartButtonDisplayed() {
        try {
            WebElement addToCartButton = driver.findElement(ADD_TO_CART_BUTTON);
            return addToCartButton.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Check if cart icon is displayed")
    public boolean isCartIconDisplayed() {
        try {
            WebElement cartIcon = driver.findElement(CART_ICON);
            return cartIcon.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Wait for product details to load")
    public void waitForProductDetailsToLoad() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(PRODUCT_TITLE));
            wait.until(ExpectedConditions.visibilityOfElementLocated(PRODUCT_PRICE));
            wait.until(ExpectedConditions.visibilityOfElementLocated(ADD_TO_CART_BUTTON));
        } catch (Exception e) {
            throw new RuntimeException("Product details failed to load", e);
        }
    }

    @Step("Get product price as number")
    public double getProductPriceAsNumber() {
        try {
            String priceText = getProductPrice();
            String cleanPrice = priceText.replaceAll("[^\\d.,]", "").replace(",", ".");

            if (cleanPrice.isEmpty()) {
                throw new RuntimeException("Price text is empty");
            }

            return Double.parseDouble(cleanPrice);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse product price as number", e);
        }
    }

    public static class ProductInfo {
        private final String title;
        private final String price;

        public ProductInfo(String title, String price) {
            this.title = title;
            this.price = price;
        }

        public String getTitle() {
            return title;
        }

        public String getPrice() {
            return price;
        }

        @Override
        public String toString() {
            return String.format("ProductInfo{title='%s', price='%s'}", title, price);
        }
    }
}