package com.ktpm.tests;

import com.ktpm.base.BaseTest;
import com.ktpm.model.TestCaseResult;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.time.LocalTime;

/**
 * Các Test Scripts kiểm thử Đăng nhập (E2E Test)
 * Kế thừa BaseTest
 */
public class LoginE2ETest extends BaseTest {
    @Test(priority = 1, description = "TC1: Nhập Tên đăng nhập là Mã sinh viên (chữ số)")
    public void testTC1_NumericUsername() {
        String testId = "TC1";
        String testName = "Nhập Tên đăng nhập là Mã sinh viên (chữ số)";
        String inputData = "username: '6451071057', password: 'pass123'";
        String expected = "Tài khoản không đúng (hoặc Tài khoản hoặc mật khẩu không đúng.)";
        String actual = "";
        String status = "FAIL";

        try {
            loginPage.enterUsername("6451071057");
            Thread.sleep(500);
            loginPage.enterPassword("pass123");
            Thread.sleep(500);
            loginPage.clickLogin();
            Thread.sleep(1200);

            actual = loginPage.getErrorMessage();
            Assert.assertTrue(actual.contains("không đúng") || actual.contains("Tài khoản"),
                    "Không hiển thị thông báo lỗi khi nhập username mã sinh viên!");
            status = "PASS";
        } catch (Throwable t) {
            actual = actual.isEmpty() ? ("Lỗi: " + t.getMessage()) : actual;
            throw new RuntimeException(t);
        } finally {
            testResults.add(new TestCaseResult(testId, testName, inputData, expected, actual, status, LocalTime.now().format(TIME_FMT)));
        }
    }
}
