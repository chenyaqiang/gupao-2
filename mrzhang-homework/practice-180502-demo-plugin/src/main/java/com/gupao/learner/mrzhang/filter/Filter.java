package com.gupao.learner.mrzhang.filter;

/**
 * The interface Filter.
 *
 * @param <T> the type parameter
 * @author zhangliang
 * @version Id : Filter, v 0.1 2018/5/2 16:09 zhangliang Exp $
 */
public interface Filter<T> {

    /**
     * Do filter boolean.
     *
     * @param source the source
     * @return the boolean
     */
    boolean doFilter(T source);
}
