package com.ly.maker.annotations;

/**
 * The interface Summary complex attr.
 *
 * @author zhangliang
 * @version Id : SummaryComplexAttr, v 0.1 2018/5/3 10:21 zhangliang Exp $
 */
public @interface SummaryComplexAttr {
    /**
     * Name string.
     *
     * @return the string
     */
    String name();

    /**
     * Required boolean.
     *
     * @return the boolean
     */
    boolean required();

    /**
     * Description string.
     *
     * @return the string
     */
    String description();
}