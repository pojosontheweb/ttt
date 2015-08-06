package com.pojosontheweb.ttt.stripes;

import net.sourceforge.stripes.util.HtmlUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

public class Attributes {

    protected final LinkedHashMap<String, String> attributes;

    public Attributes(LinkedHashMap<String, String> attributes) {
        if (attributes == null) {
            this.attributes = new LinkedHashMap<>();
        } else {
            this.attributes = new LinkedHashMap<>(attributes);
        }
    }

    public Attributes() {
        this(null);
    }

    public String get(String name) {
        return attributes.get(name);
    }

    public Iterable<String> names() {
        return attributes.keySet();
    }

    public Attributes set(String name, String value) {
        LinkedHashMap<String,String> newMap = new LinkedHashMap<>(attributes);
        if (value==null) {
            newMap.remove(name);
        } else {
            newMap.put(name, value);
        }
        return new Attributes(newMap);
    }

    public Attributes cat(String name, String value) {
        String newValue = value;
        String existingValue = attributes.get(name);
        if (existingValue != null) {
            newValue = existingValue + " " + value;
        }
        return set(name, newValue);
    }

    public Attributes replace(String name, String str, String replacement) {
        String s = attributes.get(name);
        if (s == null) {
            // nothing to do
            return this;
        }
        return set(name, s.replace(str, replacement));
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

    public static final Attributes EMPTY_ATTRS = new Attributes();

    public static Attributes emptyAttrs() {
        return EMPTY_ATTRS;
    }

    public Attributes unset(String name) {
        return set(name, null);
    }
}
