package stttripes.templates;

import com.pojosontheweb.ttt.stripes.StripesTags;
import net.sourceforge.stripes.validation.ValidationError;

import java.io.Writer;
import java.util.List;

/**
 * Tags entry point for the app.
 *
 * Provides access to the Stripes tag lib as well as
 * some custom tags of out own.
 */
public class MyTags {

    public final StripesTags stripes;

    /**
     * Creates instance for passed writer.
     * @param out the writer to render the tags to
     */
    public MyTags(Writer out) {
        // init stripes taglib
        stripes = new StripesTags(out);
    }

    /**
     * Create a row template : allows to reuse the table
     * markup for our demo form. This tag has a body,
     * and is used along with a try/resource block.
     *
     * @param label the field label
     * @param errors the list of errors if any
     * @return a row template for the field
     */
    public MyFormRow formRow(String label, List<ValidationError> errors) {
        MyFormRow r = new MyFormRow(label, errorsTemplate(errors));
        r.open(stripes.getWriter());
        return r;
    }

    /**
     * Create an errors template : formats passed errors as we want to
     * @param errors the errors to format if any
     * @return the errors template
     */
    public FieldErrorsTemplate errorsTemplate(List<ValidationError> errors) {
        return new FieldErrorsTemplate(errors);
    }

}
