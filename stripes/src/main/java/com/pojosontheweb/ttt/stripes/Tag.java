package com.pojosontheweb.ttt.stripes;

public interface Tag<T extends Tag<T>> extends AutoCloseable {
    T open();
}
