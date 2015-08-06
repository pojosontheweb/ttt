package com.pojosontheweb.ttt.stripes;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.config.Configuration;
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
import net.sourceforge.stripes.validation.ValidationMetadata;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;

public class Text extends HtmlTag.Input<Text> {

    private static final Log log = Log.getInstance(Text.class);

    private final Form form;
    private String value;
    private String errorCssClass;

    public Text(Form form, String name) {
        super("input", "text", name, null);
        this.form = form;
        updateValueAttr();
    }

    public Text setValue(String value) {
        this.value = value;
        updateValueAttr();
        return this;
    }

    public Text setErrorCssClass(String css) {
        this.errorCssClass = css;
        if (errorCssClass != null && hasErrors()) {
            // add the "error" css class to the field
            attrCat("class", errorCssClass);
        } else {
            // remove the "error" css class but preserve
            // existing other classes
            attrReplace("class", errorCssClass, "");
        }
        return this;
    }

    private void updateValueAttr() {
        Object v = getSingleOverrideValue();
        if (v != null) {
            attr("value", format(v, true));
        }
    }


    public boolean hasErrors() {
        return form.hasErrors(getName());
    }

    private String format(Object input, boolean forOutput) {
        if (input == null) {
            return "";
        }

        // format the value
        FormatterFactory factory = StripesFilter.getConfiguration().getFormatterFactory();
        Formatter formatter = factory.getFormatter(input.getClass(),
            StripesTags.getRequest().getLocale(),
            this.getFormatType(),
            this.getFormatPattern());
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

    private ValidationMetadata getValidationMetadata() throws StripesJspException {
        // find the action bean class we're dealing with
        Class<? extends ActionBean> beanClass = form.getBeanClass();

        if (beanClass != null) {
            // check validation for encryption flag
            return StripesFilter.getConfiguration().getValidationMetadataProvider()
                .getValidationMetadata(beanClass, new ParameterName(getName()));
        }
        else {
            return null;
        }
    }

    private Object getSingleOverrideValue() {
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

    private Object getOverrideValueOrValues() {
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

    private Object getValueFromActionBean() {
        ActionBean actionBean = form.getActionBean();
        Object value = null;

        if (actionBean != null) {
            try {
                value = BeanUtil.getPropertyValue(getName(), actionBean);
            }
            catch (ExpressionException ee) {
                if (!StripesConstants.SPECIAL_URL_KEYS.contains(getName())) {
                    log.info("Could not find property [", getName(), "] on ActionBean.", ee);
                }
            }
        }

        return value;
    }

    private String[] getValuesFromRequest() {
        HttpServletRequest request = StripesTags.getRequest();
        String[] value = request.getParameterValues(getName());

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
                    .getValidationMetadata(beanClass, new ParameterName(getName()));
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
        return form.getErrors(getName());
    }

    public Form getForm() {
        return form;
    }

    public String getValue() {
        return value;
    }

    public String getErrorCssClass() {
        return errorCssClass;
    }
}
