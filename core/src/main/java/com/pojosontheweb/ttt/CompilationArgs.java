package com.pojosontheweb.ttt;

import java.io.Reader;
import java.io.Writer;

public class CompilationArgs {

    private final Reader in;
    private final Writer out;
    private final String fqn;

    public CompilationArgs(Reader in, Writer out, String fqn) {
        this.in = in;
        this.out = out;
        this.fqn = fqn;
    }

    public Reader getIn() {
        return in;
    }

    public Writer getOut() {
        return out;
    }

    public String getFqn() {
        return fqn;
    }
}
