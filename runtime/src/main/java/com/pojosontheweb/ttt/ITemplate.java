package com.pojosontheweb.ttt;

import java.io.Writer;

/**
 * Base interface for all templates.
 */
public interface ITemplate {

    /**
     * Render this template to passed writer
     * @param out the writer to render the template to
     */
    void render(Writer out);

    /**
     * Return the content type, if any. Defaults to null : it is
     * up to the rendering code to find a suitable default then.
     */
    default String getContentType() {
        return null;
    }

}
