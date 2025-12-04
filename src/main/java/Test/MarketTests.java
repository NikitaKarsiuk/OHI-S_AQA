package Test;

import Page.Auth.LoginPage;
import Page.Cart.CartPage;
import Page.Homepage.FeedPage;
import Page.Homepage.HomePage;
import Page.Market.MarketPage;
import Page.Market.MyOrders.MyOrders;
import Page.Market.MyOrders.OrderDetail;
import Page.Market.Product.ProductDetailPage;
import Page.Market.SpecialOffers.SpecialOffers;
import io.qameta.allure.Description;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

public class MarketTests extends BaseTest {

    private MarketPage marketPage;
    private HomePage homePage;
    private ProductDetailPage productDetailPage;
    private CartPage cartPage;
    private SpecialOffers specialOffers;
    private LoginPage loginPage;
    private FeedPage feedPage;
    private MyOrders myOrders;
    private OrderDetail orderDetail;
    @BeforeMethod
    public void initPages() {
        marketPage = new MarketPage(driver);
        homePage = new HomePage(driver);
        productDetailPage = new ProductDetailPage(driver);
        cartPage = new CartPage(driver);
        specialOffers = new SpecialOffers(driver);
        loginPage = new LoginPage(driver);
        feedPage = new FeedPage(driver);
        myOrders = new MyOrders(driver);
        orderDetail = new OrderDetail(driver);
    }

    @Test
    @Story("Market page check")
    @Description("The test checks whether the market page opens")
    public void testOpenMarketPage() {
        homePage.open()
                .acceptCookies();
        Assert.assertTrue(homePage.isOpened(), "The page didn't load");
        homePage.clickElementInHeaderMenu("Market");
        Assert.assertTrue(marketPage.isOpened(), "The page didn't load");
    }

    @Test
    @Story("Market page has products check")
    @Description("The test checks whether the market page has products")
    public void testProductCheck() {
        homePage.open()
                .acceptCookies();
        Assert.assertTrue(homePage.isOpened(), "The page didn't load");
        homePage.clickElementInHeaderMenu("Market");
        Assert.assertTrue(marketPage.isOpened(), "The page didn't load");
        marketPage.selectCountry();
        Assert.assertTrue(marketPage.hasProducts(), "No products found on the market page");
    }
    @Test
    @Story("Detailed filter validation")
    @Description("Test validates filter functionality with multiple checks")
    public void testFilterValidation() throws InterruptedException {
        homePage.open()
                .acceptCookies();
        Assert.assertTrue(homePage.isOpened(), "The page didn't load");
        homePage.clickElementInHeaderMenu("Market");
        Assert.assertTrue(marketPage.isOpened(), "The page didn't load");
        marketPage.selectCountry();
        Assert.assertTrue(marketPage.hasProducts(), "No products found on the market page");

        int initialTotalCount = marketPage.getTotalProductsCount();
        int eurofileFilterCount = marketPage.getFilterCount("Eurofile");

        marketPage.selectFilter("Eurofile");
        Assert.assertTrue(marketPage.isFilterSelected("Eurofile"),
                  "Eurofile filter is not selected");
        marketPage.waitForProductsToLoad();

        int totalCountAfter = marketPage.getTotalProductsCount();

        Assert.assertTrue(totalCountAfter <= initialTotalCount,
                "Filtered products count doesn't match filter count");
        Assert.assertEquals(totalCountAfter, eurofileFilterCount,
                "Total products count should remain the same");
    }

    @Test
    @Story("Market page price sorting")
    @Description("The test checks if price sorting works correctly")
    public void testPriceSorting()  {
        homePage.open()
                .acceptCookies();
        homePage.clickElementInHeaderMenu("Market");
        Assert.assertTrue(marketPage.isOpened(), "Market page didn't load");
        marketPage.selectCountry();
        Assert.assertTrue(marketPage.hasProducts(), "No products found on the market page");
        marketPage.sortByPriceAscending();
        Assert.assertTrue(marketPage.isPricesSortedAscending(),
                "Prices are not sorted in ascending order");
    }

