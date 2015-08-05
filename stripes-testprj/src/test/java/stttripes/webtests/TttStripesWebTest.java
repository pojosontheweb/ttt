package stttripes.webtests;

import com.pojosontheweb.selenium.Findr;
import com.pojosontheweb.selenium.ManagedDriverJunit4TestBase;
import org.junit.Before;
import org.junit.Test;

import static com.pojosontheweb.selenium.Findrs.*;
import static org.openqa.selenium.By.*;

public class TttStripesWebTest extends ManagedDriverJunit4TestBase {

    static final String BASE_URL = System.getProperty("webtests.base.url", "http://localhost:8080/sttt");

    @Before
    public void navigateToHome() {
        get("/");
        findr()
            .elemList(tagName("h1"))
            .whereElemCount(1)
            .at(0)
            .where(textEquals("Stripes TTT test project"))
            .eval();
    }

    @Test
    public void simple() {
        clickLinkAndAssertSimpleText(
            "simple template (beanclass, no param)",
            "Hey, you have not provided no param. Try '?myProp=bar'..."
        );
        clickLinkAndAssertSimpleText(
            "simple template (beanclass, param)",
            "Cool ! this was bound : hello_world"
        );
        clickLinkAndAssertSimpleText(
            "simple template (url, no param)",
            "Hey, you have not provided no param. Try '?myProp=bar'..."
        );
        clickLinkAndAssertSimpleText(
            "simple template (url, param)",
            "Cool ! this was bound : hello_world"
        );
        clickLinkAndAssertSimpleText(
            "simple",
            "Hey, you have not provided no param. Try '?myProp=bar'..."
        );
    }

    private CalculatorPage calcPage() {
        return new CalculatorPage(findr());
    }

    @Test
    public void calculator() {
        clickLink("templated calculator");
        calcPage()
            .setNumberOne("11")
            .setNumberTwo("22")
            .clickAdd()
            .assertResult("33.0");

        calcPage()
            .setNumberOne(null)
            .clickAdd()
            .assertErrorNumberOne("Number One is a required field");

        calcPage()
            .setNumberTwo(null)
            .clickDivide()
            .assertErrorNumberOne("Number One is a required field")
            .assertErrorNumberTwo("Number Two is a required field");
    }

    private void clickLinkAndAssertSimpleText(String linkLabel, String expectedSimpleText) {
        clickLink(linkLabel);
        assertSimpleText(expectedSimpleText);
        clickBackToHome();
    }

    protected void get(String relPath) {
        getWebDriver().get(BASE_URL + relPath);
    }

    void clickLink(String label) {
        findr()
            .elemList(tagName("a"))
            .where(textEquals(label))
            .whereElemCount(1)
            .at(0)
            .click();
    }

    void assertSimpleText(String expected) {
        findr()
            .elemList(tagName("p"))
            .where(textEquals(expected))
            .whereElemCount(1)
            .at(0)
            .eval();
    }

    void clickBackToHome() {
        clickLink("home");
    }

}
