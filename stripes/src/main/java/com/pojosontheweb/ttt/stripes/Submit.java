package com.pojosontheweb.ttt.stripes;

import java.io.Writer;
import java.util.Map;

public class Submit extends HtmlTag.ButtonBase<Submit> {

    public Submit(Writer out, Form form, String name, String value, Map<String, String> attributes) {
        super(out, form, "input", "submit", name, value, attributes);
    }

}
