package com.ly.maker.annotations;

import java.lang.annotation.*;

/**
 * The interface Summary attr.
 *
 * @author zhangliang
 * @version Id : SummaryAttr, v 0.1 2018/5/3 10:21 zhangliang Exp $
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
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
