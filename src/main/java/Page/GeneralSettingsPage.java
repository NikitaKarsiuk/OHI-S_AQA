package Page;

import Page.Base.BasePage;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;

@Slf4j
public class GeneralSettingsPage extends BasePage<GeneralSettingsPage> {

    public enum Field {
        PHONE(1),
        LANGUAGE(2),
        CURRENCY(3);
        private final int index;
        Field(int index) { this.index = index; }
        public int getIndex() { return index; }
    }

    private static final String PAGE_URL = "https://qa2.ohi-s.com/settings/general/";

    private static final String EDIT_BUTTON_XPATH = "(//div[@class='settings-item__header']/button)[%d]";
    private static final By SAVE_BUTTON_XPATH     = By.xpath(
            "(//div[@class='settings-item__actions']/button)[1]");
    private static final By CANCEL_BUTTON_XPATH   = By.xpath(
            "(//div[@class='settings-item__actions']/button)[2]");

    private static final By PHONE_INPUT_XPATH   = By.xpath("//input[@type='tel']");
    private static final By PHONE_NUMBER_XPATH  = By.xpath
            ("(//span[contains(@class,'settings-item__text')])[1]");

    private static final String LANGUAGE_PATTERN
            = "//li[contains(@class,'el-select-dropdown__item')]/span[normalize-space(text())='%s']";
    private static final By LANGUAGE_XPATH = By.xpath(
            "(//span[contains(@class,'settings-item__text')])[2]");

    private static final String CURRENCY_PATTERN = "//li[@role='option']/span[normalize-space(text())='%s']";
    private static final By CURRENCY_XPATH       = By.xpath(
            "(//span[contains(@class,'settings-item__text')])[3]");

    private static final By SUCCESS_CHANGE_ALERT =
            By.cssSelector(".el-message.el-message--success.is-closable");

    public GeneralSettingsPage(WebDriver driver) {
        super(driver);
    }

    @Step("Open General Settings page")
    @Override
    public GeneralSettingsPage open() {
        log.info("Opening settings page: {}", PAGE_URL);
        driver.get(PAGE_URL);
        waitVisible(PHONE_NUMBER_XPATH);
        return this;
    }

    @Step("Click 'Edit' for field: {field}")
    public GeneralSettingsPage editField(Field field) {
        By btn = By.xpath(String.format(EDIT_BUTTON_XPATH, field.getIndex()));
        safeClick(btn);
        return this;
    }

    @Step("Click 'Save'")
    public GeneralSettingsPage save() {
        safeClick(SAVE_BUTTON_XPATH);

        try { waitVisible(SUCCESS_CHANGE_ALERT); } catch (TimeoutException ignored) {}

        try { waitGone(SAVE_BUTTON_XPATH); } catch (TimeoutException e) {
            log.warn("Save button still visible after click. Possibly save failed.");
        }
        return this;
    }

    @Step("Click 'Cancel' for field: {field}")
    public GeneralSettingsPage cancel(Field field) {
        try {
            safeClick(CANCEL_BUTTON_XPATH);
            waitGone(CANCEL_BUTTON_XPATH);
        } catch (NoSuchElementException e) {
            log.warn("Cancel button not present. Field: {}", field);
        }
        return this;
    }

    @Step("Set phone number to: {phone}")
    public GeneralSettingsPage setPhone(String phone) {
        WebElement input = waitVisible(PHONE_INPUT_XPATH);
        clearAndType(input, phone);
        return this;
    }

    @Step("Get current phone")
    public String getPhone() {
        try {
            return waitVisible(PHONE_NUMBER_XPATH).getText().trim();
        } catch (TimeoutException | NoSuchElementException e) {
            return waitVisible(PHONE_INPUT_XPATH).getAttribute("value").trim();
        }
    }

    @Step("Select language: {language}")
    public GeneralSettingsPage selectLanguage(String language) {
        By opt = By.xpath(String.format(LANGUAGE_PATTERN, language));
        safeClick(opt);
        return this;
    }

    @Step("Get selected language")
    public String getLanguage() {
        try {
            return waitVisible(LANGUAGE_XPATH).getText().trim();
        } catch (Exception e) {
            log.warn("Failed to read language: {}", e.getMessage());
            return "";
        }
    }

    @Step("Select currency: {currency}")
    public GeneralSettingsPage selectCurrency(String currency) {
        By opt = By.xpath(String.format(CURRENCY_PATTERN, currency));
        safeClick(opt);
        return this;
    }

    @Step("Get selected currency")
    public String getCurrency() {
        try {
            return waitVisible(CURRENCY_XPATH).getText().trim();
        } catch (Exception e) {
            log.warn("Failed to read currency: {}", e.getMessage());
            return "";
        }
    }
}
