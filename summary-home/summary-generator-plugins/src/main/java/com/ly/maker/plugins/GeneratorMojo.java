package com.ly.maker.plugins;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.ly.maker.annotations.SummaryAttr;
import com.ly.maker.metadata.AttributeMetaData;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * The type Generator mojo.
 */
@Mojo(name = "generator")
public class GeneratorMojo extends AbstractMojo {

    /** 需要生成文档的主类文件列表,将尝试为列表中每个指定的类生成接口说明文档 */
    @Parameter
    private List<String> packageFileList;

    /**
     * Perform whatever build-process behavior this <code>Mojo</code> implements.<br>
     * This is the main trigger for the <code>Mojo</code> inside the <code>Maven</code> system, and allows
     * the <code>Mojo</code> to communicate errors.
     *
     * @throws MojoExecutionException if an unexpected problem occurs.
     *                                Throwing this exception causes a "BUILD ERROR" message to be displayed.
     * @throws MojoFailureException   if an expected problem (such as a compilation failure) occurs.
     *                                Throwing this exception causes a "BUILD FAILURE" message to be displayed.
     */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (this.packageFileList == null || this.packageFileList.size() == 0) {
            System.out.println("nothing to generate.");
            return;
        }

        List<String> finalFileList = this.prepareFilePathList();

    }

    private List<AttributeMetaData> analysisMetaData(Class<?> clazz) {
        Objects.requireNonNull(clazz, "clazz should not be null.");
        Field[] declaredFields = clazz.getDeclaredFields();
        List<AttributeMetaData> attributeMetaDataList = new ArrayList<>();
        if (declaredFields == null || declaredFields.length == 0) {
            return attributeMetaDataList;
        }

        for (Field declaredField : declaredFields) {
            if (!declaredField.isAnnotationPresent(SummaryAttr.class)) {
                continue;
            }

            SummaryAttr annotation = declaredField.getAnnotation(SummaryAttr.class);
            attributeMetaDataList.add(new AttributeMetaData() {
                {
                    setName(annotation.name());
                    setDesc(annotation.description());
                    setRequired(annotation.required());
                }
            });
        }

        return attributeMetaDataList;
    }

    /** 获取需要扫描的文件路径,预留扩展,方便以后对整个路径的扫描支持. */
    private List<String> prepareFilePathList() {
        return this.packageFileList;
    }

    //        public static void main(String[] args) throws ClassNotFoundException {
    //            Class<?> aClass = Class.forName("com.ly.maker.test");
    //            for (Field field : aClass.getDeclaredFields()) {
    //                System.out.println(field.getName());
    //                System.out.println(field);
    //            }
    //            System.out.println(aClass.getName());
    //        }
}
