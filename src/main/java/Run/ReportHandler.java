package Run;

import LambdaBox.TestResponse;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ReportHandler {

    public static void writeReport(final Map<String, List<String>> expectedCases,
                                    final Map<String, TestResponse> results) {
        final ExtentReports extent = prepareReporter();
        expectedCases.entrySet().stream()
                .forEach(entry -> {
                    ExtentTest suite = extent.createTest(entry.getKey());
                    entry.getValue().stream().forEach(testName-> {
                        ExtentTest node = suite.createNode(testName);
                        TestResponse res = results.get(entry.getKey() + "." + testName);
                        if (res == null) {
                            node.fail("NO RESULT FOUND");
                        } else {
                            node.log(Status.INFO, "Test id:" + res.getTestId());
                            res.getLogLines().forEach(line ->
                                    node.log(Status.INFO, line));
                            if (res.getScreenShot() != null) {
                                try {
                                    node.log(Status.INFO, "Screen Shot",
                                            MediaEntityBuilder.createScreenCaptureFromBase64String(res.getScreenShot()).build());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (res.getResCode() == 0) {
                                node.pass("PASS");
                            } else {
                                node.fail("FAIL");
                            }
                        }
                    });
                });
        extent.flush();
    }

    private static ExtentReports prepareReporter() {
        ExtentHtmlReporter logger = new ExtentHtmlReporter(System.getProperty("user.dir") +
                "\\test-output\\logger\\report.html");
        logger.config().setAutoCreateRelativePathMedia(true);
        logger.config().setDocumentTitle("Regression Report");
        logger.config().setReportName("Regression Report");
        logger.config().setTheme(Theme.STANDARD);
        ExtentReports  extent = new ExtentReports();
        extent.attachReporter(logger);
        return extent;
    }
}
