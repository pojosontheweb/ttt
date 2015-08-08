package com.pojosontheweb.ttt.jsptags;

import com.pojosontheweb.ttt.Template;
import com.pojosontheweb.ttt.TttWriter;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.Tag;

public class TagTemplate<T extends Tag, B extends BodyTag> extends Template {

    protected final T tag;

    public TagTemplate(PageContext pageContext, T tag, B parent) {
        this.tag = tag;
        tag.setPageContext(pageContext);
        if (parent != null) {
            tag.setParent(parent);
        }
    }

    public T getTag() {
        return tag;
    }

    @Override
    protected void doRender(TttWriter tw) throws Exception {
//        try {
            tag.doStartTag();
            tag.doEndTag();
//        } finally {
//            tag.release();
//        }
    }
}
