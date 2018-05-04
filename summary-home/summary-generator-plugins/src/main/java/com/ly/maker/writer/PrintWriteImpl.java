package com.ly.maker.writer;

import java.util.List;

/**
 * @author zhangliang
 * @version Id: PrintWriteImpl, v 0.1 2018/5/4 19:50 zhangliang Exp $
 */
public class PrintWriteImpl implements Writer<List<String>> {
    /**
     * Save.
     *
     * @param strings the data
     */
    @Override
    public void save(List<String> strings) {
        System.out.println("----from writer-----");
        System.out.println(String.join("\r\n", strings));
    }
}
