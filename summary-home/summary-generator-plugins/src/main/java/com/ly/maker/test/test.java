package com.ly.maker.test;

import com.ly.maker.annotations.DataType;
import com.ly.maker.annotations.SummaryRoot;

import java.util.List;

/**
 * @author zhangliang
 * @version Id: test, v 0.1 2018/5/3 11:39 zhangliang Exp $
 */
@SummaryRoot(name = "parent", description = "parent-test")
public class test {
    @DataType(name = "name", required = true, description = "test.name")
    private String       name;

    @DataType(name = "age", required = true, description = "test.age")
    private Integer      age;

    @DataType(name = "btest", required = false, description = "complex type, btest", hasChild = true)
    private btest        btest;

    @DataType(description = "list collection", hasChild = true, itemType = "com.ly.maker.test.Remark")
    private List<Remark> lstCollection;

    //    @DataType(description = "self usage test", required = true)
    //    private test    selftest;
}
