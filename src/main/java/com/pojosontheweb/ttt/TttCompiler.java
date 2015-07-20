package com.pojosontheweb.ttt;

public class TttCompiler {

    public static CompilationResult compile(CompilationArgs args) {
        CompilationContext cc = new CompilationContext(args);
        return cc.compile();
    }


}




