package com.pojosontheweb.ttt.stripes;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.controller.ActionResolver;
import net.sourceforge.stripes.controller.StripesConstants;
import net.sourceforge.stripes.controller.StripesFilter;
import net.sourceforge.stripes.tag.PageOptionsTag;
import net.sourceforge.stripes.util.CryptoUtil;
import net.sourceforge.stripes.util.HtmlUtil;
import net.sourceforge.stripes.util.UrlBuilder;
import net.sourceforge.stripes.validation.ValidationError;
import net.sourceforge.stripes.validation.ValidationErrors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.Writer;
import java.util.*;

public class Form extends HtmlTag.WithBody<Form> {

    private final Class<? extends ActionBean> beanClass;
    private final String method;
    private final String action;
    private final boolean partial; // TODO handle partial form

    // TODO handle url, wizzard...

//    private Map<String,Class<?>> fieldsPresent = new HashMap<String,Class<?>>();

    public Form(
        Writer out,
        Class<? extends ActionBean> beanClass,
        boolean partial,
        String method,
        Map<String,String> attributes) {

        super(out, "form", attributes);
        this.beanClass = beanClass;
        this.partial = partial;
        this.method = method;

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
        this.action = response.encodeURL(action);
        attr("action", action);
        attr("method", method != null ? method : "POST");
    }

    @Override
    public void close() {
        writeHiddenTags();
        super.close();
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

//    public Text text(String field) {
//        return new Text(this, field);
//    }

    public Submit submit(String name, String value) {
        return new Submit(name, value, null);
    }

    public Text text(String name) {
        return new Text(this, name, null, null, null, null, null);
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

    protected String getActionBeanUrlBinding() {
        ActionResolver resolver = StripesFilter.getConfiguration().getActionResolver();
        return resolver.getUrlBinding(beanClass);
    }

    public List<ValidationError> getErrors(String fieldName) {
        ValidationErrors errs = getActionBean().getContext().getValidationErrors();
        List<ValidationError> fieldErrs = errs.get(fieldName);
        return fieldErrs != null ? fieldErrs : Collections.emptyList();
    }

    public boolean hasErrors(String fieldName) {
        return getErrors(fieldName).size() > 0;
    }

}
