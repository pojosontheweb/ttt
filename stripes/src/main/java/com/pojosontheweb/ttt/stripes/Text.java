package com.pojosontheweb.ttt.stripes;

import net.sourceforge.stripes.tag.FormTag;
import net.sourceforge.stripes.tag.InputTextTag;

import javax.servlet.jsp.PageContext;

public class Text extends InputTagSupportBase<InputTextTag> {

    public Text(PageContext pageContext, InputTextTag tag, FormTag parent) {
        super(pageContext, tag, parent);
    }

}
