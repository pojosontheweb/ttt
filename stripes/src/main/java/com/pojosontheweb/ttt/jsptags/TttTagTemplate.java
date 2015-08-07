package com.pojosontheweb.ttt.jsptags;

import com.pojosontheweb.ttt.Template;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import java.io.IOException;
import java.io.Writer;

public class TttTagTemplate extends Template {

    private final Tag tag;

    public TttTagTemplate(Tag tag) {
        this.tag = tag;
        this.pageContext = pageContext;
    }

    @Override
    public void render(Writer out) throws IOException {
        // manufacture PageContext
        TttPageContext ctx =
        TttTagTree.handleNode();


    }
}
