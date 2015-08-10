package stttripes.templates;

import com.pojosontheweb.ttt.jsptags.TagLib;
import net.sourceforge.stripes.validation.ValidationError;

import java.io.Writer;
import java.util.List;

public class MyTags extends TagLib {

    public MyTags(Writer out) {
        super(out);
    }

    public MyFormRow formRow(String label, List<ValidationError> errors) {
        MyFormRow r = new MyFormRow(label, new FieldErrorsTemplate(errors));
        r.open(out);
        return r;
    }

}
