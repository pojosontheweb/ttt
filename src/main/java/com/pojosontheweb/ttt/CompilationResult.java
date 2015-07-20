package com.pojosontheweb.ttt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompilationResult {

    public static class Err {

        private final int offset;
        private final String msg;

        public Err(int offset, String msg) {
            this.offset = offset;
            this.msg = msg;
        }

        public int getOffset() {
            return offset;
        }

        public String getMsg() {
            return msg;
        }
    }

    private final List<Err> errors = new ArrayList<>();

    public CompilationResult() {
    }

    public void addError(int offset, String err) {
        errors.add(new Err(offset, err));
    }

    public List<Err> getErrors() {
        return Collections.unmodifiableList(errors);
    }
}
