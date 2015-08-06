package com.pojosontheweb.ttt.stripes;

public class Password extends HtmlTag.InputWithoutBody {

    public Password(Form form, String name) {
        super(new InputDelegate("input", form, "password", name), null);
    }
}
