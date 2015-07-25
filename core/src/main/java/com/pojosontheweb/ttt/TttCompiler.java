package com.pojosontheweb.ttt;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Collectors;

public class TttCompiler {

    public static void generateTemplateBaseClass(File target) throws IOException {
        // write out the base Template class source file
        File templateBase = new File(
            Arrays.asList(target.getAbsolutePath(), "com", "pojosontheweb", "ttt", "Template.java")
                .stream()
                .collect(Collectors.joining(File.separator))
        );
        templateBase.getParentFile().mkdirs();
        Files.copy(TttCompiler.class.getResourceAsStream("/com/pojosontheweb/ttt/Template.java"), templateBase.toPath());
    }

    public static void compile(Path srcDir, Path targetDir, boolean clean) throws Exception {

        File target = targetDir.toFile();
        if (clean) {
            delete(target);
        }

        if (!target.exists()) {
            target.mkdirs();
        }

        // write out the base Template class source file
        generateTemplateBaseClass(target);

        final PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:*.ttt");
        Files.walkFileTree(srcDir, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
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
                            compile(in, out, fqn);
                        }
                    }

                }
                return super.visitFile(file, attrs);
            }
        });

    }

    public static void compile(Reader in, Writer out, String fqn) throws IOException {
        TttListener l = new TttListener(out, fqn);
        ANTLRInputStream input = new ANTLRInputStream(in); // create a lexer that feeds off of input CharStream
        TttLexer lexer = new TttLexer(input); // create a buffer of tokens pulled from the lexer
        CommonTokenStream tokens = new CommonTokenStream(lexer); // create a parser that feeds off the tokens buffer
        TttParser parser = new TttParser(tokens);
        ParseTree tree = parser.r(); // begin parsing at init rule
        ParseTreeWalker w = new ParseTreeWalker();
        w.walk(l, tree);
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




