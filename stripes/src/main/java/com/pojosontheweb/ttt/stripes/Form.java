package com.pojosontheweb.ttt.stripes;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.controller.ActionResolver;
import net.sourceforge.stripes.controller.StripesConstants;
import net.sourceforge.stripes.controller.StripesFilter;
import net.sourceforge.stripes.tag.PageOptionsTag;
import net.sourceforge.stripes.util.CryptoUtil;
import net.sourceforge.stripes.util.HtmlUtil;
import net.sourceforge.stripes.util.StringUtil;
import net.sourceforge.stripes.util.UrlBuilder;
import net.sourceforge.stripes.validation.ValidationError;
import net.sourceforge.stripes.validation.ValidationErrors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.Writer;
import java.util.*;

public class Form extends HtmlTag.WithBody {

    private final Class<? extends ActionBean> beanClass;
    private final String actionWithoutContext;
    private final boolean partial;

    // TODO handle wizzard...

//    private Map<String,Class<?>> fieldsPresent = new HashMap<String,Class<?>>();

    Form(
        Writer out,
        Class<? extends ActionBean> beanClass,
        String actionWithoutContext,
        boolean partial,
        String method,
        Attributes attributes) {

        super(out, "form", attributes);

        assert beanClass != null || actionWithoutContext != null;

        this.beanClass = beanClass;
        this.partial = partial;
        this.actionWithoutContext = actionWithoutContext;

        UrlBuilder urlBuilder =
            new UrlBuilder(getRequest().getLocale(), Url.getActionBeanUrl(beanClass), false).setEvent(null);

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

        this.attributes = this.attributes
            .set("action",  response.encodeURL(action))
            .set("method", method != null ? method : "POST");
    }

    @Override
    public Form open() {
        if (!partial) {
            return (Form)super.open();
        }
        return this;
    }

    @Override
    public void close() {
        if (!partial) {
            writeHiddenTags();
            super.close();
        }
    }

    public Class<? extends ActionBean> getBeanClass() {
        return beanClass;
    }

    private void write(String s, String... strs) {
        write(out, s, strs);
    }

    protected void writeHiddenTags() {
        write("<div style=\"display: none;\">");
        writeSourcePageHiddenField();
//        if (isWizard()) {
//            writeWizardFields();
//        }
        writeFieldsPresentHiddenField();
        write("</div>");
    }

    protected boolean isXmlTags() {
        return !"html".equalsIgnoreCase(getHtmlMode());
    }

    private String getHtmlMode() {
        return StripesFilter.getConfiguration().getBootstrapPropertyResolver().getProperty(PageOptionsTag.CFG_KEY_HTML_MODE);
    }

    protected void writeFieldsPresentHiddenField() {
        write("<input type=\"hidden\" name=\"");
        write(StripesConstants.URL_KEY_FIELDS_PRESENT);
        write("\" value=\"");
        write(getFieldsPresentValue());
        write(isXmlTags() ? "\" />" : "\">");
    }

    protected String getFieldsPresentValue() {
        // Figure out what set of names to include
        Set<String> namesToInclude = new HashSet<String>();

        // TODO
//        if (isWizard()) {
//            namesToInclude.addAll(this.fieldsPresent.keySet());
//        }
//        else {
//            for (Map.Entry<String,Class<?>> entry : this.fieldsPresent.entrySet()) {
//                Class<?> fieldClass = entry.getValue();
//                if (InputSelectTag.class.isAssignableFrom(fieldClass)
//                    || InputCheckBoxTag.class.isAssignableFrom(fieldClass)) {
//                    namesToInclude.add(entry.getKey());
//                }
//            }
//        }

        // Combine the names into a delimited String and encrypt it
        String hiddenFieldValue = HtmlUtil.combineValues(namesToInclude);
        return CryptoUtil.encrypt(hiddenFieldValue);
    }


    protected void writeSourcePageHiddenField() {
        write("<input type=\"hidden\" name=\"");
        write(StripesConstants.URL_KEY_SOURCE_PAGE);
        write("\" value=\"");
        write(getSourcePageValue());
        write(isXmlTags() ? "\" />" : "\">");
    }

    protected String getSourcePageValue() {
        return CryptoUtil.encrypt(getRequest().getServletPath());
    }

    public ActionBean getActionBean() {
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

    public String getAction() {
        return actionWithoutContext;
    }

    protected String getActionBeanUrlBinding() {
        ActionResolver resolver = StripesFilter.getConfiguration().getActionResolver();
        if (beanClass == null) {
            String path = StringUtil.trimFragment(this.actionWithoutContext);
            String binding = resolver.getUrlBindingFromPath(path);
            if (binding == null)
                binding = path;
            return binding;
        }
        else {
            return resolver.getUrlBinding(beanClass);
        }
    }

    public List<ValidationError> getErrors(String fieldName) {
        ValidationErrors errs = getActionBean().getContext().getValidationErrors();
        List<ValidationError> fieldErrs = errs.get(fieldName);
        return fieldErrs != null ? fieldErrs : Collections.emptyList();
    }

    public boolean hasErrors(String fieldName) {
        return getErrors(fieldName).size() > 0;
    }

    //
    // factory methods for nested inputs
    //

    public Submit submit(String name) {
        return new Submit(out, this, name);
    }

    public Submit submit(String name, String value) {
        return new Submit(out, this, name).setValue(value);
    }

    public Text text(String name) {
        return new Text(this, name);
    }

}
