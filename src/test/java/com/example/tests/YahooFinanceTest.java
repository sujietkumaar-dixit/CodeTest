package com.example.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

public class YahooFinanceTest {

    private WebDriver driver;

    // Locators for homepage and Tesla stock page.
    private final By searchBoxLocator = By.xpath("//*[@placeholder='Search for news, symbols or companies']");
    private final By autosuggestFirstEntryLocator = By.xpath("//li[@data-type='quotes'][1]");
    private final By stockPriceLocator = By.xpath("//*[@data-testid='qsp-price']");
    private final By previousCloseLocator = By.xpath("//*[@data-field='regularMarketPreviousClose']");
    private final By volumeLocator = By.xpath("//*[@data-field='regularMarketVolume']");

    @BeforeClass
    public void setUp() {
        // Automatically resolves and downloads the correct ChromeDriver version.
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void testTeslaStock() {
        try {
            // Step 1: Navigate to Yahoo Finance homepage.
            driver.get("https://finance.yahoo.com/");
            log("Navigated to Yahoo Finance.");

            // Step 2: Search for "TSLA".
            WebElement searchBox = waitForElement(searchBoxLocator);
            searchBox.clear();
            searchBox.sendKeys("TSLA");
            log("Typed 'TSLA' in the search box.");

            // Step 3: Click on the first autosuggest entry.
            safeFetch(autosuggestFirstEntryLocator).click();
            log("Clicked on the first autosuggest entry.");

            // Step 4: Verify Tesla stock price is > $200.
            WebElement stockPriceElement = safeFetch(stockPriceLocator);
            String priceText = stockPriceElement.getText().trim();
            log("Retrieved stock price: " + priceText);
            double stockPrice = parsePrice(priceText);
            Assert.assertTrue(stockPrice > 200, "Tesla stock price is not greater than $200.");

            // Step 5: Capture additional information.
            WebElement prevCloseElement = safeFetch(previousCloseLocator);
            WebElement volumeElement = safeFetch(volumeLocator);
            log("Previous Close: " + prevCloseElement.getText().trim());
            log("Volume: " + volumeElement.getText().trim());

        } catch (Exception e) {
            log("Error: " + e.getMessage());
            e.printStackTrace();
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }
    }
    private WebElement safeFetch(By locator) {
        for (int i = 0; i < 3; i++) { // Retry mechanism
            try {
                return waitForElement(locator);
            } catch (StaleElementReferenceException e) {
                System.out.println("Retrying to fetch element: " + locator.toString());
            }
        }
        throw new RuntimeException("Failed to fetch element after retries: " + locator);
    }


    /**
     * Helper method to wait for an element to be visible.
     *
     * @param locator The By locator of the element.
     * @return The visible WebElement.
     */
    private WebElement waitForElement(By locator) {
        return new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Converts a price string to a double.
     *
     * @param priceStr The price string (e.g., "215.55").
     * @return The parsed double value.
     */
    private double parsePrice(String priceStr) {
        try {
            priceStr = priceStr.replaceAll("[,$]", "");
            return Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Unable to parse price: " + priceStr, e);
        }
    }

    /**
     * Simple logging utility.
     *
     * @param message The message to log.
     */
    private void log(String message) {
        System.out.println("[YahooFinanceTest] " + message);
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            log("Closed the browser.");
        }
    }
}
