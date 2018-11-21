package common;

import dto.TestResponse;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


public class Run {
    private static final Logger LOGGER = Logger.getLogger(Run.class.getCanonicalName());
    public static void main(final String[] args) {
//        String res = runSingleTest(dummyConf());
//        System.out.println(res);
        new RemoteRun().runAllRemote();
    }

    public static String runSingleTest(final Map<String, String> config) {
        Config.readPropertiesFromMap(config);
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
                LogHandler2.getResult(), LogHandler2.extractLog(), LogHandler2.getScreenShot())
                .toString();
    }



    public static Map<String, String> dummyConf() {
        Map<String, String> conf = new HashMap<>();
        conf.put("browser", "chrome");
        conf.put("host", "https://www.google.com/");
        conf.put("timeout", "5");
        conf.put("res.folder", "src\\main\\resources\\local\\");
        conf.put("tmp.folder", "tmp\\");
        conf.put("test.suite", "tests.Test1");
        conf.put("test.name", "simpleTest1");
        conf.put("test.id", "1");
        return conf;
    }

}
