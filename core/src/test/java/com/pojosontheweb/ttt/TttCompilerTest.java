package com.pojosontheweb.ttt;

import static org.junit.Assert.*;
import org.junit.Test;

import java.io.*;

public class TttCompilerTest {

    private static void assertNoErrors(CompilationResult res) {
        assertTrue(res.getErrors().size()==0);
    }

    private CompilationResult compile(
        String resourceName,
        String className) {
        try {


            Reader in = new InputStreamReader(getClass().getResourceAsStream(resourceName));
            StringWriter out = new StringWriter();
            CompilationArgs args = new CompilationArgs(in, out, className);
            CompilationResult result = TttCompiler.compile(args);

            System.out.println("Generated\n--------------------");
            System.out.println(out);

            return result;

        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testOneAttr() {
        assertNoErrors(compile("/OneAttr.ttt", "com.xyz.OneAttr"));
    }

    @Test
    public void testOneAttrMultiLine() {
        assertNoErrors(compile("/OneAttrMultiLine.ttt", "com.xyz.OneAttrMultiLine"));
    }

    @Test
    public void testOneAttrScript() {
        assertNoErrors(compile("/OneAttrScript.ttt", "com.xyz.OneAttrScript"));
    }

}
