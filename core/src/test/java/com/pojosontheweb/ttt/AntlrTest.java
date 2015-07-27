package com.pojosontheweb.ttt;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;

import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

public class AntlrTest {

    @Test
    public void parseVariousTemplates() throws Exception {

        AtomicInteger nbFails = new AtomicInteger(0);
        for (String s : Arrays.asList(
            "<%@ page import=\"com.xyz.Bar\" %>",
            "<%@ page extends=\"com.xyz.Bar\" %>",
            "<%@ page import=\"com.xyz.Bar\" %>\n<%@ page extends=\"com.xyz.Bar\" %>",
            "  <%@ page import=\"com.xyz.Bar\" %>\n <%@ page extends=\"com.xyz.Bar\" %>",
            "  <%@ page import=\"com.xyz.Bar\" %>\n <%@ page extends=\"com.xyz.Bar\" %><%@page import=\"com.xyz.Bar2\"%>",
            "<%@ page import=\"com.xyz.Bar\" %>\nsome text",
            "<%@ page import=\"com.xyz.Bar\" %>\n<%! Foo bar; %>\nsome text",
            "<%@ page import=\"com.xyz.Bar\" %>\n<%!\n\tFoo bar;\n\tcom.xyz.Blah b;\n%>\nsome text",
            "<%@ page import=\"com.xyz.Bar\" %>\n<% foo %>",
            "<%@ page import=\"com.xyz.Bar\" %>\nHello <%= foo %>, <% bar %> !"
        )) {
            TttCompiler.convertExceptions(() -> {

//                System.out.println("parsing template :");
//                System.out.println(s);
//                System.out.println("----");

                ANTLRInputStream input = new ANTLRInputStream(new StringReader(s)); // create a lexer that feeds off of input CharStream
                TttLexer lexer = new TttLexer(input); // create a buffer of tokens pulled from the lexer
                CommonTokenStream tokens = new CommonTokenStream(lexer); // create a parser that feeds off the tokens buffer
                TttParser parser = new TttParser(tokens);
                ParseTree tree = parser.r(); // begin parsing at init rule
//                System.out.println(tree.toStringTree(parser)); // print LISP-style tree
                if (parser.getNumberOfSyntaxErrors() > 0) {
//                    System.out.println("TEMPLATE HAS ERRORS !");
                    nbFails.incrementAndGet();
                }

//                System.out.println("");
            });
        }
        assertEquals("Failures found", 0, nbFails.get());
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

    @Test
    public void listener() throws Exception {
        StringWriter sw = new StringWriter();
        try {
            TttListener l = new TttListener(sw, "com.xyz.myapp.MyTemplate");
            String s = "<%! com.xyz.Bar bar; %>\nTXT <%= EXPR %> TXT 2 <% CODE %> TXT3";
            walk(s, l);
        } finally {
            sw.close();
        }
        String actual = sw.toString();
//        System.out.println(actual);
        assertEquals("package com.xyz.myapp;\n" +
            "\n" +
            "public class MyTemplate extends com.pojosontheweb.ttt.Template {\n" +
            "\n" +
            "\tprivate final com.xyz.Bar bar;\n" +
            "\n" +
            "\t/**\n" +
            "\t * Creates an instance of this template.\n" +
            "\t *\n" +
            "\t */\n" +
            "\tpublic MyTemplate(com.xyz.Bar bar) {\n" +
            "\t\tthis.bar = bar;\n" +
            "\t}\n" +
            "\n" +
            "\t@Override\n" +
            "\tpublic void render(java.io.Writer out) throws java.io.IOException {\n" +
            "\t\twrite(out, \"TXT \" );\n" +
            "\t\twrite(out,  EXPR  );\n" +
            "\t\twrite(out, \" TXT 2 \" );\n" +
            "\t\tCODE \n" +
            "\t\twrite(out, \" TXT3\" );\n" +
            "\t}\n" +
            "}\n", actual);

    }

    @Test
    public void listenerImports() throws Exception {
        StringWriter sw = new StringWriter();
        try {
            TttListener l = new TttListener(sw, "com.xyz.myapp.MyTemplate");
            String s = "<%@ page import=\"java.util.Collection\" %>\n<%! Collection bar; %>\nTXT <%= EXPR %> TXT 2 <% CODE %> TXT3";
            walk(s, l);
        } finally {
            sw.close();
        }
        String actual = sw.toString();
//        System.out.println(actual);
        assertEquals("package com.xyz.myapp;\n" +
            "\n" +
            "import java.util.Collection;\n" +
            "\n" +
            "public class MyTemplate extends com.pojosontheweb.ttt.Template {\n" +
            "\n" +
            "\tprivate final Collection bar;\n" +
            "\n" +
            "\t/**\n" +
            "\t * Creates an instance of this template.\n" +
            "\t *\n" +
            "\t */\n" +
            "\tpublic MyTemplate(Collection bar) {\n" +
            "\t\tthis.bar = bar;\n" +
            "\t}\n" +
            "\n" +
            "\t@Override\n" +
            "\tpublic void render(java.io.Writer out) throws java.io.IOException {\n" +
            "\t\twrite(out, \"TXT \" );\n" +
            "\t\twrite(out,  EXPR  );\n" +
            "\t\twrite(out, \" TXT 2 \" );\n" +
            "\t\tCODE \n" +
            "\t\twrite(out, \" TXT3\" );\n" +
            "\t}\n" +
            "}\n", actual);

    }

    @Test
    public void listenerExtends() throws Exception {
        StringWriter sw = new StringWriter();
        try {
            TttListener l = new TttListener(sw, "com.xyz.myapp.MyTemplate");
            String s = "<%@ page extends=\"java.util.Collection\" %>\n<%! String bar; %>\nTXT <%= EXPR %> TXT 2 <% CODE %> TXT3";
            walk(s, l);
        } finally {
            sw.close();
        }
        String actual = sw.toString();
//        System.out.println(actual);
        assertEquals("package com.xyz.myapp;\n" +
            "\n" +
            "public class MyTemplate extends com.pojosontheweb.ttt.Template implements java.util.Collection {\n" +
            "\n" +
            "\tprivate final String bar;\n" +
            "\n" +
            "\t/**\n" +
            "\t * Creates an instance of this template.\n" +
            "\t *\n" +
            "\t */\n" +
            "\tpublic MyTemplate(String bar) {\n" +
            "\t\tthis.bar = bar;\n" +
            "\t}\n" +
            "\n" +
            "\t@Override\n" +
            "\tpublic void render(java.io.Writer out) throws java.io.IOException {\n" +
            "\t\twrite(out, \"TXT \" );\n" +
            "\t\twrite(out,  EXPR  );\n" +
            "\t\twrite(out, \" TXT 2 \" );\n" +
            "\t\tCODE \n" +
            "\t\twrite(out, \" TXT3\" );\n" +
            "\t}\n" +
            "}\n", actual);

    }

}
