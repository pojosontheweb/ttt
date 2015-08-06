package com.pojosontheweb.ttt.stripes;

import java.io.Writer;

public class Submit extends HtmlTag.ButtonBase<Submit> {

    public Submit(Writer out, Form form, String name) {
        super(out, form, "input", "submit", name);
    }

}
