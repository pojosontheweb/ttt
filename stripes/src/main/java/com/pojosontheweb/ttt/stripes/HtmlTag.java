package com.pojosontheweb.ttt.stripes;

import com.pojosontheweb.ttt.Template;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.config.Configuration;
import net.sourceforge.stripes.controller.ParameterName;
import net.sourceforge.stripes.controller.StripesConstants;
import net.sourceforge.stripes.controller.StripesFilter;
import net.sourceforge.stripes.exception.StripesJspException;
import net.sourceforge.stripes.exception.StripesRuntimeException;
import net.sourceforge.stripes.format.*;
import net.sourceforge.stripes.localization.LocalizationUtility;
import net.sourceforge.stripes.util.CryptoUtil;
import net.sourceforge.stripes.util.HtmlUtil;
import net.sourceforge.stripes.util.Log;
import net.sourceforge.stripes.util.bean.BeanUtil;
import net.sourceforge.stripes.util.bean.ExpressionException;
import net.sourceforge.stripes.validation.ValidationError;
import net.sourceforge.stripes.validation.ValidationMetadata;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.pojosontheweb.ttt.Util.toRtEx;

/**
 * Base class for all HTML tags. Handles base tag functionality, such
 * as attributes and the like.
 *
 * Tags split in two categories :
 * <ul>
 *     <li>tags without body : {@link com.pojosontheweb.ttt.stripes.HtmlTag.WithoutBody}</li>
 *     <li>tags with an optional body : {@link com.pojosontheweb.ttt.stripes.HtmlTag.WithBody}</li>
 * </ul>
 */
public abstract class HtmlTag extends Template {

    protected final String tag;
    protected Attributes attributes;

    HtmlTag(String tag, Attributes attributes) {
        assert tag != null;
        this.tag = tag;
        this.attributes = attributes != null ? attributes : Attributes.emptyAttrs();
    }

    protected void writeTagStart(Writer out) {
        write(out, "<", tag);
        if (attributes.size()>0) {
            write(out, " ", attrsToString());
        }
    }

    protected void writeClosingTag(Writer out) {
        write(out, "</", tag, ">");
    }

    private String encodeAttribute(String name) {
        StringBuilder sb = new StringBuilder()
            .append(name)
            .append("=\"");
        String val = attributes.get(name);
        if (val != null) {
            sb.append(HtmlUtil.encode(val));
        }
        sb.append("\"");
        return sb.toString();
    }

    public String attrsToString() {
        return StreamSupport.stream(attributes.names().spliterator(), false)
            .map(this::encodeAttribute)
            .collect(Collectors.joining(" "));
    }

    protected HttpServletRequest getRequest() {
        return StripesTags.getRequest();
    }

    protected HttpServletResponse getResponse() {
        return StripesTags.getResponse();
    }

    /**
     * Base class for body-less (void elements) tags. Such tags can be passed
     * directly to an expression block.
     *
     */
    public static abstract class WithoutBody extends HtmlTag {

        public WithoutBody(String tag, Attributes attributes) {
            super(tag, attributes);
        }

        @Override
        public final void render(Writer out) throws IOException {
            writeTagStart(out);
            write(out, "/>");
        }

    }

    /**
     * Base class for html tags that have an optiona body. Those tags
     * can be used in two different ways. Without a body they can be used
     * inside an expression :
     *
     * <code>
     *     <pre>
     *         &lt;%= new MyTag() %&gt;
     *     </pre>
     * </code>
     *
     * With a body, you have to use a try-resource block and call open()
     * as the last instruction for opening the tag :
     *
     * <code>
     *     <pre>
     *         &lt;% try (MyTag t = new MyTag(..).open() { %@gt;
     *
     *              ... body goes here
     *
     *         &lt;% } %&gt;
     *     </pre>
     * </code>
     *
     */
    public static abstract class WithBody extends HtmlTag implements AutoCloseable {

        protected final Writer out;

        public WithBody(Writer out, String tag, Attributes attributes) {
            super(tag, attributes);
            this.out = out;
        }

        public WithBody open() {
            writeTagStart(out);
            write(out, ">");
            return this;
        }

        @Override
        public void close() {
            writeClosingTag(out);
        }

