package com.pojosontheweb.ttt.stripes;

import com.pojosontheweb.ttt.jsptags.BodyTagTemplate;
import com.pojosontheweb.ttt.jsptags.TttPageContext;
import net.sourceforge.stripes.tag.LinkTag;

public class Link extends BodyTagTemplate<LinkTag> {

    public Link(TttPageContext pageContext, LinkTag bodyTag) {
        super(pageContext, bodyTag);
    }

    public Link addParameter(String name, String value) {
        bodyTag.addParameter(name, value);
        return this;
    }
}