    @Test
    @Story("Product details consistency")
    @Description("The test checks if price and title match when clicking on the first product")
    public void testFirstProductDetailsConsistency() {
        homePage.open()
                .acceptCookies();
        homePage.clickElementInHeaderMenu("Market");
        Assert.assertTrue(marketPage.isOpened(), "Market page didn't load");
        marketPage.selectCountry();
        Assert.assertTrue(marketPage.hasProducts(), "No products found on the market page");

       String expectedTitle = marketPage.getFirstProductTitle();
       String expectedPrice = marketPage.getFirstProductPrice();

        marketPage.clickFirstProduct();
        Assert.assertTrue(productDetailPage.isOpened(), "Product detail page didn't load");

        Assert.assertEquals(productDetailPage.getProductTitle(), expectedTitle,
                "Product title doesn't match between market page and detail page");
        Assert.assertEquals(productDetailPage.getProductPrice(), expectedPrice,
               "Product price doesn't match between market page and detail page");
    }

    @Test
    @Story("Add to cart first product")
    @Description("The test checks if we add to cart our first product")
    public void testAddToCartFirstProduct()  {
        homePage.open()
                .acceptCookies();
        homePage.clickElementInHeaderMenu("Market");
        Assert.assertTrue(marketPage.isOpened(), "Market page didn't load");
        marketPage.selectCountry();
        Assert.assertTrue(marketPage.hasProducts(), "No products found on the market page");

        String expectedTitle = marketPage.getFirstProductTitle();
        String expectedPrice = marketPage.getFirstProductPrice();

        marketPage.clickFirstProduct();
        Assert.assertTrue(productDetailPage.isOpened(), "Product detail page didn't load");

        Assert.assertEquals(productDetailPage.getProductTitle(), expectedTitle,
                "Product title doesn't match between market page and detail page");
        Assert.assertEquals(productDetailPage.getProductPrice(), expectedPrice,
                "Product price doesn't match between market page and detail page");
        productDetailPage.addToCart();
        productDetailPage.goToCart();
        Assert.assertTrue(cartPage.isOpened(), "Cart page didn't load");
        Assert.assertTrue(cartPage.isItemAddedInCart(expectedTitle), "The cart doesn't contain the item");
    }

    @Test
    @Story("Add to cart first product")
    @Description("The test checks if we add to cart our first product twice")
    public void testAddToCartFirstProductTwice()  {
        homePage.open()
                .acceptCookies();
        homePage.clickElementInHeaderMenu("Market");
        Assert.assertTrue(marketPage.isOpened(), "Market page didn't load");
        marketPage.selectCountry();
        Assert.assertTrue(marketPage.hasProducts(), "No products found on the market page");

        String expectedTitle = marketPage.getFirstProductTitle();
        String expectedPrice = marketPage.getFirstProductPrice();

        marketPage.clickFirstProduct();
        Assert.assertTrue(productDetailPage.isOpened(), "Product detail page didn't load");

        Assert.assertEquals(productDetailPage.getProductTitle(), expectedTitle,
                "Product title doesn't match between market page and detail page");
        Assert.assertEquals(productDetailPage.getProductPrice(), expectedPrice,
                "Product price doesn't match between market page and detail page");
        productDetailPage.AddProduct();

        int amountOfProduct = productDetailPage.getAmountOfProduct();
        productDetailPage.addToCart();
        productDetailPage.goToCart();

        Assert.assertTrue(cartPage.isOpened(), "Cart page didn't load");
        Assert.assertEquals(amountOfProduct, cartPage.getItemQuantity(expectedTitle), "Quantity of the product corresponds to the basket");
    }

    @Test
    @Story("Open special offers page")
    @Description("The test checks if we open a special offer page")
    public void testOpenSpecialOffersPage()  {
        homePage.open()
                .acceptCookies();
        homePage.clickElementInHeaderMenu("Market");
        Assert.assertTrue(marketPage.isOpened(), "Market page didn't load");
        marketPage.selectCountry();
        Assert.assertTrue(marketPage.hasProducts(), "No products found on the market page");
        marketPage.clickSpecialOffersTab();
        Assert.assertTrue(specialOffers.isOpened(), "Special offers page is not opened");;
    }

    @Test
    @Story("Open my orders page")
    @Description("The test checks if we have order list on page")
    public void testOpenMyOrdersPage()  {
        homePage.open()
                .acceptCookies();
        homePage.clickLogInButton();
        loginPage.isOpened();
        loginPage.login("jorobawi@gmail.com", "aoADaSZoly");
        Assert.assertTrue(feedPage.hasLoaded(), "Feed page is not opened");
        feedPage.clickElementInHeaderMenu("Market");
        Assert.assertTrue(marketPage.isOpened(), "Market page didn't load");
        marketPage.selectCountry();
        marketPage.clickMyOrdersTab();
        Assert.assertTrue(myOrders.isOpened(), "My Orders page is not opened");;
        Assert.assertTrue(myOrders.isOrdersListDisplayed(), "Orders list is not displayed");
    }

