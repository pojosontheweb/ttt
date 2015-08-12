package stttripes.webtests;

import com.pojosontheweb.selenium.ManagedDriverJunit4TestBase;
import org.junit.Before;
import org.junit.Test;
import stttripes.actions.MyEnum;

import java.util.stream.IntStream;

import static com.pojosontheweb.selenium.Findrs.*;
import static org.openqa.selenium.By.*;

public class TttStripesIT extends ManagedDriverJunit4TestBase {

    static final String BASE_URL = System.getProperty("webtests.base.url", "http://localhost:8080/sttt");

    // navigate to home page before the test
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

        // encoding test
        clickLink("simple");
        findr().elem(cssSelector("p.encoding-test"))
            .where(textEquals("être ou ne pas être encodé, là est la question..."))
            .eval();
    }

    @Test
    public void calculator() {
        clickLink("templated calculator");
        calcPage()
            .setNumberOne("11")
            .setNumberTwo("22")
            .clickAdd()
            .assertResult("33");

        calcPage()
            .setNumberOne(null)
            .clickAdd()
            .assertErrorNumberOne("Number One is a required field");

        calcPage()
            .setNumberTwo(null)
            .clickDivide()
            .assertErrorNumberOne("Number One is a required field")
            .assertErrorNumberTwo("Number Two is a required field");

        calcPage()
            .setNumberOne("123")
            .setNumberTwo("0")
            .clickDivide()
            .assertErrorNumberTwo("Dividing by zero is not allowed.");

        calcPage()
            .setNumberTwo("456")
            .clickDivide()
            .assertResult("0.27");

        clickBackToHome();
    }

    @Test
    public void inputs() {

        clickLink("various inputs");
        InputsPage inputsPage = new InputsPage(findr());

        inputsPage
            .clickDoStuff()
            .assertError(0, "Text is a required field")
            .assertFieldError("text");

        inputsPage
            .clickReset()
            .assertMessage(0, "Reset !");

        inputsPage
            .setText("hey there !")
            .setPassword("secret")
            .clickCheckbox1()
            .clickCheckbox2()
            .selectCollection("bar")
            .selectCollectionObj("baz")
            .selectEnum(MyEnum.Good)
            .selectMap("foo")
            .clickRadio(MyEnum.Excellent)
            .clickButtonWithLabel("with body")
            .clickButtonWithLabel("I am localized")
            .clickButtonWithLabel("I'm in a custom tag !")
            .setTextArea("rock'n'roll")
            .clickDoStuff();

        String[] expectedMessages = new String[]{
            "Stuff's been done.",
            "text=hey there !",
            "password=secret",
            "checkbox1=true",
            "checkbox2=true",
            "textFromSelect=bar",
            "myObjId=3",
            "myEnum=Good",
            "fromSelectMap=1",
            "myEnumRadio=Excellent",
            "myFile=null",
            "myHidden=I am hidden",
            "myTextArea=rock'n'roll"
        };

        IntStream.range(0, expectedMessages.length)
            .forEach(i -> inputsPage.assertMessage(i, expectedMessages[i]));
    }

    //
    // helper methods
    //

    private CalculatorPage calcPage() {
        return new CalculatorPage(findr());
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
