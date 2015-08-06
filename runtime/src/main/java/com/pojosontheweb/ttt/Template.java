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

    protected void write(Writer out, Object first, Object... rest) {
        write(out, first);
        if (rest != null) {
            for (Object o : rest) {
                write(out, o);
            }
        }
    }

    @Override
    public String getContentType() {
        return null;
    }

}
