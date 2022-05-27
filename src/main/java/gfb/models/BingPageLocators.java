package gfb.models;

import lombok.Getter;

/*This enum is to store all the DOM elements of Bing needed to run the tests*/
@Getter
public enum BingPageLocators {

    SEARCH_TEXTBOX("sb_form_q", BingPageLocators.ID),
    SEARCH_RESULTS("//li[@class='b_algo']", BingPageLocators.XPATH);
    //multiple usage of same strings
    private static final String XPATH = "xpath";
    private static final String ID = "id";
    private final String element;
    private final String elementType;

    BingPageLocators(String element, String elementType) {
        this.element = element;
        this.elementType = elementType;
    }
}
