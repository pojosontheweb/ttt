package com.pojosontheweb.ttt.stripes;

import com.pojosontheweb.ttt.jsptags.TagTemplate;
import net.sourceforge.stripes.tag.ErrorsTag;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTag;

public class Errors extends TagTemplate<ErrorsTag,BodyTag> {

    public Errors(PageContext pageContext, ErrorsTag tag) {
        super(pageContext, tag, null);
    }
}
