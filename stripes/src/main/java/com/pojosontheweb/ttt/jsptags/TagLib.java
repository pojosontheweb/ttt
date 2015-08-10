package com.pojosontheweb.ttt.jsptags;

import com.pojosontheweb.ttt.TttWriter;
import com.pojosontheweb.ttt.Util;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.controller.ExecutionContext;

import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.Tag;
import java.io.Writer;
import java.nio.charset.Charset;

public class TagLib {

    protected final TttPageContext pageContext;
    protected final TttWriter out;
    protected final Charset encoding;

    public TagLib(Writer out) {
        this(out, null);
    }

    public TagLib(Writer out, Charset encoding) {
        this.out = (TttWriter)out;
        this.encoding = Util.getCharsetWithDefault(encoding);
        ExecutionContext ec = ExecutionContext.currentContext();
        ActionBeanContext context = ec.getActionBeanContext();
        pageContext = new TttPageContext(encoding, out, context.getServletContext(), context.getRequest(), context.getResponse());
    }

    public <T extends Tag, B extends BodyTag> TagTemplate<T,B> wrap(T tag, B parentTag) {
        return new TagTemplate<>(pageContext, tag, parentTag);
    }

}
