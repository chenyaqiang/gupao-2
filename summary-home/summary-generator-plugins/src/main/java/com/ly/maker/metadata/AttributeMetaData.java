package com.ly.maker.metadata;

/**
 * The type Attribute data.
 *
 * @author zhangliang
 * @version Id : AttributeMetaData, v 0.1 2018/5/3 14:48 zhangliang Exp $
 */
public class AttributeMetaData extends BaseMetaData {
    /** 是否是必须 */
    private boolean  required;

    /** 属性的描述 */
    private String   desc;

    /** 数据类型 */
    private String   type;

    private Class<?> fullType;

    private boolean  isBaseType;

    /**
     * Getter method for property <tt>required</tt>.
     *
     * @return property value of required
     */
    public boolean isRequired() {
        return required;
    }

    /**
     * Setter method for property <tt>required</tt>.
     *
     * @param required value to be assigned to property required
     */
    public void setRequired(boolean required) {
        this.required = required;
    }

    /**
     * Getter method for property <tt>desc</tt>.
     *
     * @return property value of desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Setter method for property <tt>desc</tt>.
     *
     * @param desc value to be assigned to property desc
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * Getter method for property <tt>type</tt>.
     *
     * @return property value of type
     */
    public String getType() {
        return type;
    }

    /**
     * Setter method for property <tt>type</tt>.
     *
     * @param type value to be assigned to property type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Getter method for property <tt>fullType</tt>.
     *
     * @return property value of fullType
     */
    public Class<?> getFullType() {
        return fullType;
    }

    /**
     * Setter method for property <tt>fullType</tt>.
     *
     * @param fullType value to be assigned to property fullType
     */
    public void setFullType(Class<?> fullType) {
        this.fullType = fullType;
    }

    /**
     * Getter method for property <tt>isBaseType</tt>.
     *
     * @return property value of isBaseType
     */
    public boolean isBaseType() {
        return isBaseType;
    }

    /**
     * Setter method for property <tt>baseType</tt>.
     *
     * @param baseType value to be assigned to property baseType
     */
    public void setBaseType(boolean baseType) {
        isBaseType = baseType;
    }
}
