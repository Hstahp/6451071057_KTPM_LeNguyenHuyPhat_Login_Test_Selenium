package com.ktpm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * LoginPage: Locator và thao tác cho Form đăng nhập UTC (kế thừa BasePage)
 */
public class LoginPage extends BasePage {
    public static final String PAGE_URL = "https://vanphongdientu.utc.edu.vn/Login";

    private final By usernameInput = By.name("username");
    private final By passwordInput = By.name("userpwd");
    private final By rememberCheckbox = By.id("persistent");
    private final By rememberLabel = By.cssSelector("label[for='persistent']");
    private final By loginButton = By.cssSelector("input.submit_login");
    private final By googleLoginButton = By.cssSelector("a.button");
    private final By forgotPasswordLink = By.cssSelector("div.helps a");
    private final By errorMessage = By.cssSelector("div.error");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        navigateTo(PAGE_URL);
    }

    public void enterUsername(String username) {
        type(usernameInput, username);
    }

    public void enterPassword(String password) {
        type(passwordInput, password);
    }

    public void clearUsername() {
        clear(usernameInput);
    }

    public void clearPassword() {
        clear(passwordInput);
    }

    public void toggleRememberMe() {
        try {
            click(rememberLabel);
        } catch (Exception e) {
            try {
                WebElement checkbox = driver.findElement(rememberCheckbox);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkbox);
            } catch (Exception ex) {
                // ignore
            }
        }
    }

    public boolean isRememberMeChecked() {
        try {
            return driver.findElement(rememberCheckbox).isSelected();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickLogin() {
        click(loginButton);
    }

    public void clickForgotPassword() {
        click(forgotPasswordLink);
    }

    public void clickGoogleLogin() {
        click(googleLoginButton);
    }

    public String getErrorMessage() {
        return getText(errorMessage);
    }

    public String getPasswordInputType() {
        return getAttribute(passwordInput, "type");
    }

    public String getPasswordValue() {
        return getAttribute(passwordInput, "value");
    }

    public String getUsernameValue() {
        return getAttribute(usernameInput, "value");
    }

    public void pressEnterOnPassword() {
        pressKey(passwordInput, Keys.ENTER);
    }
}
