package com.pojosontheweb.ttt.stripes;

import com.pojosontheweb.ttt.ITemplate;
import net.sourceforge.stripes.action.StreamingResolution;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

public class TemplateResolution extends StreamingResolution {

    public TemplateResolution(ITemplate template) {
        // TODO use template.getContentType when available
        super("text/html", asReader(template));
    }

    private static Reader asReader(ITemplate template) {
        try {
            StringWriter out = new StringWriter();
            try {
                template.render(out);
            } finally {
                out.close();
            }
            return new StringReader(out.toString());
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

}
