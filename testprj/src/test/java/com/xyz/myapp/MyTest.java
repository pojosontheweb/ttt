package com.xyz.myapp;

import static org.junit.Assert.*;
import org.junit.Test;

import java.io.StringWriter;

public class MyTest {

    @Test
    public void testMyTemplate() throws Exception {
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

    @Test
    public void testMyTemplate2() throws Exception {
        StringWriter out = new StringWriter();
        try {
            new MyTemplate2(new Foo()).render(out);
        } finally {
            out.close();
        }
        assertEquals("<div class=\"foo\">\n" +
            "    baz\n" +
            "    <span>ZERO</span>\n" +
            "</div>", out.toString());
    }

}
