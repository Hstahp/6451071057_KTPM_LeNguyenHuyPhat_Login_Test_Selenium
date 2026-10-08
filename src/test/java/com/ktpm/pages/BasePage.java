package com.ktpm.pages;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * BasePage: Thao tác chung (Wait, Click, Type, Clear, GetText, GetAttribute, Check Alert,...)
 */
public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void navigateTo(String url) {
        driver.get(url);
    }

    public void click(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    public void type(By locator, String text) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(text);
    }

    public void clear(By locator) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
    }

    public String getText(By locator) {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return element.getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    public String getAttribute(By locator, String attributeName) {
        try {
            return driver.findElement(locator).getAttribute(attributeName);
        } catch (Exception e) {
            return "";
        }
    }

    public void pressKey(By locator, Keys key) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.sendKeys(key);
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getTitle() {
        return driver.getTitle();
    }

    public boolean isAlertPresent() {
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            alert.dismiss();
            return alertText != null;
        } catch (Exception e) {
            return false;
        }
    }
}
