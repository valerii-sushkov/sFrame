package Run;

import LambdaBox.TestRequest;
import LambdaBox.TestResponse;
import common.LogHandler;
import common.TestResultListener;

import helpers.Config;
import org.apache.commons.lang3.StringUtils;
import org.testng.Reporter;
import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlInclude;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;


public class LambdaRun {
    private static final Logger LOGGER = Logger.getLogger(LambdaRun.class.getCanonicalName());
    public static void main(final String[] args) {
        TestResponse res = runSingleTest(dummyConf());
        System.out.println(res);
    }

    public static TestResponse runSingleTest(final TestRequest request) {
        Config.readPropertiesFromRequest(request);
        Logger logger = Logger.getLogger(StringUtils.EMPTY);
        logger.addHandler(new LogHandler());
        TestNG testNG = new TestNG(false);
        testNG.setVerbose(TestNG.DEFAULT_VERBOSE);
        Reporter.setEscapeHtml(false);
        testNG.setListenerClasses(Arrays.asList(org.uncommons.reportng.HTMLReporter.class,
                org.uncommons.reportng.JUnitXMLReporter.class, TestResultListener.class));

        List<XmlSuite> suites = new ArrayList<>();
        XmlSuite suite = new XmlSuite();
        suite.setName("My Suite");

        XmlTest test = new XmlTest(suite);
        test.setName("My Test 1111");

        List<XmlClass> classes = new ArrayList<>();
        XmlClass xmlClass = new XmlClass("tests.Test1");
        List<XmlInclude> includeMethods = new ArrayList<>();
        includeMethods.add(new XmlInclude(PROPERTY.TEST_TO_RUN.getValue()));
        xmlClass.setIncludedMethods(includeMethods);
        classes.add(xmlClass);

        test.setXmlClasses(classes);
        suites.add(suite);

        testNG.setXmlSuites(suites);
        testNG.run();

        return new TestResponse(Integer.valueOf(PROPERTY.TEST_ID.getValue()), PROPERTY.TEST_TO_RUN.getValue(),
                LogCollector.getResult(), LogCollector.extractLog(), LogCollector.getScreenShot());
    }



    public static TestRequest dummyConf() {
        TestRequest req = new TestRequest(1, "tests.Test1", "simpleTest1");
        req.setBrowser("chrome");
        req.setHost("https://www.google.com/");
        req.setResFolder("src\\main\\resources\\local\\");
        req.setTmpFolder("tmp\\");
        return req;
    }

}
