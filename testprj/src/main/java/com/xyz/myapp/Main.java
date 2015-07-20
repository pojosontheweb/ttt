package com.xyz.myapp;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

public class Main {

    private static void sep(Writer out) throws IOException {
        out.write("\n*****\n");
    }

    public static void main(String[] args) throws Exception {
        Foo f = new Foo();
        try (Writer out = new PrintWriter(System.out)) {
            new OneAttr(f).render(out);
            sep(out);
            new OneAttrMultiLine(f).render(out);
            sep(out);
            new Wrapper("hello there", new OneAttr(f)).render(out);
        }
    }
}
