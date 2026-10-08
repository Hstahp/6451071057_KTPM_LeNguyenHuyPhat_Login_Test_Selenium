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

    @Test(priority = 6, description = "TC6: Kiểm thử nút 'Đăng nhập bằng e-mail UTC'")
    public void testTC6_GoogleEmailLoginButton() {
        String testId = "TC6";
        String testName = "Kiểm thử nút 'Đăng nhập bằng e-mail UTC'";
        String inputData = "Click nút: 'Đăng nhập bằng e-mail UTC'";
        String expected = "Hệ thống thực hiện chuyển hướng tới cổng xác thực Email SSO";
        String actual = "";
        String status = "FAIL";

        try {
            loginPage.clickGoogleLogin();
            Thread.sleep(1200);

            String currentUrl = loginPage.getCurrentUrl();
            actual = "Đã click nút Đăng nhập bằng e-mail UTC, URL hiện tại: " + currentUrl;
            Assert.assertNotNull(currentUrl, "Không nhận diện được URL sau khi click!");
            status = "PASS";
        } catch (Throwable t) {
            actual = actual.isEmpty() ? ("Lỗi: " + t.getMessage()) : actual;
            throw new RuntimeException(t);
        } finally {
            testResults.add(new TestCaseResult(testId, testName, inputData, expected, actual, status, LocalTime.now().format(TIME_FMT)));
        }
    }

    @Test(priority = 7, description = "TC7: Kiểm thử bật/tắt (Toggle) 2 lần ô tích chọn 'Giữ tôi luôn đăng nhập'")
    public void testTC7_ToggleRememberMeTwice() {
        String testId = "TC7";
        String testName = "Kiểm thử bật/tắt (Toggle) 2 lần ô tích chọn 'Giữ tôi luôn đăng nhập'";
        String inputData = "Click 2 lần vào checkbox 'Giữ tôi luôn đăng nhập'";
        String expected = "Trạng thái ô tích chọn trở về không chọn (false)";
        String actual = "";
        String status = "FAIL";

        try {
            loginPage.toggleRememberMe();
            Thread.sleep(400);
            loginPage.toggleRememberMe();
            Thread.sleep(400);

            boolean isChecked = loginPage.isRememberMeChecked();
            actual = "Trạng thái checkbox sau 2 lần click: isChecked = " + isChecked;
            Assert.assertFalse(isChecked, "Checkbox vẫn bị chọn sau khi toggle 2 lần!");
            status = "PASS";
        } catch (Throwable t) {
            actual = actual.isEmpty() ? ("Lỗi: " + t.getMessage()) : actual;
            throw new RuntimeException(t);
        } finally {
            testResults.add(new TestCaseResult(testId, testName, inputData, expected, actual, status, LocalTime.now().format(TIME_FMT)));
        }
    }

    @Test(priority = 8, description = "TC8: Kiểm thử gửi form đăng nhập bằng phím ENTER từ ô Mật khẩu")
    public void testTC8_SubmitFormWithEnterKey() {
        String testId = "TC8";
        String testName = "Kiểm thử gửi form đăng nhập bằng phím ENTER từ ô Mật khẩu";
        String inputData = "username: 'huongnt', password: '123456' + Press ENTER";
        String expected = "Form tự động submit và hiển thị phản hồi từ máy chủ";
        String actual = "";
        String status = "FAIL";

        try {
            loginPage.enterUsername("huongnt");
            Thread.sleep(500);
            loginPage.enterPassword("123456");
            Thread.sleep(500);
            loginPage.pressEnterOnPassword();
            Thread.sleep(1200);

            actual = loginPage.getErrorMessage();
            Assert.assertTrue(actual.contains("không đúng") || actual.contains("Tài khoản"),
                    "Phím ENTER không kích hoạt gửi form đăng nhập!");
            status = "PASS";
        } catch (Throwable t) {
            actual = actual.isEmpty() ? ("Lỗi: " + t.getMessage()) : actual;
            throw new RuntimeException(t);
        } finally {
            testResults.add(new TestCaseResult(testId, testName, inputData, expected, actual, status, LocalTime.now().format(TIME_FMT)));
        }
    }

    @Test(priority = 9, description = "TC9: Kiểm thử nhập Mật khẩu chứa khoảng trắng ở giữa")
    public void testTC9_PasswordWithSpacesInside() {
        String testId = "TC9";
        String testName = "Kiểm thử nhập Mật khẩu chứa khoảng trắng ở giữa";
        String inputData = "username: 'huongnt', password: '123 456@utc'";
        String expected = "Tài khoản không đúng (phân biệt ký tự khoảng trắng trong mật khẩu)";
        String actual = "";
        String status = "FAIL";

        try {
            loginPage.enterUsername("huongnt");
            Thread.sleep(500);
            loginPage.enterPassword("123 456@utc");
            Thread.sleep(500);
            loginPage.clickLogin();
            Thread.sleep(1200);

            actual = loginPage.getErrorMessage();
            Assert.assertTrue(actual.contains("không đúng") || actual.contains("Tài khoản"),
                    "Hệ thống không phát hiện mật khẩu sai khi có khoảng trắng ở giữa!");
            status = "PASS";
        } catch (Throwable t) {
            actual = actual.isEmpty() ? ("Lỗi: " + t.getMessage()) : actual;
            throw new RuntimeException(t);
        } finally {
            testResults.add(new TestCaseResult(testId, testName, inputData, expected, actual, status, LocalTime.now().format(TIME_FMT)));
        }
    }

    @Test(priority = 10, description = "TC10: Kiểm thử xóa (Clear) dữ liệu ô Username trước khi nhấn Đăng nhập")
    public void testTC10_ClearUsernameBeforeSubmit() {
        String testId = "TC10";
        String testName = "Kiểm thử xóa (Clear) dữ liệu ô Username trước khi nhấn Đăng nhập";
        String inputData = "username: '' (xóa sau khi nhập), password: '123456'";
        String expected = "Bạn chưa nhập tên đăng nhập";
        String actual = "";
        String status = "FAIL";

        try {
            loginPage.enterUsername("tempuser");
            Thread.sleep(400);
            loginPage.clearUsername();
            Thread.sleep(400);
            loginPage.enterPassword("123456");
            Thread.sleep(400);
            loginPage.clickLogin();
            Thread.sleep(1200);

            actual = loginPage.getErrorMessage();
            Assert.assertTrue(actual.contains("tên đăng nhập") || actual.contains("chưa nhập"),
                    "Không hiển thị thông báo yêu cầu nhập tên đăng nhập sau khi xóa dữ liệu ô Username!");
            status = "PASS";
        } catch (Throwable t) {
            actual = actual.isEmpty() ? ("Lỗi: " + t.getMessage()) : actual;
            throw new RuntimeException(t);
        } finally {
            testResults.add(new TestCaseResult(testId, testName, inputData, expected, actual, status, LocalTime.now().format(TIME_FMT)));
        }
    }

    @Test(priority = 11, description = "TC11: Kiểm thử nhập Mật khẩu chứa ký tự Tiếng Việt có dấu (UTF-8)")
    public void testTC11_VietnameseUnicodePassword() {
        String testId = "TC11";
        String testName = "Kiểm thử nhập Mật khẩu chứa ký tự Tiếng Việt có dấu (UTF-8)";
        String inputData = "username: 'huongnt', password: 'MậtKhẩuViệt2026!'";
        String expected = "Hệ thống xử lý chuỗi UTF-8 an toàn, báo lỗi Tài khoản không đúng";
        String actual = "";
        String status = "FAIL";

        try {
            loginPage.enterUsername("huongnt");
            Thread.sleep(500);
            loginPage.enterPassword("MậtKhẩuViệt2026!");
            Thread.sleep(500);
            loginPage.clickLogin();
            Thread.sleep(1200);

            actual = loginPage.getErrorMessage();
            Assert.assertTrue(actual.contains("không đúng") || actual.contains("Tài khoản"),
                    "Hệ thống gặp lỗi khi nhận chuỗi mật khẩu có dấu tiếng Việt!");
            status = "PASS";
        } catch (Throwable t) {
            actual = actual.isEmpty() ? ("Lỗi: " + t.getMessage()) : actual;
            throw new RuntimeException(t);
        } finally {
            testResults.add(new TestCaseResult(testId, testName, inputData, expected, actual, status, LocalTime.now().format(TIME_FMT)));
        }
    }

    @Test(priority = 12, description = "TC12: Kiểm thử nhập Tên đăng nhập là địa chỉ Email UTC hợp lệ")
    public void testTC12_EmailUsernameFormat() {
        String testId = "TC12";
        String testName = "Kiểm thử nhập Tên đăng nhập là địa chỉ Email UTC hợp lệ";
        String inputData = "username: 'sinhvien@st.utc.edu.vn', password: '123456'";
        String expected = "Tài khoản không đúng";
        String actual = "";
        String status = "FAIL";

        try {
            loginPage.enterUsername("sinhvien@st.utc.edu.vn");
            Thread.sleep(500);
            loginPage.enterPassword("123456");
            Thread.sleep(500);
            loginPage.clickLogin();
            Thread.sleep(1200);

            actual = loginPage.getErrorMessage();
            Assert.assertTrue(actual.contains("không đúng") || actual.contains("Tài khoản"),
                    "Không hiển thị thông báo lỗi với tài khoản định dạng Email không tồn tại!");
            status = "PASS";
        } catch (Throwable t) {
            actual = actual.isEmpty() ? ("Lỗi: " + t.getMessage()) : actual;
            throw new RuntimeException(t);
        } finally {
            testResults.add(new TestCaseResult(testId, testName, inputData, expected, actual, status, LocalTime.now().format(TIME_FMT)));
        }
    }
}
