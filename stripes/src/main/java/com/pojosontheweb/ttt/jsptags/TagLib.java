package com.pojosontheweb.ttt.jsptags;

import com.pojosontheweb.ttt.TttWriter;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.controller.ExecutionContext;

import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.Tag;
import java.io.Writer;

public class TagLib {

    protected final TttPageContext pageContext;
    protected final TttWriter out;

    public TagLib(Writer out) {
        this.out = (TttWriter)out;
        ExecutionContext ec = ExecutionContext.currentContext();
        ActionBeanContext context = ec.getActionBeanContext();
        pageContext = new TttPageContext(out, context.getServletContext(), context.getRequest(), context.getResponse());
    }

    public <T extends Tag, B extends BodyTag> TagTemplate<T,B> wrap(T tag, B parentTag) {
        return new TagTemplate<>(pageContext, tag, parentTag);
    }

}
