package com.pojosontheweb.ttt.stripes2;

import com.pojosontheweb.ttt.jsptags.TagTemplate;
import net.sourceforge.stripes.tag.FormTag;
import net.sourceforge.stripes.tag.InputTextTag;

import javax.servlet.jsp.PageContext;

public class Text extends TagTemplate<InputTextTag,FormTag> {

    public Text(PageContext pageContext, InputTextTag tag, FormTag parent) {
        super(pageContext, tag, parent);
    }
}
