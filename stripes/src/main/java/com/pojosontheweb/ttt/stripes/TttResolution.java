package com.pojosontheweb.ttt.stripes;

import com.pojosontheweb.ttt.ITemplate;
import com.pojosontheweb.ttt.Util;
import net.sourceforge.stripes.action.StreamingResolution;

import java.io.*;
import java.nio.charset.Charset;

public class TttResolution extends StreamingResolution {

    public TttResolution(ITemplate template) {
        this(template, Util.CHARSET_UTF8);
    }

    public TttResolution(ITemplate template, Charset encoding) {
        super(template.getContentType(), asReader(template, encoding));
        setCharacterEncoding(encoding.name());
    }

    private static Reader asReader(ITemplate template, Charset encoding) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try (OutputStreamWriter out = new OutputStreamWriter(bos, encoding)) {
                template.render(out);
            }
            return new StringReader(bos.toString(encoding.name()));
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

}
