package com.pojosontheweb.ttt.stripes;

import net.sourceforge.stripes.tag.FormTag;
import net.sourceforge.stripes.tag.InputPasswordTag;

import javax.servlet.jsp.PageContext;

public class Password extends InputTagSupportBase<InputPasswordTag> {

    public Password(PageContext pageContext, InputPasswordTag tag, FormTag parent) {
        super(pageContext, tag, parent);
    }
}
