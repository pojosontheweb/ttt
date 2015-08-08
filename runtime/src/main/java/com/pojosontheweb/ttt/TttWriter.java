package com.pojosontheweb.ttt;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class TttWriter extends Writer {

    private final List<Writer> stack = new ArrayList<>();

    private Writer out;

    public TttWriter(Writer out) {
        this.stack.add(out);
        this.out = out;
    }

    public void write(int c) throws IOException {
        out.write(c);
    }

    public void write(String str, int off, int len) throws IOException {
        out.write(str, off, len);
    }

    public Writer append(CharSequence csq) throws IOException {
        return out.append(csq);
    }

    public Writer append(CharSequence csq, int start, int end) throws IOException {
        return out.append(csq, start, end);
    }

    public Writer append(char c) throws IOException {
        return out.append(c);
    }

    public void close() throws IOException {
        out.close();
    }

    public void write(char[] cbuf, int off, int len) throws IOException {
        out.write(cbuf, off, len);
    }

    public void write(char[] cbuf) throws IOException {
        out.write(cbuf);
    }

    public void write(String str) throws IOException {
        out.write(str);
    }

    public void flush() throws IOException {
        out.flush();
    }

    public void push(Writer writer) {
        stack.add(writer);
        out = writer;
    }

    public Writer pop() {
        if (stack.size() < 2) {
            throw new RuntimeException("cannot pop writer, none has been pushed");
        }
        Writer last = stack.remove(stack.size()-1);
        out = stack.get(stack.size()-1);
        return last;
    }
}
