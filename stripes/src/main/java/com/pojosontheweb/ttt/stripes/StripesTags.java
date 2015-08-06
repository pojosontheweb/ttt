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

    //
    // s:url
    //

    public Url url(Class<? extends ActionBean> beanClass) {
        return new Url(beanClass);
    }

    public Url url(String url) {
        return new Url(url);
    }

    //
    // s:link
    //

    public Link link(Url url) {
        return new Link(out, url, null);
    }

    public Link link(String url) {
        return link(url(url));
    }

    public Link link(Class<? extends ActionBean> beanClass) {
        return link(url(beanClass));
    }


    //
    // s:form
    //

    public Form form(Class<? extends ActionBean> beanClass) {
        return new Form(out, beanClass, false, null, null);
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
