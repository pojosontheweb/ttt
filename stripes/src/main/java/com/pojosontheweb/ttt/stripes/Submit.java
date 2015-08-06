package com.pojosontheweb.ttt.stripes;

import java.io.Writer;

public class Submit extends HtmlTag.ButtonBase {

    Submit(Writer out, Form form, String name) {
        super(out, form, "input", "submit", name, null, null);
    }

    public Submit(Writer out, Form form, String name, String value, Attributes attributes) {
        super(out, form, "input", "submit", name, value, attributes);

    }

    public Submit setValue(String value) {
        return new Submit(out, form, name, value, attributes);
    }

}
