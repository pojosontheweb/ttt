package com.pojosontheweb.ttt.stripes;

public interface HasAttributes<T extends HasAttributes<T>> {

    T set(String name, Object value);

}
