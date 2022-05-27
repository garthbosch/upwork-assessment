package gfbTest;

import gfb.actions.UIActions;
import gfb.models.SearchResultsDetails;
import gfb.utils.FileHelper;
import gfb.utils.SeleniumWebDriverUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseTest {

    protected SeleniumWebDriverUtils driver = new SeleniumWebDriverUtils();
    protected String url;
    protected String searchEngine;
    protected Map<String, String> configMan = new HashMap<>();
    protected UIActions uiActions;
    protected List<SearchResultsDetails> googleResultList = new ArrayList<>();
    protected List<SearchResultsDetails> bingResultList = new ArrayList<>();
    SoftAssert softAssert = new SoftAssert();

    /*This method happens before any test class gets executed.
     * The search engine, e.g. google or bing, get passed via the TestNG xml file
     * The method loads the config properties file to a HashMap
     * The url get set depending on which search engine will be used
     * The Selenium driver gets initialised and started
     * Cookies are cleared
     * The uiActions class gets initialised*/
    @Parameters("searchEngine")
    @BeforeClass
    public void setUpTests(String searchEngine) {
        this.searchEngine = searchEngine;
        configMan = FileHelper.readPropertiesFile("src/main/resources/config.properties");
        url = configMan.get(searchEngine);
        driver.startDriver(url, configMan.get("browserType"));
        driver.clearCookies();
    }

    /*This method will always close the Selenium WebDriver whether the test pass or fail */
    @AfterClass(alwaysRun = true)
    public void closeDown() {
        softAssert.assertAll();
        driver.shutdown();
    }
}
