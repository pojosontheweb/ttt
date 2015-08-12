package com.pojosontheweb.ttt;

public class TttCompileError {

    private final String message;
    private final int line;
    private final int charInLine;

    public TttCompileError(String message, int line, int charInLine) {
        this.message = message;
        this.line = line;
        this.charInLine = charInLine;
    }

    public String getMessage() {
        return message;
    }

    public int getLine() {
        return line;
    }

    public int getCharInLine() {
        return charInLine;
    }
}
