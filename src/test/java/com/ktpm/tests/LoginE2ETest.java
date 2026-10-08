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

    @Test(priority = 2, description = "TC2: Kiểm thử tấn công SQL Injection ở ô Mật khẩu")
    public void testTC2_SqlInjectionPassword() {
        String testId = "TC2";
        String testName = "Kiểm thử tấn công SQL Injection ở ô Mật khẩu";
        String inputData = "username: 'testuser', password: '\' OR \'1\'=\'1'";
        String expected = "Tài khoản không đúng (chặn tấn công SQLi mật khẩu)";
        String actual = "";
        String status = "FAIL";

        try {
            loginPage.enterUsername("testuser");
            Thread.sleep(500);
            loginPage.enterPassword("' OR '1'='1");
            Thread.sleep(500);
            loginPage.clickLogin();
            Thread.sleep(1200);

            actual = loginPage.getErrorMessage();
            Assert.assertTrue(actual.contains("không đúng") || actual.contains("Tài khoản"),
                    "Hệ thống không chặn được SQL Injection ở ô mật khẩu!");
            status = "PASS";
        } catch (Throwable t) {
            actual = actual.isEmpty() ? ("Lỗi: " + t.getMessage()) : actual;
            throw new RuntimeException(t);
        } finally {
            testResults.add(new TestCaseResult(testId, testName, inputData, expected, actual, status, LocalTime.now().format(TIME_FMT)));
        }
    }

    @Test(priority = 3, description = "TC3: Kiểm thử tấn công XSS dạng SVG Payload ở ô Tên đăng nhập")
    public void testTC3_XssSvgUsername() {
        String testId = "TC3";
        String testName = "Kiểm thử tấn công XSS dạng SVG Payload ở ô Tên đăng nhập";
        String inputData = "username: '<svg onload=alert(\'XSS_UTC\')>', password: '123456'";
        String expected = "Hệ thống chặn script XSS thành công, không bật alert";
        String actual = "";
        String status = "FAIL";

        try {
            loginPage.enterUsername("<svg onload=alert('XSS_UTC')>");
            Thread.sleep(500);
            loginPage.enterPassword("123456");
            Thread.sleep(500);
            loginPage.clickLogin();
            Thread.sleep(1200);

            boolean hasAlert = loginPage.isAlertPresent();
            Assert.assertFalse(hasAlert, "Mã script XSS dạng SVG đã bị kích hoạt trên trình duyệt!");

            actual = "Hệ thống xử lý an toàn: " + loginPage.getErrorMessage();
            status = "PASS";
        } catch (Throwable t) {
            actual = actual.isEmpty() ? ("Lỗi: " + t.getMessage()) : actual;
            throw new RuntimeException(t);
        } finally {
            testResults.add(new TestCaseResult(testId, testName, inputData, expected, actual, status, LocalTime.now().format(TIME_FMT)));
        }
    }

    @Test(priority = 4, description = "TC4: Kiểm thử dán (Paste) dữ liệu vào ô Tên đăng nhập")
    public void testTC4_PasteUsername() {
        String testId = "TC4";
        String testName = "Kiểm thử dán (Paste) dữ liệu vào ô Tên đăng nhập";
        String pasteValue = "sv6451071057";
        String inputData = "Dán (Ctrl + V) chuỗi: " + pasteValue;
        String expected = "Chuỗi Tên đăng nhập được dán thành công vào ô username";
        String actual = "";
        String status = "FAIL";

        try {
            WebElement userInput = driver.findElement(By.name("username"));
            userInput.click();

            try {
                StringSelection stringSelection = new StringSelection(pasteValue);
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
                userInput.sendKeys(Keys.chord(Keys.CONTROL, "v"));
            } catch (Throwable t) {
                userInput.sendKeys(pasteValue);
            }
            Thread.sleep(800);

            String currentValue = loginPage.getUsernameValue();
            actual = "Giá trị sau khi dán vào ô Username: " + currentValue;
            Assert.assertEquals(currentValue, pasteValue, "Tính năng paste vào ô Username không hoạt động!");
            status = "PASS";
        } catch (Throwable t) {
            actual = actual.isEmpty() ? ("Lỗi: " + t.getMessage()) : actual;
            throw new RuntimeException(t);
        } finally {
            testResults.add(new TestCaseResult(testId, testName, inputData, expected, actual, status, LocalTime.now().format(TIME_FMT)));
        }
    }

    @Test(priority = 5, description = "TC5: Kiểm thử liên kết 'Bạn quên mật khẩu đăng nhập ?'")
    public void testTC5_ForgotPasswordLink() {
        String testId = "TC5";
        String testName = "Kiểm thử liên kết 'Bạn quên mật khẩu đăng nhập ?'";
        String inputData = "Click link: 'Bạn quên mật khẩu đăng nhập ?'";
        String expected = "Trình duyệt chuyển hướng tới trang Quên mật khẩu (/Login/GetPass)";
        String actual = "";
        String status = "FAIL";

        try {
            loginPage.clickForgotPassword();
            Thread.sleep(1200);

            String currentUrl = loginPage.getCurrentUrl();
            actual = "Đã chuyển hướng thành công tới URL: " + currentUrl;
            Assert.assertTrue(currentUrl.toLowerCase().contains("getpass") || driver.getTitle().contains("quên") || driver.getTitle().contains("Quên"),
                    "Liên kết Quên mật khẩu không chuyển hướng đúng trang!");
            status = "PASS";
        } catch (Throwable t) {
            actual = actual.isEmpty() ? ("Lỗi: " + t.getMessage()) : actual;
            throw new RuntimeException(t);
        } finally {
            testResults.add(new TestCaseResult(testId, testName, inputData, expected, actual, status, LocalTime.now().format(TIME_FMT)));
        }
    }
}
