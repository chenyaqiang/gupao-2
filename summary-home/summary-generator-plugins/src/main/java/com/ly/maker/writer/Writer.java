package com.ly.maker.writer;

/**
 * The interface Writer.
 *
 * @param <TData> the type parameter
 * @author zhangliang
 * @version Id : Writer, v 0.1 2018/5/3 16:48 zhangliang Exp $
 */
public interface Writer<TData> {
    /**
     * Save.
     *
     * @param data the data
     */
    void save(TData data);
}
