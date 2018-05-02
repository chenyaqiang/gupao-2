package com.gupao.learner.mrzhang;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * The type Scan mojo.
 *
 * @author zhangliang
 * @version Id : ScanMojo, v 0.1 2018/5/2 15:52 zhangliang Exp $
 */
// 在声明goal时，指定goal默认的phase,则在使用方未配置phase时，将会使用goal的defaultPhase填充.
@Mojo(name = "scan", defaultPhase = LifecyclePhase.COMPILE)
public class ScanMojo extends AbstractMojo {

    /** the path which used as scan entry */
    @Parameter(property = "path")
    private String                scanPath;

    @Parameter(property = "targetSuffix", defaultValue = ".java")
    private String                targetSuffix;

    /** set a filter rule*/
    private final Predicate<File> filter = file -> {
                                             String fileName = file.getName();
                                             String suffix = fileName.substring(fileName.lastIndexOf("."));
                                             return this.targetSuffix.equals(suffix);
                                         };

    /**
     * Setter method for property <tt>scanPath</tt>.
     *
     * @param scanPath value to be assigned to property scanPath
     */
    public void setScanPath(String scanPath) {
        this.scanPath = scanPath;
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (StringUtils.isEmpty(this.scanPath)) {
            return;
        }
        System.out.println(MessageFormat.format("scan path is:{0}", this.scanPath));
        List<String> lstFiles = doScan(this.scanPath);
        System.out.println("The total files size is:" + lstFiles.size());
        System.out.println("----- list details are those below -----");
        lstFiles.forEach(System.out::println);
    }

    /**
     * do scan file with recursive.
     * @param scanPath scan path.
     * @return file name array
     */
    private List<String> doScan(String scanPath) {
        List<String> lstFileName = new ArrayList<>();
        File file = new File(scanPath);
        File[] files = file.listFiles();
        Objects.requireNonNull(files, "files can not be null.");
        for (File f : files) {
            if (f.isDirectory()) {
                // 开启递归.
                lstFileName.addAll(this.doScan(f.getPath()));
                continue;
            }

            // 执行过滤.
            if (!this.filter.test(f)) {
                continue;
            }

            lstFileName.add(f.getPath());
        }
        return lstFileName;
    }

    //    public static void main(String[] args) throws MojoFailureException, MojoExecutionException {
    //        ScanMojo scanMojo = new ScanMojo();
    //        scanMojo.setScanPath("/Users/zhangliang/Workspaces/Code/github/zhangliang/homework/gupao/mrzhang-homework/practice-180502-demo-plugin");
    //        scanMojo.execute();
    //    }
}