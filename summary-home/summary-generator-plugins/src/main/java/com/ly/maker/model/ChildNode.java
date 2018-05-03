package com.ly.maker.model;

/**
 * The type Child node.
 *
 * @author zhangliang
 * @version Id : ChildNode, v 0.1 2018/5/3 15:41 zhangliang Exp $
 */
public class ChildNode extends Node {
    private String  dataType;
    private String  description;
    private boolean required;

    /**
     * Getter method for property <tt>dataType</tt>.
     *
     * @return property value of dataType
     */
    public String getDataType() {
        return dataType;
    }

    /**
     * Setter method for property <tt>dataType</tt>.
     *
     * @param dataType value to be assigned to property dataType
     */
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    /**
     * Getter method for property <tt>description</tt>.
     *
     * @return property value of description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter method for property <tt>description</tt>.
     *
     * @param description value to be assigned to property description
     */
    public void setDescription(String description) {
        this.description = description;
    }

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
}
