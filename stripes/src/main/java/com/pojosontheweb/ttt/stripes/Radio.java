package com.pojosontheweb.ttt.stripes;

import net.sourceforge.stripes.tag.FormTag;
import net.sourceforge.stripes.tag.InputRadioButtonTag;

import javax.servlet.jsp.PageContext;

public class Radio extends InputTagSupportBase<InputRadioButtonTag,Radio> {

    public Radio(PageContext pageContext, InputRadioButtonTag tag, FormTag parent) {
        super(pageContext, tag, parent);
    }

    public Radio setValue(Object value) {
        tag.setValue(value);
        return this;
    }

    public Radio setChecked(String checked) {
        tag.setChecked(checked);
        return this;
    }
}
