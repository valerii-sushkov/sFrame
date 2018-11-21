package dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class TestResponse {
    private Integer testId;
    private String testCaseName;
    private int resCode;
    private List<String> logLines;
    private String screenShot;

    public TestResponse() {
    }

    public TestResponse(Integer testId, String testCaseName, int resCode, List<String> logLines, String screenShot) {
        this.testId = testId;
        this.testCaseName = testCaseName;
        this.resCode = resCode;
        this.logLines = logLines;
        this.screenShot = screenShot;
    }

    public Integer getTestId() {
        return testId;
    }

    public String getTestCaseName() {
        return testCaseName;
    }

    public void setTestCaseName(String testCaseName) {
        this.testCaseName = testCaseName;
    }

    public List<String> getLogLines() {
        return logLines;
    }

    public void setLogLines(List<String> logLines) {
        this.logLines = logLines;
    }

    public int getResCode() {
        return resCode;
    }

    public void setResCode(int resCode) {
        this.resCode = resCode;
    }

    public String getScreenShot() {
        return screenShot;
    }

    public void setScreenShot(String screenShot) {
        this.screenShot = screenShot;
    }

    public void setTestId(Integer testId) {
        this.testId = testId;
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();

        String jsonInString = null;
        try {
            jsonInString = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{\"resCode\":1, \"message\":\"Error converting to JSON\"," +
                    "\"cause\":\"" + e.getMessage() + "\"}";
        }
        return jsonInString;
    }
}
