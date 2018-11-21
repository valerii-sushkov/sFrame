package helpers;

import common.PROPERTY;
import drivers.DriverSession;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.logging.Logger;

public class WaitUtils {
    private static final Logger LOGGER = Logger.getLogger(WaitUtils.class.getCanonicalName());

    private WaitUtils() {

    }

    public static void waitForElement(final WebElement element, final String message) {
        WebDriver driver = DriverSession.getDriverSession();
        WebDriverWait wait = new WebDriverWait(driver, Long.valueOf(Config.getProperty(PROPERTY.TIMEOUT)));
        wait.withMessage(message + "/n" + driver.getPageSource()).until((WebDriver drv) -> isElementPresent(element));
    }

    public static void waitForElement(final By element, final String message) {
        WebDriver driver = DriverSession.getDriverSession();
        WebDriverWait wait = new WebDriverWait(driver, Long.valueOf(Config.getProperty(PROPERTY.TIMEOUT)));
        wait.withMessage(message).until((WebDriver drv) -> isElementPresent(element));
    }

    public static boolean isElementPresent(final By by) {
        WebDriver driver = DriverSession.getDriverSession();
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
        List<WebElement> elements = driver.findElements(by);
        boolean exists = elements.stream().anyMatch(elem -> elem.isDisplayed());
        driver.manage().timeouts().implicitlyWait(Long.valueOf(Config.getProperty(PROPERTY.TIMEOUT)), TimeUnit.SECONDS);
        return exists;
    }

    public static boolean isElementPresent(final WebElement element) {
        boolean res = false;
        try {
            WebDriver driver = DriverSession.getDriverSession();
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
            res = element.isDisplayed();
            driver.manage().timeouts()
                    .implicitlyWait(Long.valueOf(Config.getProperty(PROPERTY.TIMEOUT)), TimeUnit.SECONDS);
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            LOGGER.config("Element not found.");
            if (e instanceof StaleElementReferenceException) {
                LOGGER.warning("Stale element during wait.");
            }
        }
        return res;
    }

    public static boolean isElementCurrentlyPresent(final WebElement element) {
        boolean res = false;
        try {
            WebDriver driver = DriverSession.getDriverSession();
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
            res = element.isDisplayed();
            driver.manage().timeouts()
                    .implicitlyWait(Long.valueOf(Config.getProperty(PROPERTY.TIMEOUT)), TimeUnit.SECONDS);
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            LOGGER.config("Element not found.");
            if (e instanceof StaleElementReferenceException) {
                LOGGER.warning("Stale element during wait.");
            }
        }
        return res;
    }

    public static void waitForChange(final WebElement element, final Predicate<WebElement> change,
                                     final String message) {
        WebDriver driver = DriverSession.getDriverSession();
        WebDriverWait wait = new WebDriverWait(driver, Long.valueOf(Config.getProperty(PROPERTY.TIMEOUT)));
        wait.withMessage(message).until((WebDriver drv) -> change.test(element));
    }

    public static void waitForChange(final Predicate<WebDriver> change,
                                     final String message) {
        WebDriver driver = DriverSession.getDriverSession();
        WebDriverWait wait = new WebDriverWait(driver, Long.valueOf(Config.getProperty(PROPERTY.TIMEOUT)));
        wait.withMessage(message).until((WebDriver drv) -> change.test(driver));
    }

    public static void waitForSpinnerStop() {
        waitForSpinnerStopLong(2);
    }

    public static void waitForSpinnerStopLong(final int waitMultiplier) {
        By spinner = By.xpath("//div[@id='base-loader' or @id='load_order-list' or @id='load_price-grid']");
        WebDriverWait wait = new WebDriverWait(DriverSession.getDriverSession(),
                Long.valueOf(Config.getProperty(PROPERTY.TIMEOUT)) * waitMultiplier);
        wait.withMessage("Spinner is not disappeared!")
                .until((WebDriver drv) -> {
                    try {
                        WebElement spin = drv.findElement(spinner);
                        boolean res = spin == null ? false : !spin.isDisplayed();
                        return res;
                    } catch (NoSuchElementException | StaleElementReferenceException | UnhandledAlertException e) {
                        LOGGER.config("Element not found.");
                        return true;
                    }
                });
    }

    public static void uglyDelay(final int sec) {
        try {
            Thread.sleep(1000 * sec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void checkAlert(final String message) {
            WebDriver driver = DriverSession.getDriverSession();
            WebDriverWait wait = new WebDriverWait(driver, Long.valueOf(Config.getProperty(PROPERTY.TIMEOUT)));
            wait.withMessage(message).until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            alert.accept();
    }
}
