package Page.Market;

import Page.Base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

public class MarketPage extends BasePage<MarketPage> {
    private static final By TITLE = By.xpath("//h1[@class='o-typography__heading1 market-catalog__title']");
    private static final String URL = "https://qa2.ohi-s.com/";
    private static final By PRODUCT_CARDS = By.cssSelector(".market-product-card");
    private static final By PRODUCT_PRICES = By.cssSelector(".current-price");
    private static final By SORT_PRICE_BUTTON = By.xpath("//*[text()='price']");
    private static final By SELECT_COUNTRY_FRAME = By.cssSelector(".el-dialog");
    private static final By COUNTRY_FIELD = By.cssSelector(".el-select__wrapper");
    private static final By SAVE_LOCATION_BUTTON = By.cssSelector(".el-button.el-button--primary.el-button--large.market-region-popup__button");
    private static final By NUMBER_PRODUCTS = By.cssSelector(".number-products");
    private static final By SPECIAL_OFFERS_TAB = By.xpath("//a[contains(text(), 'Special Offers') or contains(@href, '/offers')]");
    private static final By MY_ORDERS_TAB = By.xpath("//a[contains(text(), 'My orders') or contains(@href, '/orders')]");

    public MarketPage(WebDriver driver) {
        super(driver);
    }

    @Override
    @Step("Open market page")
    public MarketPage open() {
        driver.get(URL);
        return this;
    }

    @Step("Check if market page is opened")
    public boolean isOpened() {
        try {
            WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(TITLE));
            return title.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Check if products are displayed on the page")
    public boolean hasProducts() {
        try {
            List<WebElement> products = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(PRODUCT_CARDS));
            return !products.isEmpty();
        } catch (Exception e) {
            throw new RuntimeException("Failed to check products presence", e);
        }
    }

    @Step("Select filter: {filterName}")
    public MarketPage selectFilter(String filterName) {
        try {
            By filterLocator = By.xpath("//span[@title='" + filterName + "']/ancestor::label");
            WebElement filterElement = wait.until(ExpectedConditions.elementToBeClickable(filterLocator));
            filterElement.click();
            return this;
        } catch (Exception e) {
            throw new RuntimeException("Failed to select filter: " + filterName, e);
        }
    }

    @Step("Get filter count for: {filterName}")
    public int getFilterCount(String filterName) {
        try {
            By countLocator = By.xpath("//span[@title='" + filterName + "']/following-sibling::span[contains(@class, 'filter-wrapper__count')]");
            WebElement countElement = wait.until(ExpectedConditions.visibilityOfElementLocated(countLocator));

            String countText = countElement.getText().trim();
            String numberString = countText.replaceAll("[^0-9]", "");

            if (numberString.isEmpty()) {
                throw new RuntimeException("No numeric value found in filter count: " + countText);
            }

            return Integer.parseInt(numberString);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get filter count for: " + filterName, e);
        }
    }

    @Step("Sort products by price ascending")
    public MarketPage sortByPriceAscending() {
        try {
            WebElement sortButton = wait.until(ExpectedConditions.elementToBeClickable(SORT_PRICE_BUTTON));
            sortButton.click();
            waitForProductsToLoad();
            return this;
        } catch (Exception e) {
            throw new RuntimeException("Failed to sort by price", e);
        }
    }

