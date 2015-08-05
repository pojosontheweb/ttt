package stttripes.webtests;

import com.pojosontheweb.selenium.Findr;

import static com.pojosontheweb.selenium.Findrs.attrEquals;
import static com.pojosontheweb.selenium.Findrs.textEquals;
import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.By.tagName;

public class CalculatorPage {

    private final Findr findr;

    public CalculatorPage(Findr findr) {
        this.findr = findr;
    }

    private Findr findr() {
        return findr;
    }

    public CalculatorPage assertResult(String s) {
        findr().elem(cssSelector("td.result")).where(textEquals(s)).eval();
        return this;
    }

    public CalculatorPage setNumberOne(String text) {
        numberOne().clear();
        numberOne().sendKeys(text);
        return this;
    }

    public CalculatorPage setNumberTwo(String text) {
        numberTwo().clear();
        numberTwo().sendKeys(text);
        return this;
    }

    private Findr numberOne() {
        return input("text", "numberOne");
    }

    private Findr numberTwo() {
        return input("text", "numberTwo");
    }

    public CalculatorPage clickAdd() {
        input("submit", "addition").click();
        return this;
    }

    public CalculatorPage clickDivide() {
        input("submit", "division").click();
        return this;
    }

    private Findr input(String type, String name) {
        return findr()
            .elemList(tagName("input"))
            .where(attrEquals("type", type))
            .where(attrEquals("name", name))
            .whereElemCount(1)
            .at(0);
    }

}
