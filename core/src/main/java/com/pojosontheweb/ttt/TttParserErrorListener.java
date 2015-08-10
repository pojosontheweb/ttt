package com.pojosontheweb.ttt;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;

class TttParserErrorListener implements ANTLRErrorListener {

    private final List<TttCompileError> compileErrors = new ArrayList<>();

    private void addErr(String msg, int line, int charPositionInLine) {
        compileErrors.add(new TttCompileError(msg, line, charPositionInLine));
    }

    private void addErr(String msg) {
        compileErrors.add(new TttCompileError(msg, -1, -1));
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        addErr(msg, line, charPositionInLine);
    }

    @Override
    public void reportAmbiguity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, boolean exact, BitSet ambigAlts, ATNConfigSet configs) {
//        addErr("Ambiguity detected");
    }

    @Override
    public void reportAttemptingFullContext(Parser recognizer, DFA dfa, int startIndex, int stopIndex, BitSet conflictingAlts, ATNConfigSet configs) {
//        addErr("Attempting full context");
    }

    @Override
    public void reportContextSensitivity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, int prediction, ATNConfigSet configs) {
//        addErr("Context sensitivity");
    }

    public List<TttCompileError> getErrors() {
        return Collections.unmodifiableList(compileErrors);
    }
}
