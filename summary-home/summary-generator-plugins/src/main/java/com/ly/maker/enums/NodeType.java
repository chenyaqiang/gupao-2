package com.ly.maker.enums;

/**
 * @author zhangliang
 * @version Id: NodeType, v 0.1 2018/5/3 15:38 zhangliang Exp $
 */
public enum NodeType {
                      Parent(0), Children(1),;
    private Integer value;

    NodeType(Integer value) {
        this.value = value;
    }
}
