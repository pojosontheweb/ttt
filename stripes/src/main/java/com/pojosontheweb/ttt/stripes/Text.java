package com.pojosontheweb.ttt.stripes;

public class Text extends HtmlTag.InputWithoutBody {

    public Text(Form form, String name) {
        super(new InputDelegate("input", form, "text", name), null);
    }
}
