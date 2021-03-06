package com.pojosontheweb.ttt;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Ignore;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

import static org.junit.Assert.*;

public class TemplateTest {

    @Test
    public void test1() throws Exception {
        doTest("Test1");
    }

    @Test
    public void test2() throws Exception {
        doTest("Test2");
    }

    @Test
    public void test3() throws Exception {
        doTest("Test3");
    }

    @Test
    public void test4() throws Exception {
        doTest("Test4");
    }

    @Test
    public void test5() throws Exception {
        doTest("Test5");
    }

    @Test
    public void test6() throws Exception {
        doTest("Test6");
    }

    @Test
    public void test7() throws Exception {
        doTest("Test7");
    }

    @Test
    public void test8() throws Exception {
        doTest("Test8");
    }

    @Test
    public void test9() throws Exception {
        doTest("Test9");
    }

    @Test
    public void test10() throws Exception {
        doTest("Test10");
    }

    @Test
    public void test11() throws Exception {
        doTest("Test11");
    }

    @Test
    public void test12() throws Exception {
        doTest("Test12");
    }

    @Test
    public void test13() throws Exception {
        doTest("Test13");
    }

    @Test
    public void test14() throws Exception {
        doTest("Test14");
    }

    @Test
    public void test15() throws Exception {
        doTest("Test15");
    }

    @Test
    public void test16() throws Exception {
        doTest("Test16");
    }

    @Test
    public void test17() throws Exception {
        doTest("Test17");
    }

    @Test
    public void test18() throws Exception {
        doTest("Test18");
    }

    @Test
    public void test19() throws Exception {
        doTest("Test19");
    }

    @Test
    public void test20() throws Exception {
        doTest("Test20");
    }

    @Test
    public void test21() throws Exception {
        doTest("Test21");
    }

    @Test
    public void test22() throws Exception {
        doTest("Test22");
    }

    @Test
    public void test23() throws Exception {
        doTest("Test23");
    }

    @Test
    public void test24() throws Exception {
        doTest("Test24");
    }

    @Test
    public void test25() throws Exception {
        doTest("Test25");
    }

    @Test
    public void test26() throws Exception {
        doTest("Test26");
    }

    @Test
    public void test27() throws Exception {
        doTest("Test27");
    }

    private static String toString(InputStream is) throws Exception {
        int nRead;
        byte[] data = new byte[2048];
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
        return crlfToLf(new String(buffer.toByteArray(), "utf-8"));
    }

    private static void doTest(String name) throws Exception {
        String templateText = null;
        try {
            templateText = toString(TemplateTest.class.getResourceAsStream("/ttt/tests/" + name + ".ttt"));
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
        System.out.println("---begin-compiled");
        System.out.println(compiled);
        System.out.println("---end-compiled");
        String expected = null;
        try {
            expected = toString(TemplateTest.class.getResourceAsStream("/ttt/tests/" + name + ".expected"));
        } catch (Exception e) {
            fail("Ref not found : " + "/ttt/tests/" + name + ".expected");
        }
        assertNotNull("Ref is null : " + "/ttt/tests/" + name + ".expected", expected);
        assertEquals("Unexpected result for template :\n" + templateText + "\n", expected, compiled);
    }
    
    private static String crlfToLf(String s) {
        return s.replaceAll("\r\n", "\n");
    }

    private static void walk(String s, TttListener l) throws Exception {
        TttParserErrorListener parserErrorListener = new TttParserErrorListener();
        ANTLRInputStream input = new ANTLRInputStream(new StringReader(s)); // create a lexer that feeds off of input CharStream
        TttLexer lexer = new TttLexer(input); // create a buffer of tokens pulled from the lexer
        lexer.addErrorListener(parserErrorListener);
        CommonTokenStream tokens = new CommonTokenStream(lexer); // create a parser that feeds off the tokens buffer
        TttParser parser = new TttParser(tokens);
        parser.addErrorListener(parserErrorListener);
        ParseTree tree = parser.r(); // begin parsing at init rule
        System.out.println(tree.toStringTree(parser));
        assertEquals(0, parser.getNumberOfSyntaxErrors());
        ParseTreeWalker w = new ParseTreeWalker();
        w.walk(l, tree);
        assertTrue("parsing errors found !", parserErrorListener.getErrors().isEmpty());
    }

}
