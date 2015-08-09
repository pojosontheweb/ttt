package stttripes.webtests;

import com.pojosontheweb.selenium.Findr;

import static com.pojosontheweb.selenium.Findrs.*;
import static org.openqa.selenium.By.*;

/**
 * Created by vankeisb on 09/08/15.
 */
public class InputsPage {

    private final Findr findr;

    public InputsPage(Findr findr) {
        this.findr = findr;
    }


    public InputsPage clickDoStuff() {
        findr.elemList(tagName("input"))
            .where(attrEquals("name", "doStuff"))
            .where(attrEquals("type", "submit"))
            .whereElemCount(1)
            .at(0)
            .click();
        return this;
    }

    public InputsPage assertError(int index, String expectedError) {
        findr.elem(tagName("ol"))
            .elemList(tagName("li"))
            .at(index)
            .where(textEquals(expectedError))
            .eval();
        return this;
    }

    public InputsPage assertFieldError(String fieldName) {
        findr.elemList(tagName("input"))
            .where(attrEquals("name", fieldName))
            .where(hasClass("error"))
            .eval();
        return this;
    }

    public InputsPage clickReset() {
        findr.elemList(tagName("input"))
            .where(attrEquals("type", "submit"))
            .where(attrEquals("name", "reset"))
            .whereElemCount(1)
            .at(0)
            .click();
        return this;
    }

    public InputsPage assertMessage(String expextedMessage) {
        findr.elem(cssSelector("ul.messages"))
            .elemList(tagName("li"))
            .where(textEquals(expextedMessage))
            .whereElemCount(1)
            .eval();
        return this;
    }
}
