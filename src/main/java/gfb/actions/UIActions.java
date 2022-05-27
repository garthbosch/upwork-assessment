package gfb.actions;

import gfb.models.BingPageLocators;
import gfb.models.GooglePageLocators;
import gfb.models.SearchResultsDetails;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.ScreenshotException;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UIActions extends SoftAssert {

    private static final String GOOGLE = "google";
    private gfb.utils.SeleniumWebDriverUtils driver;
    private Map<String, String> configMan;
    private String searchEngine;

    public UIActions(gfb.utils.SeleniumWebDriverUtils driver, Map<String, String> configMan, String searchEngine) {
        this.driver = driver;
        this.configMan = configMan;
        this.searchEngine = searchEngine;
    }

    /*This method enters the search keyword in the search bar and performs an Enter button press
     * Depending on the search engine selected in the config.properties file it will fetch the element locators from the enums*/
    public void enterTextToSearchAndPressEnter() {
        try {
            String element = searchEngine.equalsIgnoreCase(GOOGLE) ? GooglePageLocators.SEARCH_TEXTBOX.getElement() :
                    BingPageLocators.SEARCH_TEXTBOX.getElement();
            String elementType = searchEngine.equalsIgnoreCase(GOOGLE) ? GooglePageLocators.SEARCH_TEXTBOX.getElementType() :
                    BingPageLocators.SEARCH_TEXTBOX.getElementType();

            driver.enterText(element, elementType, configMan.get("searchKeyword"));
            driver.pressButton(element, elementType);
        } catch (WebDriverException e) {
            throw new WebDriverException(e);
        }
    }

    /*This method fetch the first 10 search results and add it to a list. This list is return from where it was called
     * Depending on the search engine selected in the config.properties file it will fetch the element locators from the enums*/
    public List<SearchResultsDetails> getGoogleListOfURLs() {
        List<SearchResultsDetails> listOfURLs = new ArrayList<>();
        driver.checkPageIsReady();

        try {
            String element = searchEngine.equalsIgnoreCase(GOOGLE) ? GooglePageLocators.SEARCH_RESULTS.getElement() :
                    BingPageLocators.SEARCH_RESULTS.getElement();
            String elementType = searchEngine.equalsIgnoreCase(GOOGLE) ? GooglePageLocators.SEARCH_RESULTS.getElementType() :
                    BingPageLocators.SEARCH_RESULTS.getElementType();


            List<WebElement> allSearchElements = driver.findElements(element, elementType);
            allSearchElements = allSearchElements.size() > 10 ? allSearchElements.subList(0, 10) : allSearchElements; //this will only return the first 10 results
            for (int i = 0; i < allSearchElements.size(); i++) {
                SearchResultsDetails searchResultsDetail = new SearchResultsDetails();

                WebElement urlElement = allSearchElements.get(i).findElement(By.tagName("a"));
                WebElement titleElement = urlElement.findElement(By.tagName("h3"));
                WebElement descriptionElement = allSearchElements.get(i);

                searchResultsDetail.setUrl(urlElement.getAttribute("href"));
                searchResultsDetail.setTitle(titleElement.getText());
                searchResultsDetail.setDescription(descriptionElement.getText());
                listOfURLs.add(searchResultsDetail);
            }
        } catch (WebDriverException e) {
            throw new WebDriverException(e);
        }
        return listOfURLs;
    }

    /*This method fetch the first 10 search results and add it to a list. This list is return from where it was called
     * Depending on the search engine selected in the config.properties file it will fetch the element locators from the enums*/
    public List<SearchResultsDetails> getBingListOfURLs() {
        List<SearchResultsDetails> listOfURLs = new ArrayList<>();
        driver.checkPageIsReady();

        try {
            String element = searchEngine.equalsIgnoreCase(GOOGLE) ? GooglePageLocators.SEARCH_RESULTS.getElement() :
                    BingPageLocators.SEARCH_RESULTS.getElement();
            String elementType = searchEngine.equalsIgnoreCase(GOOGLE) ? GooglePageLocators.SEARCH_RESULTS.getElementType() :
                    BingPageLocators.SEARCH_RESULTS.getElementType();

            List<WebElement> allSearchElements = driver.findElements(element, elementType);
            allSearchElements = allSearchElements.size() > 10 ? allSearchElements.subList(0, 10) : allSearchElements; //this will only return the first 10 results
            for (int i = 0; i < allSearchElements.size(); i++) {
                SearchResultsDetails searchResultsDetail = new SearchResultsDetails();

                WebElement titleElement = allSearchElements.get(i).findElement(By.tagName("h2")).findElement(By.tagName("a"));
                WebElement urlElement = titleElement;
                WebElement descriptionElement = allSearchElements.get(i).findElement(By.tagName("p"));

                searchResultsDetail.setUrl(urlElement.getAttribute("href"));
                searchResultsDetail.setTitle(titleElement.getText());
                searchResultsDetail.setDescription(descriptionElement.getText());
                listOfURLs.add(searchResultsDetail);
            }
        } catch (WebDriverException e) {
            throw new WebDriverException(e);
        }
        return listOfURLs;
    }

    public void takeScreenshot(String screenshotName) {
        screenshotName = screenshotName.replaceAll("[^a-zA-Z0-9-_]", "") + ".png";
        File imagePath = new File(configMan.get("reportingAndImagesLocation") + "screenshots/" + screenshotName);
        File srcFile = (File) ((TakesScreenshot) driver.getDriver()).getScreenshotAs(OutputType.FILE);

        try {
            FileUtils.copyFile(srcFile, new File(configMan.get("reportingAndImagesLocation") + screenshotName));
        } catch (ScreenshotException | IOException e) {
            System.out.println("An error occurred while generating a screenshot - " + e.getMessage());
        }
    }
}
