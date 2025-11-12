import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class UserMenuPage extends BasePage<UserMenuPage>{
    private final By userIcon = By.xpath(
            "//div[@class = 'avatar-wrapper el-tooltip__trigger el-tooltip__trigger']");
    private final By settings = By.linkText("/settings/general");

    public UserMenuPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public UserMenuPage open() {
        return null;
    }

    public UserMenuPage openUserMenu(){
        driver.findElement(userIcon).click();
        return this;
    }

    public GeneralSettingsPage goToSettings(){
        driver.findElement(settings).click();
        return new GeneralSettingsPage(driver);
    }
}
