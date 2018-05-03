package com.ly.maker.plugins;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import com.ly.maker.annotations.DataType;
import com.ly.maker.format.Formatter;
import com.ly.maker.format.StringFormatter;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.ly.maker.annotations.SummaryRoot;
import com.ly.maker.enums.NodeType;
import com.ly.maker.metadata.AttributeMetaData;
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
    private List<String>            packageFileList;

    private Formatter<Node, String> formatter = new StringFormatter();

    public GeneratorMojo() {
        this.packageFileList = Collections.singletonList("com.ly.maker.test.test");
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (this.packageFileList == null || this.packageFileList.size() == 0) {
            System.out.println("nothing to generate.");
            return;
        }

        List<String> finalFileList = this.prepareFilePathList();
        try {
            for (String fileItem : finalFileList) {
                Node node = this.createNodeWithRecursive(Class.forName(fileItem));
                System.out.println(formatter.format(node));
                System.out.println("end");
            }
        } catch (ClassNotFoundException ex) {
            throw new MojoExecutionException("GeneratorMojo执行异常", ex);
        }
    }

    /**
     * 单文件递归构建(当类嵌套引用自己的时候,可能出现死循环)
     * @return node
     */
    private Node createNodeWithRecursive(Class<?> aClass) throws ClassNotFoundException {
        TypeMetaData typeMetadata = getTypeMetadata(aClass);
        if (typeMetadata == null) {
            return null;
        }

        List<Node> currentNodeList = new ArrayList<>();
        RootNode rootNode = new RootNode();
        rootNode.setNodeType(NodeType.Parent);
        rootNode.setName(typeMetadata.getName());
        rootNode.setNodes(currentNodeList);

        List<AttributeMetaData> attributeMetaDataList = this.getAttrMetaData(aClass);
        for (AttributeMetaData attributeMetaData : attributeMetaDataList) {
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
                    Node nodeWithRecursive = this.createNodeWithRecursive(attributeMetaData.getNextType());
                    if (nodeWithRecursive != null) {
                        childNode.setNodes(Collections.singletonList(nodeWithRecursive));
                    }
                }
            }

            currentNodeList.add(childNode);
        }

        return rootNode;
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

    private List<AttributeMetaData> getAttrMetaData(Class<?> clazz) throws ClassNotFoundException {
        Objects.requireNonNull(clazz, "clazz should not be null.");
        Field[] declaredFields = clazz.getDeclaredFields();
        List<AttributeMetaData> attributeMetaDataList = new ArrayList<>();
        if (declaredFields == null || declaredFields.length == 0) {
            return attributeMetaDataList;
        }

        for (Field declaredField : declaredFields) {
            if (!declaredField.isAnnotationPresent(DataType.class)) {
                continue;
            }

            DataType annotation = declaredField.getAnnotation(DataType.class);
            Class<?> nextType = StringUtils.isEmpty(annotation.itemType()) ? declaredField.getType() : Class.forName(annotation.itemType());
            attributeMetaDataList.add(new AttributeMetaData() {
                {
                    setName(StringUtils.isEmpty(annotation.name()) ? declaredField.getName() : annotation.name());
                    setDesc(annotation.description());
                    setRequired(annotation.required());
                    setType(declaredField.getType().getName());
                    setHasChildren(annotation.hasChild());//isBaseDataType(declaredField.getType())
                    setNextType(nextType);//declaredField.getType());
                }
            });
        }

        return attributeMetaDataList;
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
        // clazz.equals(DateTime.class) ||
    }

    public static void main(String[] args) throws ClassNotFoundException, MojoFailureException, MojoExecutionException {
        GeneratorMojo mojo = new GeneratorMojo();
        mojo.execute();
    }
}
