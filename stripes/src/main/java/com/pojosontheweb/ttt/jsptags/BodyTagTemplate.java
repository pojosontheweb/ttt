package com.pojosontheweb.ttt.jsptags;

import com.pojosontheweb.ttt.IBodyTemplate;
import com.pojosontheweb.ttt.ITemplate;
import com.pojosontheweb.ttt.TttWriter;

import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.Tag;

import java.io.Writer;

import static com.pojosontheweb.ttt.Util.toRtEx;

public class BodyTagTemplate<T extends BodyTag> implements IBodyTemplate, ITemplate {

    protected final TttPageContext pageContext;
    protected final T bodyTag;

    protected TttBodyContent bodyContent;
    protected TttWriter out;

    private boolean opened = false;
    private boolean closed = false;

    public BodyTagTemplate(TttPageContext pageContext, T bodyTag) {
        this.pageContext = pageContext;
        this.bodyTag = bodyTag;
    }

    @Override
    public void open(TttWriter out) {
        if (opened) {
            // already opened, do nothing...
            return;
        }
        opened = true;
        this.out = out;
        toRtEx(() -> {
            bodyTag.setPageContext(pageContext);

            // open the tag
            int openRes = bodyTag.doStartTag();
            switch (openRes) {
                case Tag.SKIP_BODY:
                    // don't eval body, call end tag
                    int endRes = bodyTag.doEndTag();
                    switch (endRes) {
                        case Tag.SKIP_PAGE:
                            // supposed to skip the rest of the page...
                            // TODO
                            throw new UnsupportedOperationException("SKIP_PAGE is not supported");
                        case Tag.EVAL_PAGE:
                            // continue normally...
                            break;
                    }
                    break;

                case Tag.EVAL_BODY_INCLUDE:
                    // evaluate content
                    break;

                case BodyTag.EVAL_BODY_BUFFERED:

                    // push body content
                    bodyContent = pageContext.pushBody();
                    bodyTag.setBodyContent(bodyContent);
                    bodyTag.doInitBody();

                    // update the current writer used by the template
                    out.push(bodyContent);

                    // continue body : anything that the template
                    // outputs now gets to bodyContent
                    break;
            }
        });
    }

    @Override
    public void close() {
        if (closed) {
            // already closed, do nothing...
            return;
        }
        closed = true;

        try {
            if (bodyContent != null) {
                pageContext.popBody();
                out.pop();
                int res3 = bodyTag.doAfterBody();
                switch (res3) {
                    case BodyTag.EVAL_BODY_AGAIN:
                        // TODO
                        throw new UnsupportedOperationException("EVAL_BODY_AGAIN is not supported");

                    case BodyTag.SKIP_BODY:
                        bodyTag.doEndTag();
                        break;
                }
            } else {
                bodyTag.doAfterBody();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
//        } finally {
//            bodyTag.release();
        }
    }

    public T getBodyTag() {
        return bodyTag;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public void render(Writer out) {
        open((TttWriter)out);
        close();
    }
}
