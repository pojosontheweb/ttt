package com.pojosontheweb.ttt;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;

import java.io.PrintWriter;
import java.io.StringReader;
import java.io.Writer;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

public class AntlrTest {

    @Test
    public void parseVariousTemplates() throws Exception {

        AtomicInteger nbFails = new AtomicInteger(0);
        for (String s : Arrays.asList(
            "<%(foo1:com.xyz.Bar)%>",
            "<%( foo1  :com.xyz.Bar )%>",
            "<%( foo1  :com.xyz.Bar )%>\nsome text",
            "<%(foo1:com.xyz.Bar)%>\nTXT <%= EXPR %> TXT 2 <% CODE %> TXT3",
            "<%(foo1:com.xyz.Bar)%>\nTXT <%= EXPR %> TXT 2 <% CO\\%>DE %> TXT3"
//            "<%( foo1:com.xyz.Bar, x: int )%>\nthis is a template",
//            "<%( foo1:com.xyz.Bar, x: int )%>\nthis is a template with an expression\n<%=foo bar%>",
//            "<%( foo1:com.xyz.Bar, x: int )%>\nthis is a template with an expression including an escape\n<%=foo \\%> bar%>",
//            "<%( foo1:com.xyz.Bar, x: int )%>\nthis is a template\n<%=foo \\%> bar%> more text",
//            "<%( foo1:com.xyz.Bar, x: int )%>\nthis is a template\n<%=foo \\%> bar%> more text <% System.out.println(\"zobi\"); %>\noh yeah"
        )) {
            TttCompiler.convertExceptions(() -> {

                System.out.println("parsing template :");
                System.out.println(s);
                System.out.println("----");

                ANTLRInputStream input = new ANTLRInputStream(new StringReader(s)); // create a lexer that feeds off of input CharStream
                TttLexer lexer = new TttLexer(input); // create a buffer of tokens pulled from the lexer
                CommonTokenStream tokens = new CommonTokenStream(lexer); // create a parser that feeds off the tokens buffer
                TttParser parser = new TttParser(tokens);
                ParseTree tree = parser.r(); // begin parsing at init rule
                System.out.println(tree.toStringTree(parser)); // print LISP-style tree
                if (parser.getNumberOfSyntaxErrors() > 0) {
                    System.out.println("TEMPLATE HAS ERRORS !");
                    nbFails.incrementAndGet();
                }

                System.out.println("");
            });
        }
        assertEquals("Failures found", 0, nbFails.get());
    }

    @Test
    public void listener() throws Exception {
        try (Writer out = new PrintWriter(System.out)) {
            TttListener l = new TttListener(out, "com.xyz.myapp.MyTemplate");

            String s = "<%(foo1:com.xyz.Bar)%>\nTXT <%= EXPR %> TXT 2 <% CO\\%>DE %> TXT3";
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

}