        // render is used only when using WithBody... without a body !
        // it can be used for tags that support both body and body-less flavors,
        // like <a>.
        // as they're templates they can be rendered directly if they have no body, using an expression
        // or with a nested body using try-resource pattern.
        @Override
        public final void render(Writer out) throws IOException {
            open();
            close();
        }
    }


    public static final class InputDelegate {

        private static final Log log = Log.getInstance(InputDelegate.class);

        private final String tag;
        private final Form form;
        private final String type;
        private final String name;
        private final String value;
        private final String errorCssClass;
        private final String formatType;
        private final String formatPattern;

        public InputDelegate(String tag, Form form, String type, String name) {
            this(tag, form, type, name, null, null, null, null);
        }

        public InputDelegate(
            String tag,
            Form form,
            String type,
            String name,
            String value,
            String errorCssClass,
            String formatType,
            String formatPattern) {

            this.tag = tag;
            this.value = value;
            this.errorCssClass = errorCssClass;
            this.type = type;
            this.name = name;
            this.formatType = formatType;
            this.formatPattern = formatPattern;
            this.form = form;
        }

        public boolean hasErrors() {
            return form.hasErrors(name);
        }

        public List<ValidationError> getErrors() {
            return form.getErrors(name);
        }

        protected String format() {
            Object v = getSingleOverrideValue();
            if (v != null) {
                return format(v, true);
            }
            return null;
        }

        private String format(Object input, boolean forOutput) {
            if (input == null) {
                return "";
            }

            // format the value
            FormatterFactory factory = StripesFilter.getConfiguration().getFormatterFactory();
            net.sourceforge.stripes.format.Formatter formatter = factory.getFormatter(input.getClass(),
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

        public Form getForm() {
            return form;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public String getFormatType() {
            return formatType;
        }

        public String getFormatPattern() {
            return formatPattern;
        }

        public Attributes computeAttributes(Attributes attributes) {
            Attributes a = attributes
                .set("name", name)
                .set("type", type);

            String formatted = format();

            if (formatted != null) {
                a = a.set("value", formatted);
            }
            if (errorCssClass != null && hasErrors()) {
                a = a.cat("class", " error"); // space is ugly, we'd need a css class helper...
            }
            return a;
        }
    }



    public static abstract class InputWithoutBody extends WithoutBody {

        private final InputDelegate delegate;

        public InputWithoutBody(InputDelegate delegate, Attributes attributes) {

            super(delegate.tag, attributes);
            this.delegate = delegate;
            this.attributes = delegate.computeAttributes(this.attributes);
        }

        public List<ValidationError> getErrors() {
            return delegate.getErrors();
        }
    }

    public static abstract class InputWithBody extends WithBody {

        private final InputDelegate delegate;

        public InputWithBody(Writer out, InputDelegate delegate, Attributes attributes) {
            super(out, delegate.tag, attributes);
            this.delegate = delegate;
            this.attributes = delegate.computeAttributes(this.attributes);
        }
    }

    /**
     * Base class for buttons : handles value attr msg lookup from resources
     *
     */
    public static abstract class ButtonBase extends WithBody {

        protected final Form form;
        protected final String name;
        protected final String value;

        public ButtonBase(Writer out, Form form, String tag, String type, String name, String value, Attributes attributes) {
            super(out, tag, attributes);
            this.form = form;
            this.name = name;
            this.value = value;
            this.attributes = this.attributes
                .set("type", type)
                .set("name", name)
                .set("value", getLocalizedValue(form, name, value));
        }

        private static String getLocalizedValue(Form form, String name, String value) {
            String localizedValue = toRtEx(() -> getLocalizedFieldName(form, name));
            // Figure out where to pull the value from
            if (localizedValue != null) {
                return localizedValue;
            }
            else {
                return value;
            }
        }

        static String getLocalizedFieldName(Form form, final String name) throws StripesJspException {
            Locale locale = StripesTags.getRequest().getLocale();

            String actionPath = null;
            Class<? extends ActionBean> beanClass = null;

            if (form != null) {
                actionPath = form.getAction();
                beanClass = form.getBeanClass();
            }
            else {
                ActionBean mainBean = (ActionBean)StripesTags.getRequest().getAttribute(StripesConstants.REQ_ATTR_ACTION_BEAN);
                if (mainBean != null) {
                    beanClass = mainBean.getClass();
                }
            }
            return LocalizationUtility.getLocalizedFieldName(name, actionPath, beanClass, locale);
        }
    }

}
