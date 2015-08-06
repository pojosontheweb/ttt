package com.pojosontheweb.ttt.stripes;

import net.sourceforge.stripes.util.HtmlUtil;

import java.io.Writer;
import java.util.Map;

public class Link extends HtmlTag.WithBody<Link> {

    private Url url;
    private String text;

    public Link(Writer out, Url url) {
        super(out, "a", null);
        this.url = url;
        updateAttributes();
    }

    private void updateAttributes() {
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
        this.text = text;
        return this;
    }

    public Link addParameter(String name, Object... value) {
        url = url.addParameter(name, value);
        updateAttributes();
        return this;
    }
}
