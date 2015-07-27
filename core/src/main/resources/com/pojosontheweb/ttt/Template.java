package com.pojosontheweb.ttt;

import java.io.IOException;
import java.io.Writer;

public abstract class Template implements ITemplate {

    protected void write(Writer out, Object o) {
        try {
            if (o == null) {
                out.write("null");
            } else {
                if (o instanceof ITemplate) {
                    ((ITemplate) o).render(out);
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
