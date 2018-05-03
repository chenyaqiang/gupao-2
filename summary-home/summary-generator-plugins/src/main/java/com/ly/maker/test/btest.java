package com.ly.maker.test;

import com.ly.maker.annotations.DataType;
import com.ly.maker.annotations.SummaryRoot;

/**
 * @author zhangliang
 * @version Id: btest, v 0.1 2018/5/3 12:13 zhangliang Exp $
 */
@SummaryRoot(name = "btest-root")
public class btest {
    @DataType(name = "bname", description = "btest.bname", required = false)
    private String bname;

    /**
     * Getter method for property <tt>bname</tt>.
     *
     * @return property value of bname
     */
    public String getBname() {
        return bname;
    }

    /**
     * Setter method for property <tt>bname</tt>.
     *
     * @param bname value to be assigned to property bname
     */
    public void setBname(String bname) {
        this.bname = bname;
    }
}
