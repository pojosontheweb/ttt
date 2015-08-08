package com.pojosontheweb.ttt;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TttListener extends TttParserBaseListener {

    private static class Arg {
        final String name;
        final String type;
        final String javadoc;

        public Arg(String name, String type, String javadoc) {
            this.name = name;
            this.type = type;
            this.javadoc = javadoc;
        }

        public List<String> computeJavaDocLines() {
            String lines[] = javadoc.split("\\r?\\n");
            List<String> res = new ArrayList<>();
            for (String line : lines) {
                String s = line.replaceFirst("\\*", "").trim();
                if (!"".equals(s)) {
                    res.add(s);
                }
            }
            return res;
        }
    }

    private final List<Arg> args = new ArrayList<>();
    private final List<String> importList = new ArrayList<>();
    private final List<String> extendsList = new ArrayList<>();
    private String contentType;

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
        TttParser.ArgJavaDocContext javaDocContext = ctx.argJavaDoc();
        String javadoc = null;
        if (javaDocContext != null) {
            TttParser.JdocTextContext jdocText = javaDocContext.jdocText();
            if (jdocText != null) {
                javadoc = jdocText.getText();
            }
        }
        args.add(new Arg(name, className, javadoc));
        super.exitArg(ctx);
    }

    // collect imports
    @Override
    public void exitDirectiveImport(TttParser.DirectiveImportContext ctx) {
        importList.add(ctx.directiveValue().getText());
        super.exitDirectiveImport(ctx);
    }

    // collect extends
    @Override
    public void exitDirectiveExtends(TttParser.DirectiveExtendsContext ctx) {
        extendsList.add(ctx.directiveValue().getText());
        super.exitDirectiveExtends(ctx);
    }

    @Override
    public void exitDirectiveContentType(TttParser.DirectiveContentTypeContext ctx) {
        TttParser.ContentTypeValueContext contentTypeValue = ctx.contentTypeValue();
        contentType = contentTypeValue != null ? contentTypeValue.getText() : null;
        super.exitDirectiveContentType(ctx);
    }

    private Stream<Arg> args() {
        return args.stream();
    }

    // write render method declaration
    @Override
    public void enterParts(TttParser.PartsContext ctx) {

        // write pkg, imports and class declaration
        if (pkg != null) {
            write("package ", pkg, ";\n\n");
        }

        for (String imp : importList) {
            write("import ", imp, ";\n\n");
        }

        String implementsClause = "";
        if (extendsList.size() > 0) {
            implementsClause = "implements " + extendsList.stream().collect(Collectors.joining(", ")) + " ";
        }

        write("public class ", className, " extends com.pojosontheweb.ttt.Template ", implementsClause, "{\n\n");

        // write template args as private final fields
        args().forEach(a -> write("\tprivate final ", a.type, " ", a.name, ";\n"));

        // write ctor javadoc
        write("\n\t/**");
        write("\n\t * Creates an instance of this template.");
        write("\n\t *");
        args().forEach(arg -> {
            // sanitize javadoc for @param : this
            // should probably be handled by the parser itself...
            String paramPrefix = "@param " + arg.name + "  ";
            String spacer = IntStream.range(0, paramPrefix.length())
                .mapToObj(operand -> " ")
                .collect(Collectors.joining());
            if (arg.javadoc != null) {
                boolean first = true;
                List<String> lines = arg.computeJavaDocLines();
                for (String line : lines) {
                    if (first) {
                        write("\n\t * ", paramPrefix, line);
                        first = false;
                    } else {
                        write("\n\t * ", spacer, line);
                    }
                }
            }
        });
        write("\n\t */");

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

        // write content-type accessor if any
        if (contentType != null) {
            write("\t@Override");
            write("\n\tpublic String getContentType() {");
            write("\n\t\treturn \"", contentType, "\";");
            write("\n\t}");
            write("\n\n");
        }

        write("\t@Override\n");
        write("\tpublic void doRender(com.pojosontheweb.ttt.TttWriter out) throws Exception {\n");
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
