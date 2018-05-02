package com.mrzhang;

import java.text.MessageFormat;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import javax.smartcardio.CardTerminal;

/**
 * @author zhangliang
 * @version Id: PrintMessageMojo, v 0.1 2018/5/2 11:31 zhangliang Exp $
 */
@Mojo(name = "print")
public class PrintMessageMojo extends AbstractMojo {
    public PrintMessageMojo() {
        System.out.println("begin-----");
        System.out.println(this.message == null ? "message is null" : "message=" + this.message);
        System.out.println(this.items == null ? "items is null" : "items=" + items);
        System.out.println("end-------");
    }

    @Parameter(property = "msg", defaultValue = "the default message provided by mojodemo's plugin")
    private String       message;

    @Parameter(property = "items")
    private List<String> items;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        System.out.println("-----");
        System.out.println(MessageFormat.format("The message value is :{0}", this.message));
        if (items != null) {
            System.out.println("--items detais--");
            for (String s : items) {
                System.out.println("item-value=" + s);
            }
        }
        System.out.println("-----");
    }
}
