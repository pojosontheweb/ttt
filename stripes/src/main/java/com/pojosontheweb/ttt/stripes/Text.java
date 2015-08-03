package com.pojosontheweb.ttt.stripes;

import com.pojosontheweb.ttt.Template;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.config.Configuration;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.ParameterName;
import net.sourceforge.stripes.controller.StripesConstants;
import net.sourceforge.stripes.controller.StripesFilter;
import net.sourceforge.stripes.exception.StripesJspException;
import net.sourceforge.stripes.exception.StripesRuntimeException;
import net.sourceforge.stripes.format.Formatter;
import net.sourceforge.stripes.format.FormatterFactory;
import net.sourceforge.stripes.util.CryptoUtil;
import net.sourceforge.stripes.util.Log;
import net.sourceforge.stripes.util.bean.BeanUtil;
import net.sourceforge.stripes.util.bean.ExpressionException;
import net.sourceforge.stripes.validation.ValidationError;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMetadata;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Text extends Template {

    private static final Log log = Log.getInstance(Text.class);

    private final Form form;
    private final String name;
    private final Attributes attributes;

    private String value;
    private String formatType;
    private String formatPattern;

    public Text(Form form, String name) {
        assert name != null && !"".equals(name);
        assert form != null;
        this.name = name;
        this.form = form;
        this.attributes = new Attributes();

        attributes.set("type", "text");
        attributes.set("name", name);
        Object v = getSingleOverrideValue();

        // Figure out where to pull the value from
        if (v != null) {
            attributes.set("value", format(v, true));
        }

        // add the "error" css class to the field
        if (getErrors().size()>0) {
            attributes.set("class", "error");
        }

//        set("maxlength", getEffectiveMaxlength());
    }

    protected String format(Object input, boolean forOutput) {
        if (input == null) {
            return "";
        }

        // format the value
        FormatterFactory factory = StripesFilter.getConfiguration().getFormatterFactory();
        Formatter formatter = factory.getFormatter(input.getClass(),
            ExecutionContext.currentContext().getActionBeanContext().getRequest().getLocale(),
            this.formatType,
            this.formatPattern);
        String formatted = (formatter == null) ? String.valueOf(input) : formatter.format(input);

        // encrypt the formatted value if required
        if (forOutput && formatted != null) {
            try {
                ValidationMetadata validate = getValidationMetadata();
                if (validate != null && validate.encrypted())
                    formatted = CryptoUtil.encrypt(formatted);
            }
            catch (JspException e) {
                throw new StripesRuntimeException(e);
            }
        }

        return formatted;
    }

    protected ValidationMetadata getValidationMetadata() throws StripesJspException {
        // find the action bean class we're dealing with
        Class<? extends ActionBean> beanClass = form.getBeanClass();

        if (beanClass != null) {
            // check validation for encryption flag
            return StripesFilter.getConfiguration().getValidationMetadataProvider()
                .getValidationMetadata(beanClass, new ParameterName(name));
        }
        else {
            return null;
        }
    }



    public Attributes getAttributes() {
        return attributes;
    }

    @Override
    public String getContentType() {
        return "text/html";
    }

    @Override
    public void render(Writer out) throws IOException {
        write(out, "<input ");
        write(out, attributes.attrsToString());
        write(out, ">");
    }

    protected Object getSingleOverrideValue() {
        Object unknown = getOverrideValueOrValues();
        Object returnValue = null;

        if (unknown != null && unknown.getClass().isArray()) {
            if (Array.getLength(unknown) > 0) {
                returnValue = Array.get(unknown, 0);
            }
        } else if (unknown != null && unknown instanceof Collection<?>) {
            Collection<?> collection = (Collection<?>) unknown;
            if (collection.size() > 0) {
                returnValue = collection.iterator().next();
            }
        } else {
            returnValue = unknown;
        }

        return returnValue;
    }

    protected Object getOverrideValueOrValues() {
        // Look first for something that the user submitted in the current request
        Object v = getValuesFromRequest();

        // If that's not there, let's look on the ActionBean
        if (v == null) {
            v = getValueFromActionBean();
        }

        // And if there's no value there, look at the tag's own value
        if (v == null) {
            v = value;
        }

        return v;
//
//        return StripesFilter.getConfiguration().getPopulationStrategy().getValue(this);
    }

    protected Object getValueFromActionBean() {
        ActionBean actionBean = form.getActionBean();
        Object value = null;

        if (actionBean != null) {
            try {
                value = BeanUtil.getPropertyValue(name, actionBean);
            }
            catch (ExpressionException ee) {
                if (!StripesConstants.SPECIAL_URL_KEYS.contains(name)) {
                    log.info("Could not find property [", name, "] on ActionBean.", ee);
                }
            }
        }

        return value;
    }

    protected String[] getValuesFromRequest() {
        HttpServletRequest request = ExecutionContext.currentContext().getActionBeanContext().getRequest();
        String[] value = request.getParameterValues(name);

        /*
         * If the value was pulled from a request parameter and the ActionBean property it would
         * bind to is flagged as encrypted, then the value needs to be decrypted now.
         */
        if (value != null) {
            // find the action bean class we're dealing with
            Class<? extends ActionBean> beanClass = form.getBeanClass();
            if (beanClass != null) {
                Configuration config = StripesFilter.getConfiguration();
                ValidationMetadata validate = config.getValidationMetadataProvider()
                    .getValidationMetadata(beanClass, new ParameterName(name));
                if (validate != null && validate.encrypted()) {
                    String[] copy = new String[value.length];
                    for (int i = 0; i < copy.length; i++) {
                        copy[i] = CryptoUtil.decrypt(value[i]);
                    }
                    value = copy;
                }
            }
        }

        return value;
    }

    public List<ValidationError> getErrors() {
        ValidationErrors errs = form.getActionBean().getContext().getValidationErrors();
        List<ValidationError> fieldErrs = errs.get(name);
        return fieldErrs != null ? fieldErrs : Collections.emptyList();
    }
}
