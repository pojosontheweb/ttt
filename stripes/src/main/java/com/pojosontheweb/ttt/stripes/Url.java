package com.pojosontheweb.ttt.stripes;

import com.pojosontheweb.ttt.jsptags.TagTemplate;
import net.sourceforge.stripes.tag.UrlTag;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTag;

public class Url extends TagTemplate<UrlTag, BodyTag> {

    public Url(PageContext pageContext, UrlTag tag) {
        super(pageContext, tag, null);
    }
}
