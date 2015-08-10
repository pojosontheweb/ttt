package com.pojosontheweb.ttt;

import java.util.*;
import java.util.stream.Stream;

public class TttCompilationResult {

    private final List<TttTemplateResult> results;
    private long elapsed;

    public TttCompilationResult() {
        this(new ArrayList<>());
    }

    public TttCompilationResult(List<TttTemplateResult> results) {
        this.results = results;
    }

    public long getElapsed() {
        return elapsed;
    }

    public void setElapsed(long elapsed) {
        this.elapsed = elapsed;
    }

    public void add(String templateFullPath, String generatedFilePath, List<TttCompileError> errors) {
        results.add(
            new TttTemplateResult(
                templateFullPath,
                generatedFilePath,
                errors == null ? Collections.emptyList() : errors
            )
        );
    }

    public List<TttTemplateResult> getResults() {
        return results;
    }

    public boolean hasErrors() {
        return results.stream().anyMatch(TttTemplateResult::hasErrors);
    }

    public Stream<String> toLines() {
        return results.stream()
            .sorted()
            .map(TttTemplateResult::toLines)
            .reduce(Stream.<String>empty(), Stream::concat);
    }

    public static class TttTemplateResult implements Comparable<TttTemplateResult> {
        private final String templateFileName;
        private final String generatedFileName;
        private final List<TttCompileError> errors;

        public TttTemplateResult(String templateFileName, String generatedFileName, List<TttCompileError> errors) {
            this.templateFileName = templateFileName;
            this.generatedFileName = generatedFileName;
            this.errors = errors == null ? Collections.emptyList() : errors;
        }

        public String getTemplateFileName() {
            return templateFileName;
        }

        public String getGeneratedFileName() {
            return generatedFileName;
        }

        public List<TttCompileError> getErrors() {
            return errors;
        }

        public boolean hasErrors() {
            return !errors.isEmpty();
        }

        public Stream<String> toLines() {
            if (hasErrors()) {
                return errors.stream()
                    .map(e -> "[ERROR] " + templateFileName + " (" + e.getLine() + "," + e.getCharInLine() + ") : " + e.getMessage());

            } else {
                return Stream.of("[SUCCESS] " + templateFileName + " -> " + generatedFileName);
            }
        }

        @Override
        public int compareTo(TttTemplateResult o) {
            if (hasErrors() && o.hasErrors()) {
                return templateFileName.compareTo(o.templateFileName);
            } else if (hasErrors()) {
                return 1;
            } else if (o.hasErrors()) {
                return -1;
            } else {
                return templateFileName.compareTo(o.templateFileName);
            }
        }
    }

//    public class FileAndErrors {
//        private final File file;
//        private final List<TttCompileError> errors;
//
//        public FileAndErrors(File file, List<TttCompileError> errors) {
//            this.file = file;
//            this.errors = errors;
//        }
//
//        public File getFile() {
//            return file;
//        }
//
//        public List<TttCompileError> getErrors() {
//            return errors;
//        }
//
//        public boolean hasErrors() {
//            return !errors.isEmpty();
//        }
//
//        public Stream<String> toErrorLines() {
//            return errors.stream()
//                .map(err ->
//                        file.getAbsolutePath() + " (" + err.getLine() + "," + err.getCharInLine() + ") : " + err.getMessage()
//                );
//        }
//    }


}
