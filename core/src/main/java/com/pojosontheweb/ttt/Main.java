package com.pojosontheweb.ttt;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import java.io.File;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.pojosontheweb.ttt.Util.toRtEx;

public class Main {

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

            TttCompilationResult result = TttCompiler.compile(srcDir, targetDir, options.has("clean"));
            log.write(result.toLines().collect(Collectors.joining("\n")));

            if (result.hasErrors()) {
                log.write("Compilation failed (took " + result.getElapsed() + "ms)\n");
            } else {
                log.write("Templates compiled (took " + result.getElapsed() + "ms)\n");
            }
        }
    }

}
