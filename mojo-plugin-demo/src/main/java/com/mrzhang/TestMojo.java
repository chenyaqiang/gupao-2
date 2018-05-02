package com.mrzhang;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Hello world!
 * 在声明goal时，指定goal默认的phase,则在使用方未配置phase时，将会使用goal的defaultPhase填充.
 */
@Mojo(name = "scan", defaultPhase = LifecyclePhase.COMPILE)
public class TestMojo extends AbstractMojo {

    /** the path which used as scan entry*/
    @Parameter(property = "path")
    private String scanPath;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        System.out.println("-----");
        System.out.println("haha,the scan path is:" + scanPath);
        System.out.println("-----");
    }
}
