package com.pojosontheweb.ttt;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Mojo(
    name = "ttt",
    defaultPhase = LifecyclePhase.GENERATE_SOURCES,
    requiresDependencyResolution = ResolutionScope.COMPILE
)
public class TttMojo extends AbstractMojo {

    @Parameter(defaultValue = "${basedir}/src/main/ttt")
    private File sourceDirectory;

    @Parameter(defaultValue = "${project.build.directory}/generated-sources/ttt")
    private File outputDirectory;

    @Parameter(property = "project", required = true, readonly = true)
    protected MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        Log log = getLog();

        log.info("\"TTT : compiler starting :\n\t- src\t : " + sourceDirectory.getAbsolutePath() +
            "\n\t- dest : " + outputDirectory.getAbsolutePath() + "\n");


        try {
            TttCompilationResult result = TttCompiler.compile(sourceDirectory.toPath(), outputDirectory.toPath(), true);
            int nbOk = 0, nbKo = 0;
            for (TttCompilationResult.TttTemplateResult r : result.getResults()) {
                if (r.hasErrors()) {
                    for (TttCompileError err : r.getErrors()) {
                        log.error("TTT : " + r.getTemplateFileName() + " (" + err.getLine() + "," + err.getCharInLine() + ") : " + err.getMessage());
                    }
                    nbKo++;
                } else {
                    nbOk++;
                    log.info("TTT : " + r.getTemplateFileName() + " -> " + r.getGeneratedFileName());
                }
            }
            if (result.hasErrors()) {
                log.error("TTT : " + nbOk + " OK, " + nbKo + " failed, took " + result.getElapsed() + "ms");
                throw new MojoFailureException("TTT Compilation error(s)");
            } else {
                log.info("TTT : " + nbOk + " templates compiled, took " + result.getElapsed() + "ms");
            }

        } catch (Exception e) {
            throw new MojoExecutionException("unable to compile ttt", e);
        }

        if (project != null) {
            project.addCompileSourceRoot(outputDirectory.getPath());
        }

        log.info("template(s) compiled");
    }
}
