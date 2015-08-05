package com.pojosontheweb.ttt.stripes;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.controller.StripesFilter;
import net.sourceforge.stripes.exception.StripesJspException;
import net.sourceforge.stripes.exception.StripesRuntimeException;
import net.sourceforge.stripes.util.Log;
import net.sourceforge.stripes.util.ReflectUtil;
import net.sourceforge.stripes.util.UrlBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import static com.pojosontheweb.ttt.Util.toRtEx;

public class Link extends TagBase<Link> {

    private static final Log log = Log.getInstance(Link.class);

    private final Class<? extends ActionBean> beanClass;
    private final String url;
    private final Map<String,Object> parameters;
    private final String event;
    private final String anchor;

    public Link(
        Writer out,
        Class<? extends ActionBean> beanClass,
        String url,
        Map<String, Object> parameters,
        String event,
        String anchor) {

        super(out, "a", null);
        this.beanClass = beanClass;
        this.url = url;
        this.parameters = parameters;
        this.event = event;
        this.anchor = anchor;

        set("href", toRtEx(this::buildUrl));
    }

    @Override
    public Link open() {
        writeTagOpen();
        return this;
    }

    @Override
    public void close() {
        writeTagClose();
    }

    /**
     * Builds the URL based on the information currently stored in the tag. Ensures that all
     * parameters are appended into the URL, along with event name if necessary and the source
     * page information.
     *
     * @return the fully constructed URL
     * @throws StripesJspException if the base URL cannot be determined
     */
    protected String buildUrl() throws StripesJspException {
        HttpServletRequest request = StripesTags.getRequest();
        HttpServletResponse response = StripesTags.getResponse();

        // Add all the parameters and reset the href attribute; pass to false here because
        // the HtmlTagSupport will HtmlEncode the ampersands for us
        String base = getPreferredBaseUrl();
        UrlBuilder builder = new UrlBuilder(request.getLocale(), base, false);
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
        builder.addParameters(this.parameters);

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

    /**
     * Returns the base URL that should be used for building the link. This is derived from
     * the 'beanclass' attribute if it is set, else from the 'url' attribute.
     *
     * @return the preferred base URL for the link
     * @throws StripesJspException if a beanclass attribute was specified, but does not identify
     *         an existing ActionBean
     */
    protected String getPreferredBaseUrl() throws StripesJspException {
        // If the beanclass attribute was supplied we'll prefer that to an href
        if (this.beanClass != null) {
            String beanHref = getActionBeanUrl(beanClass);
            if (beanHref == null) {
                throw new StripesJspException("The value supplied for the 'beanclass' attribute "
                    + "does not represent a valid ActionBean. The value supplied was '" +
                    this.beanClass + "'. If you're prototyping, or your bean isn't ready yet " +
                    "and you want this exception to go away, just use 'href' for now instead.");
            }
            else {
                return beanHref;
            }
        }
        else {
            return url;
        }
    }

    public static class Builder {

        private final Writer out;
        private final Map<String,Object> parameters = new HashMap<>();
        private final Class<? extends ActionBean> beanClass;
        private final String url;

        private String event;
        private String anchor;

        private Builder(Writer out, Class<? extends ActionBean> beanClass, String url) {
            this.out = out;
            this.beanClass = beanClass;
            this.url = url;
            if (beanClass!=null && url!=null) {
                throw new StripesRuntimeException("You should never use beanclass and url in a link");
            }
        }

        public Builder(Writer out, Class<? extends ActionBean> beanClass) {
            this(out, beanClass, null);
        }

        public Builder(Writer out, String url) {
            this(out, null, url);
        }

        public Builder setEvent(String event) {
            this.event = event;
            return this;
        }

        public Builder setAnchor(String anchor) {
            this.anchor = anchor;
            return this;
        }

        public Builder setParameter(String name, Object value) {
            parameters.put(name, value);
            return this;
        }

        public Link build() {
            return new Link(out, beanClass, url, parameters, event, anchor).open();
        }

    }
}
