package com.pojosontheweb.ttt.stripes;

import com.pojosontheweb.ttt.jsptags.TagTemplate;
import net.sourceforge.stripes.tag.LinkTagSupport;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTag;

public abstract class LinkTagSupportBase<T extends LinkTagSupport, P extends BodyTag> extends TagTemplate<T,P> {

    public LinkTagSupportBase(PageContext pageContext, T tag, P parent) {
        super(pageContext, tag, parent);
    }
}
