package com.ly.maker.annotations;

import java.lang.annotation.*;

/**
 * The interface Summary attr.
 *
 * @author zhangliang
 * @version Id : DataType, v 0.1 2018/5/3 10:21 zhangliang Exp $
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DataType {
    /**
     * Name string.
     *
     * @return the string
     */
    String name() default "";

    /**
     * Required boolean.
     *
     * @return the boolean
     */
    boolean required() default false;

    /**
     * Description string.
     *
     * @return the string
     */
    String description() default "";
}
