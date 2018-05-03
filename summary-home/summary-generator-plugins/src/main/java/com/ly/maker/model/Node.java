package com.ly.maker.model;

import com.ly.maker.enums.NodeType;

import java.util.List;

/**
 * The type Node.
 *
 * @author zhangliang
 * @version Id : Node, v 0.1 2018/5/3 15:27 zhangliang Exp $
 */
public abstract class Node {
    private String     name;

    private List<Node> nodes;

    private NodeType   nodeType;

    /**
     * Getter method for property <tt>name</tt>.
     *
     * @return property value of name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter method for property <tt>name</tt>.
     *
     * @param name value to be assigned to property name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter method for property <tt>nodes</tt>.
     *
     * @return property value of nodes
     */
    public List<Node> getNodes() {
        return nodes;
    }

    /**
     * Setter method for property <tt>nodes</tt>.
     *
     * @param nodes value to be assigned to property nodes
     */
    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    /**
     * Getter method for property <tt>nodeType</tt>.
     *
     * @return property value of nodeType
     */
    public NodeType getNodeType() {
        return nodeType;
    }

    /**
     * Setter method for property <tt>nodeType</tt>.
     *
     * @param nodeType value to be assigned to property nodeType
     */
    public void setNodeType(NodeType nodeType) {
        this.nodeType = nodeType;
    }

    public abstract String format();
}
