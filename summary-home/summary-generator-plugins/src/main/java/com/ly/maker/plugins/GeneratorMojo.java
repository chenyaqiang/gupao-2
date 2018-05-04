package com.ly.maker.plugins;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;
import java.util.*;

import com.ly.maker.writer.FileWriterImpl;
import com.ly.maker.writer.PrintWriteImpl;
import com.ly.maker.writer.Writer;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.ly.maker.annotations.DataType;
import com.ly.maker.annotations.IgnoreSubType;
import com.ly.maker.annotations.ListType;
import com.ly.maker.annotations.SummaryRoot;
import com.ly.maker.enums.NodeType;
import com.ly.maker.format.Formatter;
import com.ly.maker.format.StringFormatter;
import com.ly.maker.metadata.FieldMetaData;
import com.ly.maker.metadata.TypeMetaData;
import com.ly.maker.model.ChildNode;
import com.ly.maker.model.Node;
import com.ly.maker.model.RootNode;

/**
 * The type Generator mojo.
 */
@Mojo(name = "generator")
public class GeneratorMojo extends AbstractMojo {

    /** 需要生成文档的主类文件列表,将尝试为列表中每个指定的类生成接口说明文档 */
    @Parameter
    private List<String>                  packageFileList;

    @Parameter
    private String                        classPath;

    @Parameter
    private String                        libDir;

    //全局类加载器
    private URLClassLoader                loader;

    private Formatter<Node, List<String>> formatter = new StringFormatter();
    private Writer<List<String>>          write     = new FileWriterImpl();

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (this.packageFileList == null || this.packageFileList.size() == 0) {
            System.out.println("nothing to generate.");
            return;
        }

