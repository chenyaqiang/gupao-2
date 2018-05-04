package com.ly.maker.test;

import java.util.List;

import com.ly.maker.annotations.DataType;
import com.ly.maker.annotations.ListType;
import com.ly.maker.annotations.SummaryRoot;

/**
 * @author zhangliang
 * @version Id: Student, v 0.1 2018/5/3 11:39 zhangliang Exp $
 */
@SummaryRoot(name = "Student", description = "Student desc")
public class Student {
    private String       name;

    private Integer      age;

    private Address      btest;

    private List<Remark> lstCollection;
}
