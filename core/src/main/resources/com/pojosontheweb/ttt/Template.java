package com.pojosontheweb.ttt;

import java.io.IOException;
import java.io.Writer;

public abstract class Template {

    public abstract void render(Writer out) throws IOException;

    protected void write(Writer out, Object o) {
        try {
            if (o == null) {
                out.write("null");
            } else {
                if (o instanceof Template) {
                    ((Template) o).render(out);
                } else if (o instanceof String) {
                    out.write((String) o);
                } else {
                    out.write(o.toString());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
