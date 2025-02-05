package demo.wrappers;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Wrappers {
    /*
     * Write your selenium wrappers here
     */
    private WebDriver driver;

    /*
     * Write your selenium wrappers here
     */
    public Wrappers(WebDriver driver) {
        this.driver = driver;
    }

    public void navigateToUrl(String url) {
        driver.get(url);
    }

    public void clickOnElement(WebElement element) {
        if (element != null) {
            element.click();
        } else {
            System.out.println("Element is null and cannot be clicked.");
        }
    }
}
