package dto;

public class TestRequest {
    private Integer testId;
    private String testCaseClass;
    private String testCaseName;

    public TestRequest(Integer testId, String testCaseClass, String testCaseName) {
        this.testId = testId;
        this.testCaseClass = testCaseClass;
        this.testCaseName = testCaseName;
    }

    public TestRequest() {

    }



    @Override
    public String toString() {
        return "{" +
                "\"testId\":\"" + testId + "\"," +
                "\"testCaseClass\":\"" + testCaseClass + "\"," +
                "\"testCaseName\":" + testCaseName +"\"" +
                "}";
    }

    public Integer getTestId() {
        return testId;
    }

    public void setTestId(Integer testId) {
        this.testId = testId;
    }

    public String getTestCaseClass() {
        return testCaseClass;
    }

    public void setTestCaseClass(String testCaseClass) {
        this.testCaseClass = testCaseClass;
    }

    public String getTestCaseName() {
        return testCaseName;
    }

    public void setTestCaseName(String testCaseName) {
        this.testCaseName = testCaseName;
    }
}
