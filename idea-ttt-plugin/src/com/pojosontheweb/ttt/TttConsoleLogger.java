package com.pojosontheweb.ttt;

import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.components.AbstractProjectComponent;
import com.intellij.openapi.project.Project;

public class TttConsoleLogger extends AbstractProjectComponent {

    private ConsoleView consoleView;
    private TttCompilationResult lastResult;

    public TttConsoleLogger(Project project) {
        super(project);
    }

    public void setConsoleView(ConsoleView consoleView) {
        this.consoleView = consoleView;
        if (lastResult != null) {
            log(lastResult);
        }
    }

    public void log(TttCompilationResult compilationResult) {
        if (consoleView == null) {
            lastResult = compilationResult;
        } else {
            consoleView.clear();
            if (compilationResult.hasErrors()) {
                consoleView.print("Compilation failed :\n", ConsoleViewContentType.ERROR_OUTPUT);
            } else {
                consoleView.print("Compilation succeeded :\n", ConsoleViewContentType.NORMAL_OUTPUT);
            }
            int nbOk = 0, nbKo = 0;
            for (TttCompilationResult.TttTemplateResult tr : compilationResult.getResults()) {
                if (tr.hasErrors()) {
                    nbKo++;
                    for (TttCompileError error : tr.getErrors()) {
                        consoleView.print(
                            tr.getTemplateFileName() + " (" + error.getLine() + "," + error.getCharInLine() + ") : " + error.getMessage() + "\n",
                            ConsoleViewContentType.ERROR_OUTPUT
                        );
                    }
                } else {
                    nbOk++;
                    consoleView.print(
                        tr.getTemplateFileName() + " -> " + tr.getGeneratedFileName() + "\n",
                        ConsoleViewContentType.NORMAL_OUTPUT
                    );
                }
            }
            consoleView.print("Compilation finished : " + nbOk + " OK, " + nbKo + " failed (took " + compilationResult.getElapsed() + " ms).\n", nbKo > 0 ? ConsoleViewContentType.ERROR_OUTPUT : ConsoleViewContentType.NORMAL_OUTPUT);
        }
    }


}
