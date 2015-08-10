package com.pojosontheweb.ttt;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TttCompiler {

    public static TttCompilationResult compile(Path srcDir, Path targetDir, boolean clean) throws CompilationFailedException {

        TttCompilationResult result = new TttCompilationResult();
        long start = System.currentTimeMillis();
        try {

            File target = targetDir.toFile();
            if (clean) {
                delete(target);
            }

            if (!target.exists()) {
                target.mkdirs();
            }

            final PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:*.ttt");
            Files.walkFileTree(srcDir, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    try {
                        if (attrs.isRegularFile() && matcher.matches(file.getFileName())) {
                            Path relativePath = srcDir.relativize(file);
                            String fqn = "";
                            Iterator<Path> it = relativePath.iterator();
                            String fileName = targetDir.toString();
                            while (it.hasNext()) {
                                Path p = it.next();
                                String pFileName = p.getFileName().toString();
                                if (it.hasNext()) {
                                    fqn += pFileName + ".";
                                    fileName += File.separator + pFileName;
                                } else {
                                    fqn += pFileName.replace(".ttt", "");
                                    fileName += File.separator + pFileName.replace(".ttt", ".java");
                                }
                            }

                            File outFile = new File(fileName);
                            File outParent = outFile.getParentFile();
                            if (!outParent.exists()) {
                                outParent.mkdirs();
                            }

                            try (FileReader in = new FileReader(file.toFile())) {
                                try (FileWriter out = new FileWriter(outFile)) {
                                    try {
                                        result.add(file.toFile().getAbsolutePath(), outFile.getAbsolutePath(), compile(in, out, fqn));
                                    } catch (Exception e) {
                                        throw new CompilationFailedException(e);
                                    }
                                }
                            }

                        }
                        return super.visitFile(file, attrs);
                    } catch (Exception e) {
                        throw new CompilationFailedException(e);
                    }
                }
            });
        } catch(Exception e) {
            throw new CompilationFailedException(e);
        }

        result.setElapsed(System.currentTimeMillis() - start);
        return result;
    }


    public static List<TttCompileError> compile(Reader in, Writer out, String fqn) throws CompilationFailedException {
        TttParserErrorListener listener = new TttParserErrorListener();
        try {
            TttListener l = new TttListener(out, fqn);
            ANTLRInputStream input = new ANTLRInputStream(in); // create a lexer that feeds off of input CharStream
            TttLexer lexer = new TttLexer(input); // create a buffer of tokens pulled from the lexer
            lexer.addErrorListener(listener);
            CommonTokenStream tokens = new CommonTokenStream(lexer); // create a parser that feeds off the tokens buffer
            TttParser parser = new TttParser(tokens);
            parser.addErrorListener(listener);
            ParseTree tree = parser.r(); // begin parsing at init rule
            ParseTreeWalker w = new ParseTreeWalker();
            w.walk(l, tree);
        } catch (Exception e) {
            throw new CompilationFailedException(e);
        }
        return listener.getErrors();
    }

    private static void delete(File file)
        throws IOException {

        if (file.isDirectory()) {

            //directory is empty, then delete it
            if (file.list().length == 0) {

                file.delete();

            } else {

                //list all the directory contents
                String files[] = file.list();

                for (String temp : files) {
                    //construct the file structure
                    File fileDelete = new File(file, temp);

                    //recursive delete
                    delete(fileDelete);
                }

                //check the directory again, if empty then delete it
                if (file.list().length == 0) {
                    file.delete();
                }
            }

        } else {
            //if file, then delete it
            file.delete();
        }
    }

    @FunctionalInterface
    interface RunnableThrowingException {
        void run() throws Exception;
    }

    public static void convertExceptions(RunnableThrowingException r) {
        try {
            r.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}




