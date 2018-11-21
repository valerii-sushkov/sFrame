package drivers;

import org.openqa.selenium.WebDriver;

import java.util.Hashtable;
import java.util.Map;

import static java.lang.Thread.currentThread;

public class DriverSession {
    private static Map<Long, WebDriver> sessions = new Hashtable<>();

    public static WebDriver getDriverSession() {
        return sessions.get(currentThread().getId());
    }

    public static void setDriverSession(final WebDriver driver) {
        sessions.put(currentThread().getId(), driver);
    }

    private DriverSession() {

    }

    public static void closeDriverSessions() {
        sessions.values().forEach(drv -> {
                    drv.close();
                    drv.quit();
                });

    }

    public static void closeDriverSession() {
        WebDriver driver = getDriverSession();
        if (driver != null) {
            driver.close();
            driver.quit();
        }
        sessions.remove(currentThread().getId());

    }
}