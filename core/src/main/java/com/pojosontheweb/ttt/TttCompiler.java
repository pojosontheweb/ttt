package com.pojosontheweb.ttt;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Iterator;

public class TttCompiler {

    public static CompilationResult compile(CompilationArgs args) {
        CompilationContext cc = new CompilationContext(args);
        return cc.compile();
    }

    public static void main(String[] args) throws Exception {
        OptionParser parser = new OptionParser();
        parser.accepts("help");
        parser.accepts("clean");
        OptionSpec<File> optSrc = parser
            .accepts("src")
            .withRequiredArg().ofType(File.class);
        OptionSpec<File> optTarget = parser
            .accepts("target")
            .withRequiredArg().ofType(File.class);
        OptionSet options = parser.parse(args);

        if (!options.hasOptions() || options.has("help")) {
            parser.printHelpOn(System.out);
            return;
        }

        Path srcDir = options.valueOf(optSrc).toPath();
        Path targetDir = options.valueOf(optTarget).toPath();

        try (Writer log = new PrintWriter(System.out)) {

            log.write("Ttt compiler starting :\n\t- src\t : " + srcDir +
                "\n\t- target : " + targetDir + "\n");

            File target = targetDir.toFile();
            if (options.has("clean")) {
                if (target.exists()) {
                    log.write("Cleaning target\n");
                    delete(target);
                }
            }

            if (!target.exists()) {
                target.mkdirs();
            }

            final PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:*.ttt");
            Files.walkFileTree(srcDir, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (attrs.isRegularFile() && matcher.matches(file.getFileName())) {
                        Path relativePath = srcDir.relativize(file);
                        log.write("- " + relativePath + "\n");
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

                                CompilationArgs args = new CompilationArgs(in, out, fqn);
                                CompilationContext cc = new CompilationContext(args);
                                cc.compile();

                            }
                        }

                    }
                    return super.visitFile(file, attrs);
                }
            });
        }


//        CompilationArgs args = new CompilationArgs(in, out, fqn)
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


}




