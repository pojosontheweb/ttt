package com.pojosontheweb.ttt;

import org.antlr.v4.runtime.tree.TerminalNode;

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
        String name = ctx.ID().getText();
        String className = ctx.TYPE().getText();
        args.add(new Arg(name, className));
        super.exitArg(ctx);
    }

    private Stream<Arg> args() {
        return args.stream();
    }

    // write class header
    @Override
    public void exitSignature(TttParser.SignatureContext ctx) {

        // write pkg and class declaration
        if (pkg!=null) {
            write("package ", pkg, ";\n\n");
        }
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

        super.exitSignature(ctx);
    }

    // write render method declaration
    @Override
    public void enterParts(TttParser.PartsContext ctx) {
        write("\t@Override\n");
        write("\tpublic void render(java.io.Writer out) throws java.io.IOException {\n");
        super.enterParts(ctx);
    }

    // close render method and generated class
    @Override
    public void exitParts(TttParser.PartsContext ctx) {
        write("\t}\n}");
        super.exitParts(ctx);
    }

    @Override
    public void exitPart(TttParser.PartContext ctx) {
        TerminalNode n = ctx.EXPRESSION();
        if (n!=null) {
            // expression
            write("\t\twrite(out, ", n.getText(), ");\n");
        } else {
            n = ctx.SCRIPTLET();
            if (n!=null) {
                // script
                write("\t\t", n.getText(), "\n");
            } else {
                // text
                List<TerminalNode> nodes = ctx.TEXT();
                write("\t\twrite(out, \"");
                for(TerminalNode node : nodes) {
                    write(escape(node.getText())); // TODO \n and "
                }
                write("\");\n");
            }
        }
        super.exitPart(ctx);
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
