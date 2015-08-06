package com.pojosontheweb.ttt.stripes;

import com.pojosontheweb.ttt.Template;
import net.sourceforge.stripes.util.HtmlUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Base class for all HTML tags. Handles base tag functionality, such
 * as attributes and the like.
 *
 * Tags split in two categories :
 * <ul>
 *     <li>tags without body : {@link com.pojosontheweb.ttt.stripes.HtmlTag.WithoutBody}</li>
 *     <li>tags with an optional body : {@link com.pojosontheweb.ttt.stripes.HtmlTag.WithBody}</li>
 * </ul>
 * @param <T> used for typed chained calls
 */
public abstract class HtmlTag<T extends HtmlTag<T>> extends Template {

    protected final String tag;
    protected final Map<String,String> attributes;

    public HtmlTag(String tag, Map<String, String> attributes) {
        assert tag != null;
        this.tag = tag;
        this.attributes = attributes == null ? new HashMap<>() : attributes;
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

    @SuppressWarnings("unchecked")
    protected T castThis() {
        return (T)this;
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
        return attributes.keySet().stream()
            .map(this::encodeAttribute)
            .collect(Collectors.joining(" "));
    }

    /**
     * Set the value of an attribute (mutates the object).
     * @param name the name of the attribute
     * @param value the value of the attribute
     * @return this (updated)
     */
    public T attr(String name, String value) {
        attributes.put(name, value);
        return castThis();
    }

    public T attrCat(String name, String value) {
        String newValue = value;
        String existingValue = attributes.get(name);
        if (existingValue != null) {
            newValue = existingValue + " " + value;
        }
        attributes.put(name, newValue);
        return castThis();
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
     * @param <T> used for chained calls
     */
    public static abstract class WithoutBody<T extends WithoutBody<T>> extends HtmlTag<T> {

        public WithoutBody(String tag, Map<String, String> attributes) {
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
     * @param <T> used for chained calls
     */
    public static abstract class WithBody<T extends WithBody<T>> extends HtmlTag<T> implements AutoCloseable {

        protected final Writer out;

        public WithBody(Writer out, String tag, Map<String, String> attributes) {
            super(tag, attributes);
            this.out = out;
        }

        public T open() {
            writeTagStart(out);
            write(out, ">");
            return castThis();
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

    /**
     * Base class for input tags, handles name and type attributes.
     *
     * @param <T> used for chained calls
     */
    public static abstract class Input<T extends WithoutBody<T>> extends WithoutBody<T> {

        private final String name;
        private final String type;

        public Input(String type, String name, Map<String, String> attributes) {
            super("input", attributes);
            this.name = name;
            this.type = type;
            attr("type", type);
            attr("name", name);
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }
    }

}
