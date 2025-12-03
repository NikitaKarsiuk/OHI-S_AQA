package Page.Market.MyOrders;

import Page.Base.BasePage;
import Page.Market.SpecialOffers.SpecialOffers;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class MyOrders extends BasePage<MyOrders> {
    private static final String URL_PATTERN = "/market/my-orders";
    private static final By SPECIAL_OFFERS_TITLE =
            By.xpath("//h1[@class='o-typography__heading1 my-orders__title']");
    private static final By ORDERS_LIST =
            By.cssSelector(".my-orders-list");
    private static final By LOADING_INDICATOR =
            By.cssSelector(".el-loading-mask, .spinner, [class*='loading']");
    private static final By SEARCH_INPUT =
            By.xpath("//div[@class='o-container o-section__container']//*[@class='el-input__inner']");
    private static final By ORDER_ROWS =
            By.cssSelector(".my-orders-list__row:not(.my-orders-list__row-header)");
    private static final By FILTER_BUTTON =
            By.xpath("//*[@class='el-button el-button--primary my-orders__filter-button']");
    private static final By FILTER_CLOSE_FORM_BUTTON =
            By.xpath("//div[@class='filter__content']//button[@class='el-button el-button--primary filters-panel__reset']");
    private static final By FIRST_ORDER_NUMBER =
            By.xpath("//div[contains(@class, 'my-orders-list__row') and not(contains(@class, 'my-orders-list__row-header'))][1]//p[@class='o-typography__paragraph-14-400 my-orders-list__cell-order']");

    public MyOrders(WebDriver driver) {
        super(driver);
    }

    @Override
    @Step("Open special offers for product  page")
    public MyOrders open() {
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

    @Step("Check if orders list is displayed")
    public boolean isOrdersListDisplayed() {
        try {
            WebElement ordersList = wait.until(ExpectedConditions.visibilityOfElementLocated(ORDERS_LIST));
            return ordersList.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Step("Enter search query: {query}")
    public MyOrders enterSearchQuery(String query) {
        try {
            WebElement searchInput = wait.until(ExpectedConditions.elementToBeClickable(SEARCH_INPUT));
            searchInput.sendKeys(query);
            return this;
        } catch (Exception e) {
            throw new RuntimeException("Failed to enter search query: " + query, e);
        }
    }

    @Step("Wait for search results")
    public void waitForSearchResults(String searchQuery) {
        try {
            WebDriverWait filterWait = new WebDriverWait(driver, Duration.ofSeconds(15));

            filterWait.until(driver -> {
                List<WebElement> rows = driver.findElements(ORDER_ROWS);
                for (WebElement row : rows) {
                    String rowText = row.getText().toLowerCase();
                    if (!rowText.contains(searchQuery.toLowerCase())) {
                        return false;
                    }
                }
                return true;
            });
        } catch (Exception e) {
        }
    }

    @Step("Check if order contains text: {searchText}")
    public boolean doesOrderContainText(int index, String searchText) {
        try {
            List<WebElement> orderRows = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(ORDER_ROWS));

            if (index >= 0 && index < orderRows.size()) {
                WebElement orderRow = orderRows.get(index);
                String rowText = orderRow.getText().toLowerCase();
                return rowText.contains(searchText.toLowerCase());
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Failed to check if order contains text: " + searchText, e);
        }
    }

    @Step("Get search results count")
    public int getSearchResultsCount() {
        try {
            List<WebElement> orderRows = driver.findElements(ORDER_ROWS);
            return orderRows.size();
        } catch (Exception e) {
            return 0;
        }
    }

    @Step("Click filter button")
    public void clickFilterButton() {
        try {
            WebElement filterButton = wait.until(ExpectedConditions.elementToBeClickable(FILTER_BUTTON));
            filterButton.click();
        } catch (Exception e) {
            throw e;
        }
    }

    @Step("Close filter form ")
    public void clickCloseFilterFormButton() {
        try {
            WebElement filterCloseButton = wait.until(ExpectedConditions.elementToBeClickable(FILTER_CLOSE_FORM_BUTTON));
            filterCloseButton.click();
        } catch (Exception e) {
            throw e;
        }
    }

    @Step("Select filter by name: {filterName}")
    public MyOrders selectFilter(String filterName) {
        try {
            By filterLocator = By.xpath(
                    "//span[@data-test='filter__checkbox-title' and text()='" + filterName + "']" +
                            "/ancestor::label[contains(@class, 'filter-checkbox-item')]"
            );

            WebElement filterElement = wait.until(ExpectedConditions.elementToBeClickable(filterLocator));

            filterElement.click();
            return this;
        } catch (Exception e) {
            throw new RuntimeException("Failed to select filter: " + filterName, e);
        }
    }

    @Step("Get first order number from list")
    public String getFirstOrderNumber() {
        try {
            WebElement orderNumberElement = wait.until(ExpectedConditions.visibilityOfElementLocated(FIRST_ORDER_NUMBER));
            return orderNumberElement.getText().trim();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get first order number", e);
        }
    }

    @Step("Click on first order")
    public void clickFirstOrder() {
        try {
            WebElement firstOrderElement = wait.until(ExpectedConditions.elementToBeClickable(FIRST_ORDER_NUMBER));
            firstOrderElement.click();
        } catch (Exception e) {
            throw new RuntimeException("Failed to click on first order", e);
        }
    }
}