    @Step("Check if filter is selected: {filterName}")
    public boolean isFilterSelected(String filterName) {
        try {
            By checkboxLocator = By.xpath("//span[@title='" + filterName + "']/ancestor::label//input[@type='checkbox']");
            WebElement checkbox = driver.findElement(checkboxLocator);
            return checkbox.isSelected();
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Get filtered products count")
    public int getFilteredProductsCount() {
        try {
            WebElement numberProductsElement = wait.until(ExpectedConditions.visibilityOfElementLocated(NUMBER_PRODUCTS));
            String text = numberProductsElement.getText().trim();

            if (text.contains("out of")) {
                String[] parts = text.split(" out of ");
                if (parts.length >= 1) {
                    String filteredPart = parts[0].trim();
                    if (filteredPart.matches("\\d+")) {
                        return Integer.parseInt(filteredPart);
                    }
                }
            } else if (text.matches("\\d+")) {
                return Integer.parseInt(text);
            }

            throw new RuntimeException("Could not parse filtered products count from: '" + text + "'");
        } catch (Exception e) {
            throw new RuntimeException("Failed to get filtered products count", e);
        }
    }

    @Step("Check if prices are sorted ascending")
    public boolean isPricesSortedAscending() {
        waitForProductsToLoad();
        List<Double> prices = getProductPricesAsNumbers();

        if (prices.size() < 2) {
            return true;
        }

        for (int i = 0; i < prices.size() - 1; i++) {
            if (prices.get(i) < prices.get(i + 1)) {
                return false;
            }
        }
        return true;
    }

    @Step("Get product prices as numbers")
    public List<Double> getProductPricesAsNumbers() {
        List<Double> prices = new ArrayList<>();
        try {
            List<WebElement> priceElements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(PRODUCT_PRICES));

            for (WebElement priceElement : priceElements) {
                String priceText = priceElement.getText();
                String cleanPrice = priceText.replaceAll("[^\\d.,]", "").replace(",", ".");

                if (cleanPrice.isEmpty()) {
                    continue;
                }

                try {
                    double price = Double.parseDouble(cleanPrice);
                    prices.add(price);
                } catch (NumberFormatException e) {
                    throw new RuntimeException("Could not parse price: " + priceText);
                }
            }
            return prices;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get product prices as numbers", e);
        }
    }

    @Step("Get first product title")
    public String getFirstProductTitle() {
        return getProductTitleByIndex(0);
    }

    @Step("Get first product price")
    public String getFirstProductPrice() {
        return getProductPriceByIndex(0);
    }

    @Step("Click on first product")
    public void clickFirstProduct() {
        clickProductByIndex(0);
    }

    @Step("Select country and save location")
    public void selectCountry() {
        try {
            List<WebElement> countryFrames = driver.findElements(SELECT_COUNTRY_FRAME);

            if (!countryFrames.isEmpty() && countryFrames.get(0).isDisplayed()) {
                WebElement selectCountryFrame = wait.until(ExpectedConditions.visibilityOfElementLocated(SELECT_COUNTRY_FRAME));
                setCountry("Russia");
                clickSaveLocationButton();
                wait.until(ExpectedConditions.invisibilityOfElementLocated(SELECT_COUNTRY_FRAME));
            }
        } catch (Exception e) {
            throw new RuntimeException("Error during country selection");
        }
    }

    @Step("Set country to: {country}")
    private void setCountry(String country) {
        try {
            WebElement countryField = wait.until(ExpectedConditions.elementToBeClickable(COUNTRY_FIELD));
            countryField.click();

            WebElement countryInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector(".el-select__input")
            ));
            countryInput.clear();
            countryInput.sendKeys(country);

            WebElement countryOption = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//li[contains(@class, 'el-select-dropdown__item') and contains(., '" + country + "')]")
            ));
            countryOption.click();
        } catch (Exception e) {
            throw new RuntimeException("Failed to select country: " + country, e);
        }
    }

    @Step("Click save location button")
    private void clickSaveLocationButton() {
        try {
            WebElement saveLocationButton = wait.until(ExpectedConditions.elementToBeClickable(SAVE_LOCATION_BUTTON));
            Assert.assertTrue(saveLocationButton.isDisplayed(), "Save location button is not found");
            saveLocationButton.click();
        } catch (Exception e) {
            throw new RuntimeException("Failed to click save location button", e);
        }
    }

    @Step("Get product title by index: {index}")
    public String getProductTitleByIndex(int index) {
        try {
            List<WebElement> products = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(PRODUCT_CARDS));

            if (index >= 0 && index < products.size()) {
                WebElement product = products.get(index);

                WebElement titleElement = wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(
                        product, By.cssSelector(".product-title")
                ));

                String title = titleElement.getText().trim();
                if (title.isEmpty()) {
                    throw new RuntimeException("Product title is empty");
                }

                return title;
            }
            throw new RuntimeException("Product index out of bounds: " + index);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get product title for index " + index, e);
        }
    }

    @Step("Get product price by index: {index}")
    public String getProductPriceByIndex(int index) {
        try {
            List<WebElement> products = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(PRODUCT_CARDS));

            if (index >= 0 && index < products.size()) {
                WebElement product = products.get(index);

                WebElement priceElement = wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(
                        product, By.cssSelector(".current-price")
                ));

                return priceElement.getText().trim();
            }
            throw new RuntimeException("Product index out of bounds: " + index);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get product price for index " + index, e);
        }
    }

    @Step("Click on product by index: {index}")
    private void clickProductByIndex(int index) {
        try {
            List<WebElement> products = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(PRODUCT_CARDS));

            if (index >= 0 && index < products.size()) {
                WebElement product = products.get(index);

                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", product);
                Thread.sleep(500);

                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].click();", product);

                System.out.println("Clicked product index: " + index);
            } else {
                throw new RuntimeException("Product index out of bounds: " + index);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to click product with index " + index, e);
        }
    }

    @Step("Get total products count")
    public int getTotalProductsCount() {
        try {
            WebElement numberProductsElement = wait.until(ExpectedConditions.visibilityOfElementLocated(NUMBER_PRODUCTS));
            String text = numberProductsElement.getText().trim();

            if (text.contains("out of")) {
                String[] parts = text.split(" out of ");
                if (parts.length >= 2) {
                    String totalPart = parts[1].trim();
                    if (totalPart.matches("\\d+")) {
                        return Integer.parseInt(totalPart);
                    }
                }
            }

            throw new RuntimeException("Could not parse total products count from: '" + text + "'");
        } catch (Exception e) {
            throw new RuntimeException("Failed to get total products count", e);
        }
    }

    @Step("Get product count")
    public int getProductCount() {
        try {
            List<WebElement> products = driver.findElements(PRODUCT_CARDS);
            return products.size();
        } catch (Exception e) {
            return 0;
        }
    }

    @Step("Wait for products to load")
    public void waitForProductsToLoad() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(PRODUCT_CARDS));
            wait.until(ExpectedConditions.visibilityOfElementLocated(PRODUCT_CARDS));
            Thread.sleep(1000);
        } catch (Exception e) {
            throw new RuntimeException("Products failed to load", e);
        }
    }

    @Step("Click on Special Offers tab")
    public MarketPage clickSpecialOffersTab() {
        try {
            WebElement specialOffersTab = wait.until(ExpectedConditions.elementToBeClickable(SPECIAL_OFFERS_TAB));
            specialOffersTab.click();
            return new MarketPage(driver);
        } catch (Exception e) {
            throw new RuntimeException("Failed to click Special Offers tab", e);
        }
    }

    @Step("Click on Special Offers tab")
    public MarketPage clickMyOrdersTab() {
        try {
            WebElement myOrdersTab = wait.until(ExpectedConditions.elementToBeClickable(MY_ORDERS_TAB));
            myOrdersTab.click();
            return new MarketPage(driver);
        } catch (Exception e) {
            throw new RuntimeException("Failed to click My Orders tab", e);
        }
    }
}