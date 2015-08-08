package com.pojosontheweb.ttt.jsptags;

import javax.servlet.jsp.JspWriter;
import java.io.*;

public class TttJspWriter extends JspWriter {

    private final PrintWriter out;

    public TttJspWriter(Writer out) {
        super(0, true);
        this.out = new PrintWriter(out);
    }

    @Override
    public void newLine() throws IOException {
        out.println();
    }

    @Override
    public void print(boolean b) throws IOException {
        out.print(b);
    }

    @Override
    public void print(char c) throws IOException {
        out.print(c);

    }

    @Override
    public void print(int i) throws IOException {
        out.print(i);
    }

    @Override
    public void print(long l) throws IOException {
        out.print(l);
    }

    @Override
    public void print(float f) throws IOException {
        out.print(f);
    }

    @Override
    public void print(double d) throws IOException {
        out.print(d);
    }

    @Override
    public void print(char[] s) throws IOException {
        out.print(s);
    }

    @Override
    public void print(String s) throws IOException {
        out.print(s);
    }

    @Override
    public void print(Object obj) throws IOException {
        out.print(obj);
    }

    @Override
    public void println() throws IOException {
        out.println();
    }

    @Override
    public void println(boolean x) throws IOException {
        out.println(x);

    }

    @Override
    public void println(char x) throws IOException {
        out.println(x);
    }

    @Override
    public void println(int x) throws IOException {
        out.println(x);
    }

    @Override
    public void println(long x) throws IOException {
        out.println(x);
    }

    @Override
    public void println(float x) throws IOException {
        out.println(x);
    }

    @Override
    public void println(double x) throws IOException {
        out.println(x);
    }

    @Override
    public void println(char[] x) throws IOException {
        out.println(x);
    }

    @Override
    public void println(String x) throws IOException {
        out.println(x);
    }

    @Override
    public void println(Object x) throws IOException {
        out.println(x);
    }

    @Override
    public void clear() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clearBuffer() throws IOException {
        clear();
    }

    @Override
    public void flush() throws IOException {
        out.flush();
    }

    @Override
    public void close() throws IOException {
        out.close();
    }

    @Override
    public int getRemaining() {
        return 0;
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        out.write(cbuf, off, len);
    }

}
