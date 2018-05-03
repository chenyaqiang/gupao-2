package com.ly.maker.annotations;

import java.lang.annotation.*;

/**
 * The interface Summary root.
 *
 * @author zhangliang
 * @version Id : SummaryRoot, v 0.1 2018/5/3 10:15 zhangliang Exp $
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
@Inherited
public @interface SummaryRoot {
    /**
     * annotation name.
     *
     * @return name string
     */
    String name();

    /**
     * Description string.
     *
     * @return the string
     */
    String description();
}
