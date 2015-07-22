package com.pojosontheweb.ttt;

import org.junit.Ignore;
import org.junit.Test;

import java.io.*;

public class TttCompilerTest {

    private void compile (
        String resourceName,
        String className) {
        try {
            Reader in = new InputStreamReader(getClass().getResourceAsStream(resourceName));
            try (StringWriter out = new StringWriter()) {
                TttCompiler.compile(in, out, className);
                System.out.println("Generated\n--------------------");
                System.out.println(out);
            }
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testOneAttr() {
        compile("/OneAttr.ttt", "com.xyz.OneAttr");
    }

    @Test
    public void testOneAttrMultiLine() {
        compile("/OneAttrMultiLine.ttt", "com.xyz.OneAttrMultiLine");
    }

    @Test
    public void testOneAttrScript() {
        compile("/OneAttrScript.ttt", "com.xyz.OneAttrScript");
    }

}
