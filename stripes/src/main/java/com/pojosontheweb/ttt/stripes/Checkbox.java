package com.pojosontheweb.ttt.stripes;

import net.sourceforge.stripes.tag.FormTag;
import net.sourceforge.stripes.tag.InputCheckBoxTag;

import javax.servlet.jsp.PageContext;

public class Checkbox extends InputTagSupportBase<InputCheckBoxTag,Checkbox> {

    public Checkbox(PageContext pageContext, InputCheckBoxTag tag, FormTag parent) {
        super(pageContext, tag, parent);
    }

    public Checkbox setChecked(Object checked) {
        tag.setChecked(checked);
        return this;
    }


}
