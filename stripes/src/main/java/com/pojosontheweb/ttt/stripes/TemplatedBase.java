package com.pojosontheweb.ttt.stripes;

import com.pojosontheweb.ttt.Template;


public abstract class TemplatedBase<T extends TemplatedBase<T>> extends Template {

    private final Attributes attributes = new Attributes();

    @SuppressWarnings("unchecked")
    protected T set(String name, String val) {
        attributes.set(name, val);
        return (T)this;
    }

    protected Attributes attrs() {
        return attributes;
    }

}
