package com.pojosontheweb.ttt.stripes;

import net.sourceforge.stripes.tag.FormTag;
import net.sourceforge.stripes.tag.InputHiddenTag;

import javax.servlet.jsp.PageContext;

public class Hidden extends InputTagSupportBase<InputHiddenTag,Hidden> {

    public Hidden(PageContext pageContext, InputHiddenTag tag, FormTag parent) {
        super(pageContext, tag, parent);
    }

}
