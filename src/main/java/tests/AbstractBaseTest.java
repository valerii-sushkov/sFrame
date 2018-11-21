package tests;

import common.PROPERTY;
import drivers.DriverFactory;
import drivers.DriverSession;
import helpers.Config;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractBaseTest {

    public static final Logger LOGGER = Logger.getLogger(AbstractBaseTest.class.getCanonicalName());

    @BeforeSuite
    public void setUp(final ITestContext context) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(">>>Start:" + dtf.format(now));
        LOGGER.warning(">>>Start:" + dtf.format(now));

    }

    @BeforeMethod
    public synchronized void setUpTest(final Method method, final ITestContext context) {
        if (DriverSession.getDriverSession() == null) {
            DriverSession.setDriverSession(DriverFactory.getDriverInstance());
            DriverSession.getDriverSession().get(Config.getProperty(PROPERTY.HOST_URL));
        }
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod(final ITestResult result) {
        if (!result.isSuccess()) {
            LOGGER.log(Level.SEVERE, "Not success!", result.getThrowable());
        }
        DriverSession.closeDriverSession();
    }

    @AfterSuite
    public void closeBrowsers() {
        DriverSession.closeDriverSessions();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(">>>END:" + dtf.format(now));
        LOGGER.warning(">>>END:" + dtf.format(now));
    }


}