        List<String> finalFileList = this.prepareFilePathList();
        try {
            initLoader();
            for (String fileItem : finalFileList) {
                System.out.println("----------");
                System.out.println("package name=" + fileItem);
                System.out.println(Thread.currentThread().getContextClassLoader());
                System.out.println("----------");
                Class<?> aClass = Class.forName(fileItem, true, loader);
                if (!aClass.isAnnotationPresent(SummaryRoot.class)) {
                    continue;
                }

                SummaryRoot annotation = aClass.getAnnotation(SummaryRoot.class);
                Node parentNode = new RootNode();
                parentNode.setName(parentNode.getName());
                parentNode.setNodeType(NodeType.Parent);

                this.fillNodeWithRecursive(parentNode, aClass);
                this.write.save(formatter.format(parentNode));
                System.out.println("end--hahahah");
            }
        } catch (ClassNotFoundException | IOException ex) {
            throw new MojoExecutionException("GeneratorMojo执行异常", ex);
        }
    }

    private void initLoader() throws IOException {
        String basePath = (new URL("file", null, new File(classPath).getCanonicalPath() + File.separator)).toString();
        String libPath = (new URL("file", null, new File(libDir).getCanonicalPath() + File.separator)).toString();

        System.out.println("bashPath=" + basePath);
        System.out.println("libPath=" + libPath);
        URLStreamHandler sh = null;
        List<URL> libs = new ArrayList<>();
        File libDir = new File(libPath.replaceAll("file:", ""));
        if (libDir.listFiles() != null) {
            for (File jar : libDir.listFiles()) {
                libs.add(new URL(null, libPath + jar.getName(), sh));
            }
        }

        libs.add(new URL(null, basePath, sh));
        loader = new URLClassLoader(libs.toArray(new URL[libs.size()]), Thread.currentThread().getContextClassLoader());
    }

    /**
     * 单文件递归构建(当类嵌套引用自己的时候,可能出现死循环)
     */
    private void fillNodeWithRecursive(Node parentNode, Class<?> aClass) throws ClassNotFoundException {
        Objects.requireNonNull(parentNode, "parentNode should not be null.");
        Objects.requireNonNull(aClass, "aClass should not be null.");
        List<Node> currentNodeList = new ArrayList<>();
        parentNode.setNodes(currentNodeList);
        List<FieldMetaData> attributeMetaDataList = this.getFiledMetaDataList(aClass);
        for (FieldMetaData attributeMetaData : attributeMetaDataList) {
            ChildNode childNode = new ChildNode();
            childNode.setName(attributeMetaData.getName());
            childNode.setDescription(attributeMetaData.getDesc());
            childNode.setDataType(attributeMetaData.getType());
            childNode.setRequired(attributeMetaData.isRequired());
            childNode.setNodeType(NodeType.Children);
            if (attributeMetaData.isHasChildren()) {
                // 尝试发现嵌套引用,并规避嵌套引用可能引起的死循环问题.(只能规避相邻层的递归嵌套引用,还是有bug)
                // TODO 待处理递归引用的bug.
                if (!aClass.getTypeName().equals(attributeMetaData.getNextType().getTypeName())) {
                    this.fillNodeWithRecursive(childNode, attributeMetaData.getNextType());
                }
            }

            currentNodeList.add(childNode);
        }

    }

    private TypeMetaData getTypeMetadata(Class<?> clazz) {
        Objects.requireNonNull(clazz, "clazz should not be null.");
        if (!clazz.isAnnotationPresent(SummaryRoot.class)) {
            return null;
        }

        SummaryRoot annotation = clazz.getAnnotation(SummaryRoot.class);
        return new TypeMetaData() {
            {
                setName(annotation.name());
            }
        };
    }

    /**
     * 解析某个类型下的所有可可解析的字段及注解内容.
     * 
     * @param clazz clazz
     * @return field
     * @throws ClassNotFoundException excepiton
     */
    private List<FieldMetaData> getFiledMetaDataList(Class<?> clazz) throws ClassNotFoundException {
        Objects.requireNonNull(clazz, "clazz should not be null.");
        Field[] declaredFields = clazz.getDeclaredFields();
        List<FieldMetaData> attributeMetaDataList = new ArrayList<>();
        if (declaredFields == null || declaredFields.length == 0) {
            return attributeMetaDataList;
        }

        Map<String, String> methods = getMethods(clazz.getMethods());
        for (Field declaredField : declaredFields) {
            if (!methods.containsKey("get" + declaredField.getName().toLowerCase())) {
                continue;
            }

            DataType annotation = null;
            if (declaredField.isAnnotationPresent(DataType.class)) {
                annotation = declaredField.getAnnotation(DataType.class);
            }

            IgnoreSubType ignoreSubType = null;
            ListType listType = null;
            if (declaredField.isAnnotationPresent(IgnoreSubType.class)) {
                ignoreSubType = declaredField.getAnnotation(IgnoreSubType.class);
            }

            if (declaredField.isAnnotationPresent(ListType.class)) {
                listType = declaredField.getAnnotation(ListType.class);
            }

            attributeMetaDataList.add(this.getFiledMeta(declaredField, annotation, ignoreSubType, listType));
        }

        return attributeMetaDataList;
    }

    private Map<String, String> getMethods(Method[] methods) {
        Map<String, String> maps = new HashMap<>();
        for (Method method : methods) {
            maps.put(method.getName().toLowerCase(), "");
        }
        return maps;
    }

    private FieldMetaData getFiledMeta(Field field, DataType annotation, IgnoreSubType ignoreSubType, ListType listType) throws ClassNotFoundException {
        FieldMetaData metaData = new FieldMetaData() {
            {
                setName(field.getName());
                setDesc("暂无");
                setRequired(false);
                setType(field.getType().getSimpleName());
                setHasChildren(false);
                setNextType(null);
            }
        };

        if (annotation != null) {
            // 默认填充字段名.
            metaData.setName(StringUtils.isEmpty(annotation.name()) ? field.getName() : annotation.name());
            metaData.setDesc(annotation.description());
            metaData.setRequired(annotation.required());
        }

        if (ignoreSubType != null) {
            // 忽略对子类的进一步解析,直接返回
            return metaData;
        }

        Class<?> finalType = isListType(field.getType()) ? getActualType(field, 0) : field.getType();
        if (isBaseDataType(finalType)) {
            metaData.setHasChildren(false);
            metaData.setNextType(null);
            return metaData;
        }

        metaData.setHasChildren(true);
        metaData.setNextType((finalType));
        return metaData;
    }

    private Class<?> getActualType(Field field, int i) {
        Type[] actualTypeArguments = ((ParameterizedType) field.getGenericType()).getActualTypeArguments();
        return (Class<?>) actualTypeArguments[i];
    }

    private boolean isListType(Class<?> type) {
        return type == List.class;
    }

    /** 获取需要扫描的文件路径,预留扩展,方便以后对整个路径的扫描支持. */
    private List<String> prepareFilePathList() {
        return this.packageFileList;
    }

    /**
     * 判断一个类是否为基本数据类型。
     * @param clazz 要判断的类。
     * @return true 表示为基本数据类型。
     */
    private static boolean isBaseDataType(Class clazz) {
        return (clazz.equals(String.class) || clazz.equals(Integer.class) || clazz.equals(Byte.class) || clazz.equals(Long.class) || clazz.equals(Double.class)
                || clazz.equals(Float.class) || clazz.equals(Character.class) || clazz.equals(Short.class) || clazz.equals(BigDecimal.class) || clazz.equals(BigInteger.class)
                || clazz.equals(Boolean.class) || clazz.equals(Date.class) || clazz.isPrimitive());
    }

    //    public static void main(String[] args) throws ClassNotFoundException, MojoFailureException, MojoExecutionException {
    //        GeneratorMojo mojo = new GeneratorMojo();
    //        mojo.execute();
    //    }
}
