package com.pojosontheweb.ttt.stripes2;

import com.pojosontheweb.ttt.jsptags.TagTemplate;
import net.sourceforge.stripes.tag.FormTag;
import net.sourceforge.stripes.tag.InputSubmitTag;

import javax.servlet.jsp.PageContext;

public class Submit extends TagTemplate<InputSubmitTag,FormTag> {

    public Submit(PageContext pageContext, InputSubmitTag tag, FormTag parent) {
        super(pageContext, tag, parent);
    }
}
