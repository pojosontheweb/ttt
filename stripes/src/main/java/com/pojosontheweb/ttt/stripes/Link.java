package com.pojosontheweb.ttt.stripes;

import net.sourceforge.stripes.util.HtmlUtil;

import java.io.Writer;
import java.util.Map;

public class Link extends HtmlTag.WithBody<Link> {

    private final Url url;
    private final String text;

    public Link(Writer out, Url url, Map<String, String> attributes) {
        this(out, url, attributes, null);
    }

    public Link(Writer out, Url url, Map<String, String> attributes, String text) {
        super(out, "a", attributes);
        this.url = url;
        this.text = text;
        attr("href", url.get());
    }

    @Override
    public void close() {
        // write our text if any
        if (text != null) {
            write(out, HtmlUtil.encode(text));
        }
        super.close();
    }

    public Link setText(String text) {
        return new Link(out, url, attributes, text);
    }

    public Link addParameter(String name, Object... value) {
        return new Link(out, url.addParameter(name, value), attributes, text);
    }
}
