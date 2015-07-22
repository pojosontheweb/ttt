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

        log.info("Ttt compiler starting :\n\t- src\t : " + sourceDirectory.getAbsolutePath() +
            "\n\t- dest : " + outputDirectory.getAbsolutePath() + "\n");

        try {
            TttCompiler.compile(sourceDirectory.toPath(), outputDirectory.toPath(), true);
        } catch (Exception e) {
            throw new MojoExecutionException("unable to compile ttt", e);
        }

        if (project != null) {
            project.addCompileSourceRoot(outputDirectory.getPath());
        }

        log.info("Ttt templates compiled");
    }
}
