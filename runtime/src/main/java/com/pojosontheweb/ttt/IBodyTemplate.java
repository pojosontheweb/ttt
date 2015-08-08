package com.pojosontheweb.ttt;

/**
 * Interface for "body templates" : i.e. templates that
 * are used with try-resource.
 */
public interface IBodyTemplate extends AutoCloseable {

    void open(TttWriter out);

}
