package common;

import Run.LogCollector;
import drivers.DriverSession;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;
import java.util.logging.Logger;


public class TestResultListener extends TestListenerAdapter implements ITestListener {

    public static final Logger LOGGER = Logger.getLogger(TestResultListener.class.getCanonicalName());


    @Override
    public void onTestFailure(final ITestResult iTestResult) {
        LOGGER.severe("Last URL:" + DriverSession.getDriverSession().getCurrentUrl());
        LogCollector.setResult(1);
        LogCollector.consumeLog(Reporter.getOutput(iTestResult));
        takeHeadlessScreenShot();

    }

    @Override
    public void onTestSuccess(final ITestResult iTestResult) {
        LogCollector.setResult(0);
        LogCollector.consumeLog(Reporter.getOutput(iTestResult));
    }

    @Override
    public void onFinish(final ITestContext context) {
    }

    private static void takeHeadlessScreenShot() {
        String image64 = ((TakesScreenshot) DriverSession.getDriverSession()).getScreenshotAs(OutputType.BASE64);
        Reporter.log("<img style=\"width:100%\" src=\"data:image/jpeg;base64," +
                image64 + "\"/>");
        LogCollector.setScreenShot(image64);
    }


}
