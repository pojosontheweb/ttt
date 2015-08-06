package com.pojosontheweb.ttt.stripes;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.controller.ExecutionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Writer;

public class StripesTags {

    private final Writer out;

    public StripesTags(Writer out) {
        this.out = out;
    }

    public Form.Builder form(Class<? extends ActionBean> beanClass) {
        return new Form.Builder(out, beanClass);
    }

    public Link.Builder link(Class<? extends ActionBean> beanClass) {
        return new Link.Builder(out, beanClass);
    }

    public Link.Builder link(String url) {
        return new Link.Builder(out, url);
    }

    public Format format(Object value) {
        return new Format(value);
    }

    public static HttpServletRequest getRequest() {
        return ExecutionContext.currentContext().getActionBeanContext().getRequest();
    }

    public static HttpServletResponse getResponse() {
        return ExecutionContext.currentContext().getActionBeanContext().getResponse();
    }

}
