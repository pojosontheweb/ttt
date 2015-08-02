package com.pojosontheweb.ttt.stripes;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.controller.ActionResolver;
import net.sourceforge.stripes.controller.StripesFilter;
import net.sourceforge.stripes.util.StringUtil;
import net.sourceforge.stripes.util.UrlBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.Writer;

public class Form extends TagBase implements Tag<Form> {

    private final Class<? extends ActionBean> beanClass;

    public Form(
        Writer out,
        Class<? extends ActionBean> beanClass,
        boolean partial,
        Attributes attributes) {

        super(out, "form", attributes);
        this.beanClass = beanClass;

        UrlBuilder urlBuilder =
            new UrlBuilder(getRequest().getLocale(), getActionBeanUrl(beanClass), false).setEvent(null);

        String action = urlBuilder.toString();
        if (action.startsWith("/")) {
            HttpServletRequest request = getRequest();
            String contextPath = request.getContextPath();

            // *Always* prepend the context path if "beanclass" was used
            // Otherwise, *only* prepend it if it is not already present
            if (contextPath.length() > 1
                && (beanClass != null || !action.startsWith(contextPath + '/'))) {
                action = contextPath + action;
            }
        }
        HttpServletResponse response = getResponse();
        action = response.encodeURL(action);

        getAttributes().set("action", action);
    }

    public Class<? extends ActionBean> getBeanClass() {
        return beanClass;
    }

    protected String getActionBeanUrl(Object nameOrClass) {
        Class<? extends ActionBean> beanType = getActionBeanType(nameOrClass);
        if (beanType != null) {
            return StripesFilter.getConfiguration().getActionResolver().getUrlBinding(beanType);
        }
        else {
            return null;
        }
    }

    @Override
    public Form open() {
        writeTagOpen();
        return this;
    }

    @Override
    public void close() {
        writeTagClose();
    }

    public Text text(String field) {
        return new Text(this, field);
    }

    public Submit submit(String name, String value) {
        return new Submit(name, value, null);
    }

    ActionBean getActionBean() {
        String binding = getActionBeanUrlBinding();
        HttpServletRequest request = getRequest();
        ActionBean bean = (ActionBean) request.getAttribute(binding);
        if (bean == null) {
            HttpSession session = request.getSession(false);
            if (session != null)
                bean = (ActionBean) session.getAttribute(binding);
        }
        return bean;
    }

    protected String getActionBeanUrlBinding() {
        ActionResolver resolver = StripesFilter.getConfiguration().getActionResolver();
        return resolver.getUrlBinding(beanClass);
    }

    public static class Builder {
        private final Writer out;
        private final Class<? extends ActionBean> beanClass;

        private Attributes attributes = new Attributes();
        private boolean partial = false;

        public Builder(Writer out, Class<? extends ActionBean> beanClass) {
            this.out = out;
            this.beanClass = beanClass;
        }

        public Builder partial(boolean partial) {
            this.partial = partial;
            return this;
        }

        public Builder set(String name, String value) {
            attributes.set(name, value);
            return this;
        }

        public Form build() {
            return new Form(out, beanClass, partial, attributes).open();
        }
    }
}
