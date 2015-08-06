package com.pojosontheweb.ttt.stripes;

import net.sourceforge.stripes.util.HtmlUtil;

import java.io.Writer;
import java.util.Map;

public class Link extends HtmlTag.WithBody {

    private final Url url;
    private final String text;

    Link(Writer out, Url url, String text, Attributes attributes) {
        super(out, "a", attributes);
        this.url = url;
        this.text = text;
        this.attributes = computeAttributes();
    }

    private Attributes computeAttributes() {
        return attributes.set("href", url.get());
    }

    @Override
    public Link open() {
        return (Link)super.open();
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
        return new Link(out, url, text, attributes);
    }

    public Link addParameter(String name, Object... value) {
        return new Link(out, url.addParameter(name, value), text, attributes);
    }
}
