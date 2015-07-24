package com.pojosontheweb.ttt;

import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.Lexer;
import com.intellij.psi.tree.IElementType;
import com.intellij.testFramework.LightPlatformTestCase;
import com.intellij.testFramework.UsefulTestCase;
import com.pojosontheweb.ttt.psi.TttTypes;

import java.io.StringReader;

public class LexerTest extends UsefulTestCase {

    public void testError() {
        doTest(
            "<%((((",
            new String[]{
                "TttToken:SIG_START", "<%(",
                "BAD_CHARACTER", "(",
                "BAD_CHARACTER", "(",
                "BAD_CHARACTER", "("
            });
    }

    public void testEmptySignature() {
        doTest(
            "<%()%>",
            new String[]{
                "TttToken:SIG_START", "<%(",
                "TttToken:SIG_END", ")%>"
            });
    }

    public void testEmptySignatureWs() {
        doTest(
            "<%(  )%>",
            new String[]{
                "TttToken:SIG_START", "<%(",
                "WHITE_SPACE", "  ",
                "TttToken:SIG_END", ")%>"
            });
    }

    public void testOneArg() {
        doTest(
            "<%(java.lang.String foo)%>",
            new String[]{
                "TttToken:SIG_START", "<%(",
                "TttToken:TYPE", "java.lang.String",
                "WHITE_SPACE", " ",
                "TttToken:ID", "foo",
                "TttToken:SIG_END", ")%>"
            });
    }

    public void testOneArgWs() {
        doTest(
            "<%( java.lang.String foo )%>",
            new String[]{
                "TttToken:SIG_START", "<%(",
                "WHITE_SPACE", " ",
                "TttToken:TYPE", "java.lang.String",
                "WHITE_SPACE", " ",
                "TttToken:ID", "foo",
                "WHITE_SPACE", " ",
                "TttToken:SIG_END", ")%>"
            });
    }

    public void testOneSimpleType() {
        doTest(
            "<%( int foo )%>",
            new String[]{
                "TttToken:SIG_START", "<%(",
                "WHITE_SPACE", " ",
                "TttToken:ID", "int",
                "WHITE_SPACE", " ",
                "TttToken:ID", "foo",
                "WHITE_SPACE", " ",
                "TttToken:SIG_END", ")%>"
            });
    }

    public void testTwoArgs() {
        doTest(
            "<%(java.lang.String foo, int bar)%>",
            new String[]{
                "TttToken:SIG_START", "<%(",
                "TttToken:TYPE", "java.lang.String",
                "WHITE_SPACE", " ",
                "TttToken:ID", "foo",
                "TttToken:ARG_SEP", ",",
                "WHITE_SPACE", " ",
                "TttToken:ID", "int",
                "WHITE_SPACE", " ",
                "TttToken:ID", "bar",
                "TttToken:SIG_END", ")%>"
            });
    }


    private static Lexer newLexer(String text) {
        Lexer lexer = new FlexAdapter(new TttLexer(new StringReader(text)));
        lexer.start(text);
        return lexer;
    }

    private static void doTest(String text, String[] expectedTokens) {
        System.out.println("Text :");
        System.out.println(text);
        Lexer lexer = newLexer(text);
        int idx = 0;
        System.out.println("Tokens :");
        while (lexer.getTokenType() != null) {
            String tokenTxt = lexer.getTokenText();
            String tokenType = lexer.getTokenType().toString();
            System.out.println("\"" + tokenType + "\", \"" + tokenTxt + "\",");
            lexer.advance();
            idx++;
        }
        System.out.println("nbTokens=" + idx + "\n");
        idx = 0;
        lexer = newLexer(text);
        while (lexer.getTokenType() != null) {
            if (idx >= expectedTokens.length) fail("Too many tokens");
            String tokenName = lexer.getTokenType().toString();
            String expectedTokenType = expectedTokens[idx++];
            String expectedTokenText = expectedTokens[idx++];
            assertEquals(expectedTokenType, tokenName);
            String tokenText = lexer.getBufferSequence().subSequence(lexer.getTokenStart(), lexer.getTokenEnd()).toString();
            assertEquals(expectedTokenText, tokenText);
            lexer.advance();
        }

        if (idx < expectedTokens.length) fail("Not enough tokens");
    }

}
