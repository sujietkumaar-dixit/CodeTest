# Yahoo Finance UI Automation Test Plan

## Overview
This project contains automated UI tests for Yahoo Finance using Selenium WebDriver and TestNG. The primary use case is to search for the Tesla (TSLA) stock, verify the autosuggest feature, navigate to the Tesla stock page, verify the stock price, and capture additional stock data.

## Prerequisites
- Java 1.8 or higher
- Maven 3.6.0 or higher
- Chrome browser
- ChromeDriver (automatically managed by WebDriverManager)

## Project Structure
- `pom.xml`: Maven configuration file with dependencies and plugins.
- `testng.xml`: TestNG suite configuration file.
- `src/test/java/com/example/tests/YahooFinanceTest.java`: Test class containing the test cases.

## Setup Instructions
1. **Clone the repository:**
   ```sh
   git clone <repository-url>
   cd <repository-directory>
