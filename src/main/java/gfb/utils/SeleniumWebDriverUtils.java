package gfb.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static io.github.bonigarcia.wdm.config.DriverManagerType.CHROME;
import static io.github.bonigarcia.wdm.config.DriverManagerType.FIREFOX;
import static org.awaitility.Awaitility.await;

public class SeleniumWebDriverUtils {
    private static final String XPATH = "xpath";
    private static final String ID = "id";
    private static final String CSS = "css";
    private static final String NAME = "name";
    private static final String CLASSNAME = "classname";
    private static final String LINK_TEXT = "linktext";
    private static final String PARTIAL_LINK_TEXT = "partiallinktext";
    private static final String NO_ELEMENT_ERROR_TEXT = "Unable to find element ";
    private WebDriver driver;
    private Integer waitTimeOut = 30;

    public void setURL(String baseUrl) {
        if (baseUrl != null && !baseUrl.isEmpty()) {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(waitTimeOut));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(120));
            driver.manage().window().maximize();
            driver.manage().deleteAllCookies();
            driver.get(baseUrl);
        } else {
            throw new WebDriverException("====================NO URL SPECIFIED======================");
        }
    }

    /**
     * Starts the selenium session with a new Google Chrome session
     */
    public void startDriver(String baseUrl, String browserType) {
        try {
            switch (browserType.toLowerCase()) {
                case "chrome":
                case "google":
                case "google chrome":
                    driver = new ChromeDriver(setChromeOptions());
                    break;

                case "firefox":
                case "fire":
                case "fire fox":
                    driver = new FirefoxDriver(setFirefoxOptions());
                    break;

                default:
                    String errorMessage = "Browser type " + browserType + " is incorrect, please chrome or firefox";
                    throw new AssertionError(errorMessage);
            }

            setURL(baseUrl);
            System.out.println("Done selecting Browser");
            System.out.println("Selenium driver started");
        } catch (Exception e) {
            System.err.println("Something went wrong while starting up selenium driver - " + e.getMessage());
        }
    }

    private FirefoxOptions setFirefoxOptions() {
        FirefoxOptions firefoxOptions = new FirefoxOptions();

        try {
            WebDriverManager.getInstance(FIREFOX).setup();

            firefoxOptions.setCapability(CapabilityType.OVERLAPPING_CHECK_DISABLED, false);
            firefoxOptions.setCapability(CapabilityType.ELEMENT_SCROLL_BEHAVIOR, true);
            firefoxOptions.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, true);
            firefoxOptions.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
            firefoxOptions.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
            firefoxOptions.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);
            firefoxOptions.setCapability(CapabilityType.SUPPORTS_APPLICATION_CACHE, false);
            firefoxOptions.setCapability("idle-timeout", 300000);
            firefoxOptions.addArguments("test-type");
            firefoxOptions.addArguments("--ignore-certificate-errors");
            firefoxOptions.addArguments("disable-infobars");
            firefoxOptions.addArguments("--no-sandbox");
            firefoxOptions.addArguments("--disable-dev-shm-usage");
            firefoxOptions.addArguments("enable-automation");

        } catch (WebDriverException e) {
            System.err.println("An error occurred while setting the chrome options - " + e.getMessage());
            e.printStackTrace();
        }
        return firefoxOptions;
    }

    /**
     * Configures the variables for the Chromedriver
     */
    private ChromeOptions setChromeOptions() {
        ChromeOptions chromeOptions = new ChromeOptions();

        try {
            WebDriverManager.getInstance(CHROME).setup();

            chromeOptions.setCapability(CapabilityType.OVERLAPPING_CHECK_DISABLED, false);
            chromeOptions.setCapability(CapabilityType.ELEMENT_SCROLL_BEHAVIOR, true);
            chromeOptions.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, true);
            chromeOptions.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
            chromeOptions.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
            chromeOptions.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);
            chromeOptions.setCapability(CapabilityType.SUPPORTS_APPLICATION_CACHE, false);
            chromeOptions.setCapability("idle-timeout", 300000);
            chromeOptions.addArguments("test-type");
            chromeOptions.addArguments("--ignore-certificate-errors");
            chromeOptions.addArguments("disable-infobars");
            chromeOptions.addArguments("--no-sandbox");
            chromeOptions.addArguments("--disable-dev-shm-usage");
            chromeOptions.addArguments("enable-automation");

            HashMap<String, Object> chromePrefs = new HashMap<>();
            chromePrefs.put("download.prompt_for_download", false);
            chromePrefs.put("download.directory_upgrade", true);
            chromePrefs.put("safebrowsing.enabled", false);
            chromeOptions.setExperimentalOption("prefs", chromePrefs);

        } catch (WebDriverException e) {
            System.err.println("An error occurred while setting the chrome options - " + e.getMessage());
            e.printStackTrace();
        }

        return chromeOptions;
    }

    public void clearCookies() {
        driver.manage().deleteAllCookies();
    }

    /**
     * Logs the user out and shutdown the selenium session
     */
    public void shutdown() {
        try {
            driver.quit();
            System.out.println("Driver shutting down");
        } catch (Exception ex) {
            System.err.println("Error found while shutting down driver - " + ex.getMessage());
        }
    }

    public boolean waitForElementClickable(String element, String locatorType) {
        boolean isClickable;
        try {
            Wait<WebDriver> wait = new FluentWait<>(driver).withTimeout(Duration.ofSeconds(waitTimeOut))
                    .pollingEvery(Duration.ofMillis(600)).ignoring(NoSuchElementException.class);

            switch (locatorType.toLowerCase()) {
                case XPATH:
                    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(element)));
                    break;

                case ID:
                    wait.until(ExpectedConditions.elementToBeClickable(By.id(element)));
                    break;

                case LINK_TEXT:
                    wait.until(ExpectedConditions.elementToBeClickable(By.linkText(element)));
                    break;

                case CSS:
                    wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(element)));
                    break;

                case CLASSNAME:
                    wait.until(ExpectedConditions.elementToBeClickable(By.className(element)));
                    break;

                case NAME:
                    wait.until(ExpectedConditions.elementToBeClickable(By.name(element)));
                    break;

                case PARTIAL_LINK_TEXT:
                    wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText(element)));
                    break;

                default:
                    logNoPropertyError();
            }
            isClickable = true;
        } catch (Exception ex) {
            System.err.println("Unable to click element " + element + " - " + ex.getMessage());
            isClickable = false;
        }
        return isClickable;
    }

    public void enterText(String element, String locatorType, String text) {
        Actions actions = new Actions(driver);
        try {
            if (waitForElementClickable(element, locatorType)) {
                clearField(element, locatorType);
                switch (locatorType.toLowerCase()) {
                    case XPATH:
                        actions.doubleClick(driver.findElement(By.xpath(element))).build().perform();
                        driver.findElement(By.xpath(element)).sendKeys(text);
                        break;

                    case ID:
                        actions.doubleClick(driver.findElement(By.id(element))).build().perform();
                        driver.findElement(By.id(element)).sendKeys(text);
                        break;

                    case LINK_TEXT:
                        actions.doubleClick(driver.findElement(By.linkText(element))).build().perform();
                        driver.findElement(By.linkText(element)).sendKeys(text);
                        break;

                    case CSS:
                        actions.doubleClick(driver.findElement(By.cssSelector(element))).build().perform();
                        driver.findElement(By.cssSelector(element)).sendKeys(text);
                        break;

                    case CLASSNAME:
                        actions.doubleClick(driver.findElement(By.className(element))).build().perform();
                        driver.findElement(By.className(element)).sendKeys(text);
                        break;

                    case NAME:
                        actions.doubleClick(driver.findElement(By.name(element))).build().perform();
                        driver.findElement(By.name(element)).sendKeys(text);
                        break;

                    case PARTIAL_LINK_TEXT:
                        actions.doubleClick(driver.findElement(By.partialLinkText(element))).build().perform();
                        driver.findElement(By.partialLinkText(element)).sendKeys(text);
                        break;

                    default:
                        logNoPropertyError();
                }
            }
            enterSuccessLog(element, text);
        } catch (Exception ex) {
            System.err.println("Unable to select and enter text " + element + " - " + ex.getMessage());
        }
    }

    public void pressButton(String element, String locatorType) {
        if (waitForElementClickable(element, locatorType)) {
            switch (locatorType.toLowerCase()) {
                case XPATH:
                    driver.findElement(By.xpath(element)).sendKeys(Keys.ENTER);
                    break;

                case ID:
                    driver.findElement(By.id(element)).sendKeys(Keys.ENTER);
                    break;

                case LINK_TEXT:
                    driver.findElement(By.linkText(element)).sendKeys(Keys.ENTER);
                    break;

                case CSS:
                    driver.findElement(By.cssSelector(element)).sendKeys(Keys.ENTER);
                    break;

                case CLASSNAME:
                    driver.findElement(By.className(element)).sendKeys(Keys.ENTER);
                    break;

                case NAME:
                    driver.findElement(By.name(element)).sendKeys(Keys.ENTER);
                    break;

                case PARTIAL_LINK_TEXT:
                    driver.findElement(By.partialLinkText(element)).sendKeys(Keys.ENTER);
                    break;

                default:
                    logNoPropertyError();
            }
        }
    }

    public void clearField(String element, String locatorType) {
        try {
            if (waitForElementClickable(element, locatorType)) {
                switch (locatorType.toLowerCase()) {
                    case XPATH:
                        driver.findElement(By.xpath(element)).clear();
                        break;

                    case ID:
                        driver.findElement(By.id(element)).clear();
                        break;

                    case LINK_TEXT:
                        driver.findElement(By.linkText(element)).clear();
                        break;

                    case CSS:
                        driver.findElement(By.cssSelector(element)).clear();
                        break;

                    case CLASSNAME:
                        driver.findElement(By.className(element)).clear();
                        break;

                    case NAME:
                        driver.findElement(By.name(element)).clear();
                        break;

                    case PARTIAL_LINK_TEXT:
                        driver.findElement(By.partialLinkText(element)).clear();
                        break;

                    default:
                        logNoPropertyError();
                }
            }
        } catch (Exception ex) {
            System.err.println(NO_ELEMENT_ERROR_TEXT + element + " - " + ex.getMessage());
        }
    }

    public List<WebElement> findElements(String element, String locatorType) {
        List<WebElement> findElements = null;
        try {
            switch (locatorType.toLowerCase()) {
                case ID:
                    findElements = driver.findElements(By.id(element));
                    break;

                case NAME:
                    findElements = driver.findElements(By.name(element));
                    break;

                case CSS:
                    findElements = driver.findElements(By.cssSelector(element));
                    break;

                case XPATH:
                    findElements = driver.findElements(By.xpath(element));
                    break;

                case LINK_TEXT:
                    findElements = driver.findElements(By.linkText(element));
                    break;

                case PARTIAL_LINK_TEXT:
                    findElements = driver.findElements(By.partialLinkText(element));
                    break;

                default:
                    logNoPropertyError();
            }
        } catch (Exception e) {
            System.err.println("Something went wrong while finding the elements");
        }
        return findElements;
    }

    private void logNoPropertyError() {
        System.out.println("No or incorrect element type was specified, please specify which element attribute you want to interact with");
    }

    private void enterSuccessLog(String element, String value) {
        System.out.println(value + " successfully entered into " + element);
    }

    /* This method runs javascript to check if the page is ready. If it returns false it will wait 2 seconds and do another attempt (pollinterval of 2 secs).
    It will continue to do this for 90 seconds. If the page is ready it will continue with the test */
    public void checkPageIsReady() {
        await().pollInterval(2, TimeUnit.SECONDS).atMost(90, TimeUnit.SECONDS).until(isPageReady());
    }

    private Callable<Boolean> isPageReady() {
        return () -> {
            JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
            boolean readyStateOfPage = javascriptExecutor.executeScript("return document.readyState").toString().equals("complete");
            if (!readyStateOfPage) {
                System.out.println("The page with title " + driver.getTitle() + " is not ready yet. In 2 seconds another attempt will be made.");
            }
            return readyStateOfPage;
        };
    }
    public WebDriver getDriver() {
        return driver;
    }
}
