package com.ktpm.model;

/**
 * Model chứa thông tin chi tiết về kết quả của một Test Case
 */
public class TestCaseResult {
    private String testCaseId;
    private String testName;
    private String inputData;
    private String expectedResult;
    private String actualResult;
    private String status; // PASS hoặc FAIL
    private String executionTime;

    public TestCaseResult(String testCaseId, String testName, String inputData, String expectedResult, String actualResult, String status, String executionTime) {
        this.testCaseId = testCaseId;
        this.testName = testName;
        this.inputData = inputData;
        this.expectedResult = expectedResult;
        this.actualResult = actualResult;
        this.status = status;
        this.executionTime = executionTime;
    }

    public String getTestCaseId() { return testCaseId; }
    public String getTestName() { return testName; }
    public String getInputData() { return inputData; }
    public String getExpectedResult() { return expectedResult; }
    public String getActualResult() { return actualResult; }
    public String getStatus() { return status; }
    public String getExecutionTime() { return executionTime; }
}