    @Test
    @Story("Test search field on my orders page")
    @Description("The test checks whether the search works")
    public void testSearchOrdersPage() {
        String searchQuery = "ohis";

        homePage.open()
                .acceptCookies();
        homePage.clickLogInButton();
        loginPage.isOpened();
        loginPage.login("jorobawi@gmail.com", "aoADaSZoly");
        Assert.assertTrue(feedPage.hasLoaded(), "Feed page is not opened");
        feedPage.clickElementInHeaderMenu("Market");
        Assert.assertTrue(marketPage.isOpened(), "Market page didn't load");
        marketPage.selectCountry();
        marketPage.clickMyOrdersTab();
        Assert.assertTrue(myOrders.isOpened(), "My Orders page is not opened");;
        Assert.assertTrue(myOrders.isOrdersListDisplayed(), "Orders list is not displayed");
        myOrders.enterSearchQuery(searchQuery);
        myOrders.waitForSearchResults(searchQuery);
        int resultsCount = myOrders.getSearchResultsCount();
        Assert.assertTrue(resultsCount > 0,
                "Should find at least 1 order for search: " + searchQuery);
        doesOrderContainResult(resultsCount, searchQuery);
    }

    @Test
    @Story("Test filter on my orders page")
    @Description("The test checks whether the filter works")
    public void testFilterOrdersPage() {
        String searchQuery = "Canceled by dealer";

        homePage.open()
                .acceptCookies();
        homePage.clickLogInButton();
        loginPage.isOpened();
        loginPage.login("jorobawi@gmail.com", "aoADaSZoly");
        Assert.assertTrue(feedPage.hasLoaded(), "Feed page is not opened");
        feedPage.clickElementInHeaderMenu("Market");
        Assert.assertTrue(marketPage.isOpened(), "Market page didn't load");
        marketPage.selectCountry();
        marketPage.clickMyOrdersTab();
        Assert.assertTrue(myOrders.isOpened(), "My Orders page is not opened");;
        Assert.assertTrue(myOrders.isOrdersListDisplayed(), "Orders list is not displayed");
        myOrders.clickFilterButton();
        myOrders.selectFilter(searchQuery);
        myOrders.clickCloseFilterFormButton();
        myOrders.waitForSearchResults(searchQuery);
        int resultsCount = myOrders.getSearchResultsCount();
        Assert.assertTrue(resultsCount > 0,
               "Should find at least 1 order for search: " + searchQuery);
        doesOrderContainResult(resultsCount, searchQuery);
    }

    @Test
    @Story("Test filter on my orders page")
    @Description("The test checks whether the filter works")
    public void testOpenOrderDetailsPage() {
        String searchQuery = "Canceled by dealer";

        homePage.open()
                .acceptCookies();
        homePage.clickLogInButton();
        loginPage.isOpened();
        loginPage.login("jorobawi@gmail.com", "aoADaSZoly");
        Assert.assertTrue(feedPage.hasLoaded(), "Feed page is not opened");
        feedPage.clickElementInHeaderMenu("Market");
        Assert.assertTrue(marketPage.isOpened(), "Market page didn't load");
        marketPage.selectCountry();
        marketPage.clickMyOrdersTab();
        Assert.assertTrue(myOrders.isOpened(), "My Orders page is not opened");;
        Assert.assertTrue(myOrders.isOrdersListDisplayed(), "Orders list is not displayed");
        String orderNumber = myOrders.getFirstOrderNumber();
        myOrders.clickFirstOrder();
        Assert.assertTrue(orderDetail.isOpened(), "Detail page is not opened");
        Assert.assertEquals(orderNumber, orderDetail.getOrderNumberFromHeader(), "The order on the details page does not match the orders page");
    }

    private void doesOrderContainResult(int resultsCount, String searchQuery){
        for (int i = 0; i < resultsCount; i++) {
            Assert.assertTrue(
                    myOrders.doesOrderContainText(i, searchQuery),
                    "Order #" + (i + 1) + " should contain: " + searchQuery
            );
        }
    }
}
