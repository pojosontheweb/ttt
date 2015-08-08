package com.pojosontheweb.ttt;

import java.io.Writer;

/**
 * Base interface for all templates.
 * TODO not sure this is needed the base abstract might be enough ?
 */
public interface ITemplate {

    /**
     * Return the content type, or null.
     */
    String getContentType();

    /**
     * Render this template to passed writer
     * @param out the writer to render the template to
     */
    void render(Writer out);

}
