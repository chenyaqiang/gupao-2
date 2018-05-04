package com.ly.maker.writer;

import java.io.*;
import java.util.List;

/**
 * @author zhangliang
 * @version Id: FileWriterImpl, v 0.1 2018/5/4 19:53 zhangliang Exp $
 */
public class FileWriterImpl implements Writer<List<String>> {
    /**
     * Save.
     *
     * @param strings the data
     */
    @Override
    public void save(List<String> strings) {
        try {
            File file = new File("/Users/zhangliang/Desktop/test.txt");
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
            for (String string : strings) {
                bw.write(string);
                bw.newLine();
            }
            bw.newLine();
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
