package com.ly.maker.annotations;

import java.lang.annotation.*;

/**
 * @author zhangliang
 * @version Id: ListType, v 0.1 2018/5/4 14:12 zhangliang Exp $
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface ListType {
    String elementType() default "";
}
