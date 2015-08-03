package com.pojosontheweb.ttt.stripes;

import net.sourceforge.stripes.action.ActionBean;

import java.io.Writer;

public class Tags {

    private final Writer out;

    public Tags(Writer out) {
        this.out = out;
    }

    public Form.Builder form(Class<? extends ActionBean> beanClass) {
        return new Form.Builder(out, beanClass);
    }
}
