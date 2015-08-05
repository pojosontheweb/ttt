package com.pojosontheweb.ttt.stripes;

import net.sourceforge.stripes.util.HtmlUtil;

import java.util.LinkedHashMap;
import java.util.stream.Collectors;

public class Attributes {

    protected final LinkedHashMap<String,String> attributes;

    public Attributes(LinkedHashMap<String, String> attributes) {
        this.attributes = attributes == null ? new LinkedHashMap<>() : attributes;
    }

    public Attributes() {
        this(null);
    }

    protected void set(String name, String value) {
        attributes.put(name, value);
    }

    public int size() {
        return attributes.size();
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


}
