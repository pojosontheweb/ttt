package com.pojosontheweb.ttt;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TttListener extends TttParserBaseListener {

    private static class Arg {
        final String name;
        final String type;

        public Arg(String name, String type) {
            this.name = name;
            this.type = type;
        }
    }

    private final List<Arg> args = new ArrayList<>();

    private final Writer out;
    private final String pkg;
    private final String className;

    public TttListener(Writer out, String fqn) {
        this.out = out;
        int dotIndex = fqn.lastIndexOf('.');
        if (dotIndex == -1) {
            pkg = null;
            className = fqn;
        } else {
            pkg = fqn.substring(0, dotIndex);
            className = fqn.substring(dotIndex + 1);
        }
    }

    // collect arguments
    @Override
    public void exitArg(TttParser.ArgContext ctx) {
        String name = ctx.argName().getText();
        String className = ctx.argType().getText();
        args.add(new Arg(name, className));
        super.exitArg(ctx);
    }

    private Stream<Arg> args() {
        return args.stream();
    }

    // write render method declaration
    @Override
    public void enterParts(TttParser.PartsContext ctx) {

        // write pkg, imports and class declaration
        if (pkg!=null) {
            write("package ", pkg, ";\n\n");
        }

        write("import java.io.*;\n");
        write("import java.util.*;\n\n");

        write("public class ", className, " extends com.pojosontheweb.ttt.Template {\n\n");

        // write template args as private final fields
        args().forEach(a -> write("\tprivate final ", a.type, " ", a.name, ";\n"));

        // write constructor
        write("\n\tpublic ", className, "(");
        write(
            args()
                .map(arg -> arg.type + " " + arg.name)
                .collect(Collectors.joining(", "))
        );
        write(") {\n");
        args().forEach(a -> write("\t\tthis.", a.name, " = ", a.name, ";\n"));
        write("\t}\n\n");


        write("\t@Override\n");
        write("\tpublic void render(Writer out) throws IOException {\n");
        super.enterParts(ctx);
    }

    // close render method and generated class
    @Override
    public void exitParts(TttParser.PartsContext ctx) {
        write("\t}\n}\n");
        super.exitParts(ctx);
    }

    @Override
    public void exitScript(TttParser.ScriptContext ctx) {
        write("\t\t", ctx.getText(), "\n");
        super.exitScript(ctx);
    }

    @Override
    public void exitExpr(TttParser.ExprContext ctx) {
        write("\t\twrite(out, ", ctx.getText(), " );\n");
        super.exitExpr(ctx);
    }


    @Override
    public void exitText(TttParser.TextContext ctx) {
        write("\t\twrite(out, \"", escape(ctx.getText()), "\" );\n");
        super.exitText(ctx);
    }

    private static String escape(String text) {
        return text.replace("\n", "\\n").replace("\"", "\\\"");
    }

    private void write(String... strs) {
        try {
            for (String s : strs) {
                out.write(s);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
