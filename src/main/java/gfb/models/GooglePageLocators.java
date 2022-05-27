package gfb.models;

import lombok.Getter;

/*This enum is to store all the DOM elements of Google needed to run the tests*/
@Getter
public enum GooglePageLocators {

    SEARCH_TEXTBOX("//input[@title='Search']", GooglePageLocators.XPATH),
    SEARCH_RESULTS("//div[@id='search']//div[@data-sokoban-container]", GooglePageLocators.XPATH);
    //multiple usage of same strings
    private static final String XPATH = "xpath";
    private final String element;
    private final String elementType;

    GooglePageLocators(String element, String elementType) {
        this.element = element;
        this.elementType = elementType;
    }
}
