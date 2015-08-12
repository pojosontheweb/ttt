package com.pojosontheweb.ttt.stripes;

import com.pojosontheweb.ttt.jsptags.TagTemplate;
import net.sourceforge.stripes.tag.MessagesTag;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTag;

public class Messages extends TagTemplate<MessagesTag, BodyTag> {

    public Messages(PageContext pageContext, MessagesTag tag) {
        super(pageContext, tag, null);
    }
}
