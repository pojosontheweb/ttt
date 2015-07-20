package com.xyz.myapp;

import static org.junit.Assert.*;
import org.junit.Test;

import java.io.StringWriter;

public class MyTest {

    @Test
    public void doTest() throws Exception {
        StringWriter out = new StringWriter();
        try {
            new MyTemplate(new Foo()).render(out);
        } finally {
            out.close();
        }
        assertEquals("<div class=\"foo\">\n" +
            "    baz\n" +
            "</div>", out.toString());
    }

}
