package stttripes.webtests;

import com.pojosontheweb.selenium.Findr;
import com.pojosontheweb.selenium.formz.Select;
import stttripes.actions.MyEnum;

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

    public InputsPage assertMessage(int index, String expectedMessage) {
        findr
            .elem(cssSelector("ul.messages"))
            .elemList(tagName("li"))
            .at(index)
            .where(textEquals(expectedMessage))
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
            .whereElemCount(1)
            .at(0)
            .eval();
        return this;
    }

    private InputsPage clickCheckbox(String name) {
        findr.elemList(tagName("input"))
            .where(attrEquals("type", "checkbox"))
            .where(attrEquals("name", name))
            .whereElemCount(1)
            .at(0)
            .click();
        return this;

    }

    public InputsPage clickCheckbox1() {
        return clickCheckbox("checkbox1");
    }

    public InputsPage clickCheckbox2() {
        return clickCheckbox("checkbox2");
    }

    private Findr findSelect(String name) {
        return findr.elemList(tagName("select"))
            .where(attrEquals("name", name))
            .whereElemCount(1)
            .at(0);
    }

    private InputsPage setSelect(String name, String value) {
        new Select(findSelect(name)).selectByVisibleText(value);
        return this;
    }

    public InputsPage selectCollection(String value) {
        return setSelect("textFromSelect", value);
    }

    public InputsPage selectCollectionObj(String value) {
        return setSelect("myObjId", value);
    }

    public InputsPage selectEnum(MyEnum e) {
        return setSelect("myEnum", e.name());
    }

    public InputsPage selectMap(String s) {
        return setSelect("fromSelectMap", s);
    }

    public InputsPage clickRadio(MyEnum e) {
        findr.elemList(tagName("input"))
            .where(attrEquals("type", "radio"))
            .where(attrEquals("name", "myEnumRadio"))
            .where(attrEquals("value", e.name()))
            .whereElemCount(1)
            .at(0)
            .click();
        return this;
    }

    public InputsPage clickButtonWithLabel(String value) {
        findr.elemList(tagName("input"))
            .where(attrEquals("type", "button"))
            .where(attrEquals("value", value))
            .whereElemCount(1)
            .at(0)
            .click();
        return this;
    }

    public InputsPage setText(String text) {
        Findr fText = findr.elemList(tagName("input"))
            .where(attrEquals("type", "text"))
            .where(attrEquals("name", "text"))
            .whereElemCount(1)
            .at(0);

        fText.clear();
        fText.sendKeys(text);
        return this;
    }

    public InputsPage setPassword(String password) {
        Findr fPass = findr.elemList(tagName("input"))
            .where(attrEquals("type", "password"))
            .where(attrEquals("name", "password"))
            .whereElemCount(1)
            .at(0);

        fPass.clear();
        fPass.sendKeys(password);
        return this;
    }

    public InputsPage setTextArea(String s) {
        Findr fTextarea = findr.elemList(tagName("textarea"))
            .where(attrEquals("name", "myTextArea"))
            .whereElemCount(1)
            .at(0);
        fTextarea.clear();
        fTextarea.sendKeys(s);
        return this;
    }
}
