package com.ly.maker;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * The type Generator mojo.
 */
@Mojo(name = "generator", defaultPhase = LifecyclePhase.PACKAGE)
public class GeneratorMojo extends AbstractMojo {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        System.out.println("Hello World!");
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

    }
}
