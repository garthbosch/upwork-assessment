# Upwork Central Hiring - QA - Take Home Task

### Tools
- Java
- Selenium WebDriver
- TestNG
- WebDriver Manager
- Lombok

### Setup environment
- Download and install sdkman - https://sdkman.io/install
- Once sdkman is installed then install the following (commands included):
  - jdk - sdk install java <version>
  - maven - sdk install maven <version>
  - clone or copy source code to your local machine - https://github.com/garthbosch/upwork-assessment.git
  
  Note: To list the versions of java you can run this command (you can do the same for maven) - sdk list java 

### Framework Architecture
	Project-Name
        |_src
        |   |_main
        |   |   |_java
        |   |   |   |_gfb
        |   |   |   |   |_actions
        |   |   |   |   |   |_UIActions.java
        |   |   |   |   |_models
        |   |   |   |   |   |_BingPageLocators.java
        |   |   |   |   |   |_GooglePageLocators.java
        |   |   |   |   |   |_SearchResultsDetails.java
        |   |   |   |   |   |...
        |   |   |   |   |_utils
        |   |   |   |   |   |_FileHelper.java
        |   |   |   |   |   |_SeleniumWebDriverUtils.java
        |   |   |   |   |   |...
        |   |   |_resources
        |   |   |   |_config.properties
        |   |   |   |_pageLocators.json
        |   |   |   |...
        |   |_test
        |   |   |_java
        |   |   |   |_gfbTest
        |   |   |   |   |_BaseTest.java
        |   |   |   |   |_SearchTest.java
        |   |   |   |   |...
        |   |_resources
        |   |   |   |_searchTestSuite.xml
        |   |   |   |...
        |_pom.xml
        |_README.md

### Running test
Go to your project directory from terminal and hit following commands:
- mvn -e test -Dtest.script=src/test/resources/searchTestSuite.xml -DbrowserType=chrome

Note: -Dtest.script is where the testNG xml file is. -DbrowserType is the parameter to run tests on chrome or firefox.

----

## Specifications
### Objectives
Task Summary: Automate 2 search engines (e.g. Google and another one per candidate's choice).

### Test case:
1. Run <browser>
2. Clear <browser> cookies
3. Go to the 1st search engine
4. Search using the <keyword>
5. Parse the first 10 search result items (e.g. url, title, short description): store parsed info as
   structured data of any chosen by you type (i.e. hash of hashes or array of hashes, whatever structure handy to be parsed). Exclude (don’t count on): nested search items, search engine suggestions, embedded videos, etc while working with search results.
6. Make sure at least one attribute of each item from parsed search results contains <keyword>
7. Log in stdout which search results items and their attributes contain <keyword> and which do not.
8. Repeat steps #3 - #7 for the 2nd search engine
9. Compare stored results for both engines and list out the most popular items (the ones which were
   found in both search engines)
   Note: Keep in mind - in some cases, search engines can block automated tests execution with CAPTCHA. Don't try to solve it automatically, just add a workaround, for example - look for CAPTCHA, add 1-minute sleep and solve it manually.

### Requirements:

1. Browser (at least Chrome and Firefox) and <keyword> should be configurable. The test should run with any combination of them.
2. Every action, every comparison result, etc should be logged accordingly (i.e. to stdout). Goal: when your script passes - detailed test-case steps should be logged in to STDOUT, so anybody can read it and repeat exactly the same steps and verifications but manually.
3. Your code should be well commented, so anybody can easily find out what action is being performed there and what is the purpose of those code blocks/methods/etc .
    Implementing and structuring your home task optimally will be a CRITICAL COMPONENT for our assessment of you as a candidate for a position at Upwork. Please invest the time and impress us!
    If your home task meets our expectations, we will have a follow up interview in which we will discuss it. We will assess your implementation according to the following:
4. Object-oriented programming style/structure of classes and associated files (i.e. more than a single class and file). Consider the use of inheritance or encapsulation.
5. Code is maintainable and allows for parallel contribution by team members.
6. Important: reviewers should be able to build (executed, compiled if needed) your code without any issues in their environment (code should run not only on the machine where you wrote it).
7. Code is not prone to issues.
8. Optimal use of element selectors, such that doesn’t require constant changes if page/HTML is
    updated.
9. Make the framework more scalable and reusable by envisioning how this would work when there
    are over 100 pages to work with. Assume that another team changes the UI or refactor some functionality, how would your framework/method adapt to this so there is a minimal effort in maintaining your script.