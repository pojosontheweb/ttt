package com.pojosontheweb.ttt;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class TemplateTests {

    @Test
    public void test1() throws Exception {
        doTest("Test1");
    }

    @Test
    public void test2() throws Exception {
        doTest("Test2");
    }

    private static String toString(InputStream is) throws Exception {
        int nRead;
        byte[] data = new byte[2048];
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
        return new String(buffer.toByteArray(), "utf-8");
    }

    private static void doTest(String name) throws Exception {
        String templateText = null;
        try {
            templateText = toString(TemplateTests.class.getResourceAsStream("/ttt/tests/" + name + ".ttt"));
        } catch (Exception e) {
            fail("No such template : " + "/ttt/tests/" + name + ".ttt");
        }
        assertNotNull(templateText);
        StringWriter sw = new StringWriter();
        try {
            TttListener l = new TttListener(sw, "ttt.tests." + name);
            walk(templateText, l);
        } finally {
            sw.close();
        }
        String compiled = sw.toString();
        String expected = null;
        try {
            expected = toString(TemplateTests.class.getResourceAsStream("/ttt/tests/" + name + ".expected"));
        } catch (Exception e) {
            fail("Ref not found : " + "/ttt/tests/" + name + ".expected");
        }
        assertNotNull("Ref is null : " + "/ttt/tests/" + name + ".expected", expected);
        assertEquals("Unexpected result for template :\n" + templateText + "\n", expected, compiled);
    }

    private static void walk(String s, TttListener l) throws Exception {
        ANTLRInputStream input = new ANTLRInputStream(new StringReader(s)); // create a lexer that feeds off of input CharStream
        TttLexer lexer = new TttLexer(input); // create a buffer of tokens pulled from the lexer
        CommonTokenStream tokens = new CommonTokenStream(lexer); // create a parser that feeds off the tokens buffer
        TttParser parser = new TttParser(tokens);
        ParseTree tree = parser.r(); // begin parsing at init rule
        assertEquals(0, parser.getNumberOfSyntaxErrors());
        ParseTreeWalker w = new ParseTreeWalker();
        w.walk(l, tree);
    }

}
