package stttripes.webtests;

import com.pojosontheweb.selenium.Findr;

import static com.pojosontheweb.selenium.Findrs.*;
import static org.openqa.selenium.By.*;

public class InputsPage {

    private final Findr findr;

    public InputsPage(Findr findr) {
        this.findr = findr;
    }

    private Findr form() {
        return findr.elem(cssSelector("form.my-form"));
    }

    public InputsPage clickDoStuff() {
        form()
            .elemList(tagName("input"))
            .where(attrEquals("name", "doStuff"))
            .where(attrEquals("type", "submit"))
            .whereElemCount(1)
            .at(0)
            .click();
        return this;
    }

    public InputsPage clickReset() {
        form()
            .elemList(tagName("input"))
            .where(attrEquals("type", "submit"))
            .where(attrEquals("name", "reset"))
            .whereElemCount(1)
            .at(0)
            .click();
        return this;
    }

    public InputsPage assertMessage(String expextedMessage) {
        findr
            .elem(cssSelector("ul.messages"))
            .elemList(tagName("li"))
            .where(textEquals(expextedMessage))
            .whereElemCount(1)
            .eval();
        return this;
    }

    public InputsPage assertError(int index, String expectedError) {
        findr
            .elem(tagName("ol"))
            .elemList(tagName("li"))
            .at(index)
            .where(textEquals(expectedError))
            .eval();
        return this;
    }

    public InputsPage assertFieldError(String fieldName) {
        form()
            .elemList(tagName("input"))
            .where(attrEquals("name", fieldName))
            .where(hasClass("error"))
            .eval();
        return this;
    }

}
