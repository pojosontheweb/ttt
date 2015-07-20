package com.pojosontheweb.ttt;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class CompilationContext {

//    public static void log(String msg) {
//        System.out.println(msg);
//    }

    private enum State {
        Text, Expr
    }

    private final static char EOF = (char) -1;

    private final Reader in;
    private final Writer out;
    private final String fqn;

    private CompilationResult compilationResult;
    private State state = null;

    private int cur = 0;

    public CompilationContext(CompilationArgs args) {
        this.in = args.getIn();
        this.out = args.getOut();
        this.fqn = args.getFqn();
    }

    private char read() {
        try {
            cur++;
            return (char) in.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static class TemplateArgs {

        private static class Arg {
            private final String name;
            private final String type;

            public Arg(String name, String type) {
                this.name = name;
                this.type = type;
            }
        }

        private final List<Arg> arguments;

        public TemplateArgs(List<Arg> arguments) {
            this.arguments = arguments;
        }

        public List<Arg> getArguments() {
            return Collections.unmodifiableList(arguments);
        }

    }

    public CompilationResult compile() {
        compilationResult = new CompilationResult();
        String[] pkgAndClass = getPkgAndClassName();
        if (pkgAndClass[0] != null) {
            write("package ", pkgAndClass[0], ";\n\n");
        }

        write("public class ", pkgAndClass[1], " extends com.pojosontheweb.ttt.Template {\n\n");

        // parse template signature
        TemplateArgs args = templateArguments();
        // write private fields
        for (TemplateArgs.Arg arg : args.getArguments()) {
            write("\tprivate final ");
            write(arg.type);
            write(" ");
            write(arg.name);
            write(";\n");
        }
        write("\n");

        // write ctor
        write("\tpublic ");
        write(pkgAndClass[1]);
        write("(");
        write(args.getArguments().stream()
                .map(arg -> arg.type + " " + arg.name)
                .collect(Collectors.joining(", "))
        );
        write(") {\n");
        write(args.getArguments().stream()
                .map(arg -> "\t\tthis." + arg.name + " = " + arg.name + ";")
                .collect(Collectors.joining("\n"))
        );

        write("\n\t}\n");

        write("\n\t@Override");
        write("\n\tpublic void render(java.io.Writer out) throws java.io.IOException {\n");

        textOrExpr();

        if (state==State.Text) {
            write("\"");
        }
        write(");");

        write("\n\t}\n");

        write("\n\n}\n");
        return compilationResult;
    }

    private final static TemplateArgs NO_ARGS = new TemplateArgs(Collections.emptyList());

    private TemplateArgs templateArguments() {
        char c = read();
        switch (c) {
            case EOF:
                return NO_ARGS;
            case '<':
                return argsPercent();
            default:
                handleText(State.Text, c);
                return NO_ARGS;
        }
    }

    private TemplateArgs argsPercent() {
        char c = read();
        switch (c) {
            case EOF:
                handleText(State.Text, '<');
                break;
            case '%':
                return argsParen();
            default:
                handleText(State.Text, '<', c);
        }
        return NO_ARGS;
    }

    private TemplateArgs argsParen() {
        char c = read();
        switch (c) {
            case EOF:
                handleText(State.Text, '<', '%');
                break;
            case '(':
                // args declaration
                return arguments();
            default:
                handleText(State.Text, '<', '%', c);
        }
        return NO_ARGS;
    }

    private TemplateArgs arguments() {
        try {
            // make it simpler : we buffer it all until the end of the declaration
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            char c = read();
            while (c != EOF && c != ')') {
                bos.write(c);
                c = read();
            }

            String argsStr = bos.toString("utf-8");
            String[] argsParts = argsStr.split(",");
            List<TemplateArgs.Arg> args = new ArrayList<>();
            for (String argStr : argsParts) {
                String[] argParts = argStr.split(":");
                String argName = argParts[0].trim();
                String argVal = argParts[1].trim();
                args.add(new TemplateArgs.Arg(argName, argVal));
            }

            // make sure attributes are properly closed with ')%>'
            c = read();
            if (c != '%') {
                error("Arguments not properly closed.");
                return NO_ARGS;
            }
            c = read();
            if (c != '>') {
                error("Arguments not properly closed.");
                return NO_ARGS;
            }

            // TODO very ugly stuff indeed !
            c = read();
            if (c != '\n') {
                error("Arguments should be followed by a carriage return.");
                return NO_ARGS;
            }

            return new TemplateArgs(args);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    private String[] getPkgAndClassName() {
        String packageName;
        String className;
        int dotIndex = fqn.lastIndexOf('.');
        if (dotIndex == -1) {
            packageName = null;
            className = fqn;
        } else {
            packageName = fqn.substring(0, dotIndex);
            className = fqn.substring(dotIndex + 1);
        }
        return new String[]{packageName, className};
    }

    private void textOrExpr() {
        char c = read();
        switch (c) {
            case EOF:
                break;
            case '<':
                exprPercent();
                break;
            default:
                // not an expression start
                handleText(State.Text, c);
                textOrExpr();
        }
    }

    private void exprPercent() {
        char c = read();
        switch (c) {
            case EOF:
                handleText(State.Text, '<');
                break;
            case '%':
                char c2 = read();
                switch (c2) {
                    case '=':
                        // it's an expression start
                        startExpr();
                        break;
                    default:
                        // not an expression start, it's
                        // code...
                        startCode();
                        break;
                }
                break;
            default:
                // not an expression start anyway
                handleText(State.Text, '<', c);
                textOrExpr();
                break;
        }
    }

    private void startCode() {
        // TODO
    }

    private void startExpr() {
        expression();
    }

    private void expression() {
        char c = read();
        switch (c) {
            case EOF:
                error("Expression started but not closed");
                break;
            case '%':
                char c2 = read();
                switch (c2) {
                    case EOF:
                        error("Expression started but not closed");
                        break;
                    case '>':
                        textOrExpr();
                        break;
                    default:
                        handleText(State.Expr, '%', c2);
                        expression();
                        break;
                }
                break;
            default:
                handleText(State.Expr, c);
                expression();
                break;
        }
    }

    private void error(String s) {
        compilationResult.addError(cur, s);
    }

    private void handleText(State newState, char... chars) {
        if (state != newState) {
            if (state != null) {
                if (state==State.Text) {
                    write("\"");
                }
                write(" );\n");
            }
            write("\t\twrite(out, ");
            if (newState == State.Text) {
                write("\"");
            }
        }
        state = newState;
        try {
            for (char c : chars) {
                if (state == State.Text) {
                    switch (c) {
                        case '\n' :
                            out.write("\\n");
                            break;
                        case '"' :
                            out.write("\\\"");
                            break;
                        default:
                            out.write(c);
                    }
                } else {
                    out.write(c);
                }
            }
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}




