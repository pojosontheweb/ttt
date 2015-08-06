package com.pojosontheweb.ttt.stripes;

import com.pojosontheweb.ttt.Template;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.controller.StripesFilter;
import net.sourceforge.stripes.exception.StripesJspException;
import net.sourceforge.stripes.util.Log;
import net.sourceforge.stripes.util.ReflectUtil;
import net.sourceforge.stripes.util.UrlBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;
import java.util.Objects;

import static com.pojosontheweb.ttt.Util.toRtEx;

/**
 * Manages Stripes URLs : Computes the actual URL for an action bean,
 * handling params, event, etc.
 */
public class Url extends Template {

    private static final Log log = Log.getInstance(Url.class);

    private final Class<? extends ActionBean> beanClass;
    private final String url;
    private final String event;
    private final String anchor;
    private final String builtUrl;
    private final Parameters parameters;

    public Url(Class<? extends ActionBean> beanClass, String url, String event, String anchor, Parameters parameters) {
        assert beanClass != null || url != null;
        this.beanClass = beanClass;
        this.url = url;
        this.event = event;
        this.anchor = anchor;
        this.parameters = parameters != null ? parameters : new Parameters();
        this.builtUrl = toRtEx("Unable to build url !", this::buildUrl);
    }

    public Url(Class<? extends ActionBean> beanClass) {
        this(beanClass, null, null, null, null);
    }

    public Url(String url) {
        this(null, url, null, null, null);
    }

    /**
     * Return the built url as a string
     */
    public String get() {
        return builtUrl;
    }

    private String buildUrl() throws StripesJspException {
        HttpServletRequest request = StripesTags.getRequest();
        HttpServletResponse response = StripesTags.getResponse();

        String base = beanClass!= null ? getPreferredBaseUrl(beanClass) : url;

        UrlBuilder builder = new UrlBuilder(request.getLocale(), base, true);
        if (this.event != null) {
            builder.setEvent(this.event.isEmpty() ? null : this.event);
        }
//        if (addSourcePage) {
//            builder.addParameter(StripesConstants.URL_KEY_SOURCE_PAGE,
//                CryptoUtil.encrypt(request.getServletPath()));
//        }
        if (this.anchor != null) {
            builder.setAnchor(anchor);
        }
        builder.addParameters(this.parameters.get());

        // Prepend the context path, but only if the user didn't already
        String url = builder.toString();
        String contextPath = request.getContextPath();
        if (contextPath.length() > 1) {
            boolean prepend = beanClass != null
                || url.startsWith("/") && !url.startsWith(contextPath);

            if (prepend) {
                if (url.startsWith("/"))
                    url = contextPath + url;
                else
                    log.warn("Use of prependContext=\"true\" is only valid with a URL that starts with \"/\"");
            }
        }

        return response.encodeURL(url);
    }

    public static String getPreferredBaseUrl(Class<? extends ActionBean> beanClass) throws StripesJspException {
        String beanHref = getActionBeanUrl(beanClass);
        if (beanHref == null) {
            throw new StripesJspException("The value supplied for the 'beanclass' attribute "
                + "does not represent a valid ActionBean. The value supplied was '" +
                beanClass + "'. If you're prototyping, or your bean isn't ready yet " +
                "and you want this exception to go away, just use 'href' for now instead.");
        }
        else {
            return beanHref;
        }
    }

    @SuppressWarnings("unchecked")
    public static Class<? extends ActionBean> getActionBeanType(Object nameOrClass) {
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

    /**
     * Similar to the {@link #getActionBeanType(Object)} method except that instead of
     * returning the Class of ActionBean it returns the URL Binding of the ActionBean.
     *
     * @param nameOrClass either the String FQN of an ActionBean class, or a Class object
     * @return the URL of the appropriate ActionBean class or null
     */
    public static String getActionBeanUrl(Object nameOrClass) {
        Class<? extends ActionBean> beanType = getActionBeanType(nameOrClass);
        if (beanType != null) {
            return StripesFilter.getConfiguration().getActionResolver().getUrlBinding(beanType);
        }
        else {
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Url url = (Url) o;
        return Objects.equals(builtUrl, url.builtUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(builtUrl);
    }

    public Url addParameter(String name, Object[] value) {
        return new Url(beanClass, url, event, anchor, parameters.add(name, value));
    }

    @Override
    public void render(Writer out) throws IOException {
        write(out, get());
    }
}
