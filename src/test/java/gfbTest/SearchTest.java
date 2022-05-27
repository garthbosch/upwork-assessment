package gfbTest;

import gfb.actions.UIActions;
import gfb.models.SearchResultsDetails;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.util.List;

public class SearchTest extends BaseTest {

    /*This method run the test scenarios for both search engines*/
    @Test
    public void runSearchTest() {
        //Google search
        uiActions = new UIActions(driver, configMan, "google");
        uiActions.enterTextToSearchAndPressEnter();
        googleResultList = uiActions.getGoogleListOfURLs();
        validateResultList(googleResultList, "google");

        //Bing search
        driver.setURL(configMan.get("bing"));
        uiActions = new UIActions(driver, configMan, "bing");
        uiActions.enterTextToSearchAndPressEnter();
        bingResultList = uiActions.getBingListOfURLs();
        validateResultList(bingResultList, "bing");
    }

    /*Validation of returned list items*/
    private void validateResultList(List<SearchResultsDetails> resultList, String searchEngine) {
        System.out.println("===============================================");
        System.out.println("Validating " + searchEngine + " results");
        System.out.println("------------------------------");
        for (int i = 0; i < resultList.size(); i++) {
            if (resultList.get(i).toString().contains(configMan.get("searchKeyword"))) {
                softAssert.assertTrue(true);
                System.out.println(resultList.get(i).getTitle() + " contains the keyword " + configMan.get("searchKeyword"));
            } else {
                softAssert.fail();
                System.err.println(resultList.get(i).getTitle() + " does not contain the keyword " + configMan.get("searchKeyword"));
            }
        }
        System.out.println("===============================================");
    }

    private void checkListAndPrintCommonValues(SearchResultsDetails googleItem, List<SearchResultsDetails> bingResultList) {
        for (SearchResultsDetails bingItem : bingResultList) {
            if (bingItem.getUrl().equalsIgnoreCase(googleItem.getUrl())) {
                System.out.println("Url: " + bingItem.getUrl());
                System.out.println("Title: " + bingItem.getTitle());
                System.out.println("--------------------------------------");
            }
        }
    }

    /* Logs will be printed on how to do the test manually if the test past
     * Common items will be printed */
    @AfterMethod()
    public void outputTestSteps(ITestResult result) {
        System.out.println("The following items appears in both lists:");
        for (SearchResultsDetails googleItem : googleResultList) {
            checkListAndPrintCommonValues(googleItem, bingResultList);
        }
        //region Output steps
        if (result.isSuccess()) {
            System.out.println("===============================================");
            System.out.println("Steps to execute this scenario");
            System.out.println("------------------------------");
            System.out.println("Step 1 - Open either Google Chrome or Firefox");
            System.out.println("Step 2 - Navigate to " + configMan.get("google"));
            System.out.println("Step 3 - Search for " + configMan.get("searchKeyword"));
            System.out.println("Step 4 - Capture the first 10 returned results; i.e. the url, title and description");
            System.out.println("Step 5 - Validate that the keyword " + configMan.get("searchKeyword") + " appears at least once in the returned results");
            System.out.println("Step 6 - Capture which results turned results contains the keyword " + configMan.get("searchKeyword") + " and which does not contain it");
            System.out.println("Step 7 - Repeat steps 2 to 6 navigating to " + configMan.get("bing") + " in step 2");
            System.out.println("Step 8 - Compare stored results for both engines and list out the most popular items (the ones which were found in both search engines)");
            System.out.println("===============================================");
        }
        //endregion
    }
}
