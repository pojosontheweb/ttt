package stttripes.action;

import com.pojosontheweb.ttt.stripes.TemplateResolution;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.*;
import stttripes.templates.CalculatorTemplate;

/**
 * A very simple calculator action.
 * @author Tim Fennell
 */
@UrlBinding("/calc")
public class CalculatorActionBean implements ActionBean, ValidationErrorHandler {
    private ActionBeanContext context;
    @Validate(required=true) private double numberOne;
    @Validate(required=true) private double numberTwo;
    private double result;

    public ActionBeanContext getContext() { return context; }
    public void setContext(ActionBeanContext context) { this.context = context; }

    public double getNumberOne() { return numberOne; }
    public void setNumberOne(double numberOne) { this.numberOne = numberOne; }

    public double getNumberTwo() { return numberTwo; }
    public void setNumberTwo(double numberTwo) { this.numberTwo = numberTwo; }

    public double getResult() { return result; }
    public void setResult(double result) { this.result = result; }

    @DefaultHandler
    @DontBind
    public Resolution display() {
        return new TemplateResolution(new CalculatorTemplate(this));
    }

    /** An event handler method that adds number one to number two. */
    public Resolution addition() {
        result = numberOne + numberTwo;
        return display();
    }

    /** An event handler method that divides number one by number two. */
    public Resolution division() {
        result = numberOne / numberTwo;
        return display();
    }

    // TODO
    // we need this otherwise _sourcePage is used and
    // we have a infinite loop bug...
    @Override
    public Resolution handleValidationErrors(ValidationErrors errors) throws Exception {
        return display();
    }

    /**
     * An example of a custom validation that checks that division operations
     * are not dividing by zero.
     */
    @ValidationMethod(on="division")
    public void avoidDivideByZero(ValidationErrors errors) {
        if (this.numberTwo == 0) {
            errors.add("numberTwo", new SimpleError("Dividing by zero is not allowed."));
        }
    }

}
