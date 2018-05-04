package com.ly.maker.plugins;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import com.ly.maker.annotations.*;
import com.ly.maker.format.Formatter;
import com.ly.maker.format.StringFormatter;
import com.ly.maker.metadata.FieldMetaData;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.ly.maker.enums.NodeType;
import com.ly.maker.metadata.TypeMetaData;
import com.ly.maker.model.ChildNode;
import com.ly.maker.model.Node;
import com.ly.maker.model.RootNode;

import javax.lang.model.type.ReferenceType;

/**
 * The type Generator mojo.
 */
@Mojo(name = "generator")
public class GeneratorMojo extends AbstractMojo {

    /** 需要生成文档的主类文件列表,将尝试为列表中每个指定的类生成接口说明文档 */
    @Parameter
    private List<String>            packageFileList;

    private Formatter<Node, String> formatter = new StringFormatter();

    //    public GeneratorMojo() {
    //        this.packageFileList = Collections.singletonList("com.ly.maker.test.Student");
    //    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (this.packageFileList == null || this.packageFileList.size() == 0) {
            System.out.println("nothing to generate.");
            return;
        }

        List<String> finalFileList = this.prepareFilePathList();
        try {
            for (String fileItem : finalFileList) {
                Class<?> aClass = Class.forName(fileItem);
                if (!aClass.isAnnotationPresent(SummaryRoot.class)) {
                    continue;
                }

                SummaryRoot annotation = aClass.getAnnotation(SummaryRoot.class);
                Node parentNode = new RootNode();
                parentNode.setName(parentNode.getName());
                parentNode.setNodeType(NodeType.Parent);

                this.fillNodeWithRecursive(parentNode, aClass);
                System.out.println(formatter.format(parentNode));
                System.out.println("end");
            }
        } catch (ClassNotFoundException ex) {
            throw new MojoExecutionException("GeneratorMojo执行异常", ex);
        }
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

        for (Field declaredField : declaredFields) {
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

    private FieldMetaData getFiledMeta(Field field, DataType annotation, IgnoreSubType ignoreSubType, ListType listType) throws ClassNotFoundException {
        FieldMetaData metaData = new FieldMetaData() {
            {
                setName(field.getName());
                setDesc("");
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

        if (isBaseDataType(field.getType())) {
            metaData.setHasChildren(false);
            metaData.setNextType(null);
            return metaData;
        }

        if (isListType(field.getType())) {
            // 取出元素类型.
            Type[] actualTypeArguments = ((ParameterizedType) field.getGenericType()).getActualTypeArguments();
            Type actualTypeArgument = actualTypeArguments[0];
            if (!isBaseDataType(actualTypeArgument.getClass())) {
                metaData.setHasChildren(true);
                metaData.setNextType((Class) actualTypeArgument);
            }

            return metaData;
        }

        return metaData;
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
