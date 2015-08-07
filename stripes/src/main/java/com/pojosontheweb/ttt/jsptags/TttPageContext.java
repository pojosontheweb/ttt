package com.pojosontheweb.ttt.jsptags;

import com.mockobjects.servlet.MockPageContext;

import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.el.ExpressionEvaluator;
import javax.servlet.jsp.el.VariableResolver;
import javax.servlet.jsp.tagext.BodyContent;
import java.io.IOException;
import java.util.Stack;

import static com.pojosontheweb.ttt.Util.toRtEx;

public class TttPageContext extends MockPageContext {

    private ServletResponse response;

    @Override
    public void include(String relativeUrlPath, boolean flush) throws ServletException, IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public ExpressionEvaluator getExpressionEvaluator() {
        return null;
    }

    @Override
    public VariableResolver getVariableResolver() {
        return null;
    }

    @Override
    public javax.el.ELContext getELContext() {
        return null;
    }

    @Override
    public ServletResponse getResponse() {
        return response;
    }

    public void setResponse(ServletResponse response) {
        this.response = response;
    }

    private Stack<TttBodyContent> bodyStack = new Stack<>();

    @Override
    public BodyContent pushBody() {
        TttBodyContent bodyContent = new TttBodyContent(getOut());
        bodyStack.push(bodyContent);
        setJspWriter(bodyContent);
        return bodyContent;
    }

    @Override
    public JspWriter popBody() {
        BodyContent c = bodyStack.pop();
        toRtEx(c::close);
        setJspWriter(c.getEnclosingWriter());
        return c.getEnclosingWriter();
    }
}
