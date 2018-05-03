package com.ly.maker.annotations;

/**
 * The interface Summary attr.
 *
 * @author zhangliang
 * @version Id : SummaryAttr, v 0.1 2018/5/3 10:21 zhangliang Exp $
 */
public @interface SummaryAttr {
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
