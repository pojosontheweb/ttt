package com.pojosontheweb.ttt.stripes;

import java.util.Map;

public class Submit extends HtmlTag.Input<Submit> {

    private final String value;

    public Submit(String name, String value, Map<String, String> attributes) {
        super("submit", name, attributes);
        this.value = value;
        if (value != null) {
            attr("value", value);
        }
    }

    public Submit(String name) {
        this(name, null, null);
    }

    public String getValue() {
        return value;
    }
}
