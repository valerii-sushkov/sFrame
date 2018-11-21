package pages;

import drivers.DriverSession;
import helpers.WaitUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public abstract class AbstractBasePage {

    @FindBy(xpath = "//div[@class='tooltipster-content']")
    private List<WebElement> tooltips;

    @FindBy(xpath = "//div[@class = 'alert alert-danger alert-dismissible']")
    private WebElement commonError;

    private WebDriver driver;

    public WebDriver getDriver() {
        return driver;
    }

    public AbstractBasePage() {
        driver = DriverSession.getDriverSession();
        PageFactory.initElements(driver, this);
        waitPageLoading();
    }

    public AbstractBasePage(final boolean waitToLoad) {
        driver = DriverSession.getDriverSession();
        PageFactory.initElements(driver, this);
        if (waitToLoad) {
            waitPageLoading();
        }
    }

    protected abstract void waitPageLoading();


    public void verifyNoErrors() {
        if (WaitUtils.isElementPresent(commonError)) {
            throw new RuntimeException("Error detected on page:" + commonError.getText());
        }
    }

}
