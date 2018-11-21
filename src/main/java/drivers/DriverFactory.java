package drivers;

import com.google.common.base.Preconditions;
import common.PROPERTY;
import helpers.Config;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;

public class DriverFactory {

    private DriverFactory() {

    }


    public static WebDriver getDriverInstance() {
        WebDriver driver;
        System.setProperty("org.uncommons.reportng.escape-output", "false");
        switch (PROPERTY.BROWSER.getValue()) {
            case "chrome":
                try {
                    DesiredCapabilities caps = new DesiredCapabilities();
                    caps.setJavascriptEnabled(true);
                    File logfile = new File(PROPERTY.TMP_FOLDER.getValue() + "logfile.log");
                    File execFile = new File(PROPERTY.RESOURCE_FOLDER.getValue() + "phantomjs.exe");
                    String[] phantomArgs = new String[] {
                            "--webdriver-loglevel=NONE"
                    };
                    System.out.println("0>" + execFile.exists() + " " + execFile.canExecute() + " " + !execFile.isDirectory());
                    Preconditions.checkState(true, "The driver executable does not exist: %s", execFile.getAbsolutePath());
                    PhantomJSDriverService pjsds = new PhantomJSDriverService.Builder()
                            .usingPhantomJSExecutable(execFile)
                            .usingAnyFreePort()
                            .withProxy(null)
                            .usingCommandLineArguments(phantomArgs)
                            .withLogFile(logfile)
                            .build();
                    driver = new PhantomJSDriver(pjsds, caps);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("Error with Browser", e);
                }
                break;
            case "phantom":
                try {
                    DesiredCapabilities caps = new DesiredCapabilities();
                    caps.setJavascriptEnabled(true);
                    caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
                            PROPERTY.RESOURCE_FOLDER.getValue() + "phantomjs");
                    File logfile = new File(PROPERTY.TMP_FOLDER.getValue() + "logfile.log");
                    File execFile = new File(PROPERTY.RESOURCE_FOLDER.getValue() + "phantomjs");
                    String[] phantomArgs = new String[] {
                            "--webdriver-loglevel=NONE"
                    };
                    PhantomJSDriverService pjsds = new PhantomJSDriverService.Builder()
                            .usingPhantomJSExecutable(execFile)
                            .usingAnyFreePort()
                            .withProxy(null)
                            .usingCommandLineArguments(phantomArgs)
                            .withLogFile(logfile)
                            .build();
                    driver = new PhantomJSDriver(pjsds, caps);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("Error with Browser", e);
                }
                break;
            default:
                throw new RuntimeException("Incorrect Browser type - " + PROPERTY.BROWSER.getValue());
        }
        return driver;
    }


}
