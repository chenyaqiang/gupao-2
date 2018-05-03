package com.ly.maker.test;

import com.ly.maker.annotations.DataType;
import com.ly.maker.annotations.SummaryRoot;

/**
 * @author zhangliang
 * @version Id: Remark, v 0.1 2018/5/3 18:43 zhangliang Exp $
 */
@SummaryRoot(name = "remark")
public class Remark {
    @DataType
    private String remarkTest;
}
