package com.pojosontheweb.ttt;

import org.antlr.v4.gui.TreeViewer;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;

import javax.swing.*;
import java.io.StringReader;
import java.util.Arrays;

public class AntlrTest {

    @Test
    public void doTest() throws Exception{

        String s = "<%( foo1:com.xyz.Bar, x: int )%>\nthis is a template\n<%=foo bar%>";
        ANTLRInputStream input = new ANTLRInputStream(new StringReader(s)); // create a lexer that feeds off of input CharStream
        TttLexer lexer = new TttLexer(input); // create a buffer of tokens pulled from the lexer
        CommonTokenStream tokens = new CommonTokenStream(lexer); // create a parser that feeds off the tokens buffer
        TttParser parser = new TttParser(tokens);
        ParseTree tree = parser.r(); // begin parsing at init rule
        System.out.println(s); // print LISP-style tree
        System.out.println(tree.toStringTree(parser)); // print LISP-style tree
    }

//    public static void main(String[] args) throws Exception {
//        String s = "<%( foo : com.xyz.Foo )%>";
//        ANTLRInputStream input = new ANTLRInputStream(new StringReader(s)); // create a lexer that feeds off of input CharStream
//        TttLexer lexer = new TttLexer(input); // create a buffer of tokens pulled from the lexer
//        CommonTokenStream tokens = new CommonTokenStream(lexer); // create a parser that feeds off the tokens buffer
//        TttParser parser = new TttParser(tokens);
//        ParseTree tree = parser.r(); // begin parsing at init rule
//
//        //show AST in GUI
//        JFrame frame = new JFrame("Antlr AST");
//        JPanel panel = new JPanel();
//        TreeViewer viewr = new TreeViewer(Arrays.asList(
//            parser.getRuleNames()),tree);
//        viewr.setScale(1.5);//scale a little
//        panel.add(viewr);
//        frame.add(panel);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(200,200);
//        frame.setVisible(true);
//    }
}
