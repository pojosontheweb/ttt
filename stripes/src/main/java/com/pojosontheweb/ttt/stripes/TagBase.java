package com.pojosontheweb.ttt.stripes;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.util.Log;
import net.sourceforge.stripes.util.ReflectUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Writer;
import java.util.LinkedHashMap;

public abstract class TagBase<T extends TagBase<T>> implements Tag<T> {

    private static final Log log = Log.getInstance(TagBase.class);

    private final Writer out;
    private final String tagName;
    private final Attributes attributes;

    public TagBase(Writer out, String tagName, Attributes attributes) {
        this.out = out;
        this.tagName = tagName;
        this.attributes = attributes == null ? new Attributes() : attributes;
    }

    public Attributes attributes() {
        return attributes;
    }

    @SuppressWarnings("unchecked")
    protected T set(String name, String val) {
        attributes.set(name, val);
        return (T)this;
    }

    protected void write(String... strings) {
        try {
            for (String s : strings) {
                out.write(s);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void writeTagOpen() {
        write("<", tagName);
        if (attributes.size()>0) {
            write(" ", attributes.attrsToString());
        }
        write(">");
    }

    protected void writeTagClose() {
        write("</", tagName, ">");
    }

    protected HttpServletRequest getRequest() {
        return StripesTags.getRequest();
    }

    protected HttpServletResponse getResponse() {
        return StripesTags.getResponse();
    }

    @SuppressWarnings("unchecked")
    protected Class<? extends ActionBean> getActionBeanType(Object nameOrClass) {
        Class result = null;

        // Figure out if it's a String of Class (or something else?) and act appropriately
        if (nameOrClass instanceof String) {
            try {
                result = ReflectUtil.findClass((String) nameOrClass);
            }
            catch (ClassNotFoundException cnfe) {
                log.error(cnfe, "Could not find class of type: ", nameOrClass);
                return null;
            }
        }
        else if (nameOrClass instanceof Class) {
            result = (Class) nameOrClass;
        }
        else {
            log.error("The value supplied to getActionBeanType() was neither a String nor a " +
                "Class. Cannot infer ActionBean type from value: " + nameOrClass);
            return null;
        }

        // And for good measure, let's make sure it's an ActionBean implementation!
        if (ActionBean.class.isAssignableFrom(result)) {
            return result;
        }
        else {
            log.error("Class '", result.getName(), "' specified in tag does not implement ",
                "ActionBean.");
            return null;
        }
    }

}
