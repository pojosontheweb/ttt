package com.pojosontheweb.ttt.stripes;

import com.pojosontheweb.ttt.Template;

import java.io.IOException;
import java.io.Writer;

public class Submit extends Template {

    private final String name;
    private final String value;
    private final Attributes attributes;

    public Submit(String name, String value, Attributes attributes) {
        this.name = name;
        this.value = value;
        this.attributes = attributes != null ? attributes : new Attributes();
        this.attributes.set("name", name);
        this.attributes.set("type", "submit");
        if (value != null) {
            this.attributes.set("value", value);
        }
    }

    @Override
    public void render(Writer out) throws IOException {
        write(out, "<input ");
        write(out, attributes.attrsToString());
        write(out, ">");
    }
}
