package stttripes.templates;

import com.pojosontheweb.ttt.IBodyTemplate;
import com.pojosontheweb.ttt.ITemplate;
import com.pojosontheweb.ttt.Template;
import com.pojosontheweb.ttt.TttWriter;
import com.pojosontheweb.ttt.jsptags.BodyTagTemplate;
import com.pojosontheweb.ttt.jsptags.TttBodyContent;

import java.io.StringWriter;
import java.io.Writer;

import static com.pojosontheweb.ttt.Util.toRtEx;

public class MyFormRow extends Template implements IBodyTemplate {

    private final String label;
    private final FieldErrorsTemplate errors;

    private TttWriter out;

    public MyFormRow(String label, FieldErrorsTemplate errors) {
        this.label = label;
        this.errors = errors;
    }

    @Override
    public void open(TttWriter out) {
        this.out = out;
        write(out, "<tr>");
        write(out, "<td>", label, "</td>");
        write(out, "<td>");
        out.push(new StringWriter());
    }

    @Override
    public void close() throws Exception {
        StringWriter sw = (StringWriter)out.pop();
        write(out, sw.toString());
        write(out, "</td>");
        write(out, "<td>", errors, "</td>");
        write(out, "</tr>");
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    protected void doRender(TttWriter tw) throws Exception {
        open(out);
        close();
    }

}
