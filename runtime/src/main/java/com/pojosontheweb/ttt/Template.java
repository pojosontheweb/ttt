package com.pojosontheweb.ttt;

import java.io.IOException;
import java.io.Writer;

import static com.pojosontheweb.ttt.Util.toRtEx;

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
                } else if (o instanceof SubTemplate) {

                    SubTemplate st = (SubTemplate)o;
                    // call the open method on the sub template
                    // and let it manage stuff. SubTemplate is an
                    // auto-closable and should be used with
                    // a try-resource block
                    st.open((TttWriter)out);

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

    @Override
    public final void render(Writer out) {
        // wrap the supplied writer into a TttWriter
        // if not already done...
        final TttWriter tw;
        if (out instanceof TttWriter) {
            tw = (TttWriter)out;
        } else {
            tw = new TttWriter(out);
        }
        toRtEx("error while rendering the template", () -> doRender(tw));
    }

    protected abstract void doRender(TttWriter tw) throws Exception;
}
