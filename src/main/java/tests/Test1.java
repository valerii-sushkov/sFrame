package tests;


import org.testng.annotations.Test;
import pages.SearchPage;

public class Test1 extends AbstractBaseTest {

    @Test
    public void simpleTest1() {
        //DriverSession.getDriverSession().get("https://www.google.com/");
        SearchPage sPage = new SearchPage();
        sPage.fillForm("mecha");
        sPage.verifySearchSuccessful();
    }

    @Test
    public void simpleTestFailure() {
        //DriverSession.getDriverSession().get("https://www.google.com/");
        throw new AssertionError("some conditional fail!");
    }

    @Test
    public void simpleTest2() {
        System.out.println("11111");
    }
}
