package com.ly.maker.format;

/**
 * @author zhangliang
 * @version Id: Formatter, v 0.1 2018/5/3 16:46 zhangliang Exp $
 */
public interface Formatter<TSource, TTarget> {
    TTarget format(TSource source);
}
