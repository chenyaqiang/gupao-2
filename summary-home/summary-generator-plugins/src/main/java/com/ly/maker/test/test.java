package com.ly.maker.test;

import com.ly.maker.annotations.SummaryAttr;
import com.ly.maker.annotations.SummaryRoot;

/**
 * @author zhangliang
 * @version Id: test, v 0.1 2018/5/3 11:39 zhangliang Exp $
 */
@SummaryRoot(name = "parent", description = "parent-test")
public class test {
    @SummaryAttr(name = "name", required = true, description = "test.name")
    private String  name;

    @SummaryAttr(name = "age", required = true, description = "test.age")
    private Integer age;

    @SummaryAttr(name = "btest", required = false, description = "complex type, btest")
    private btest   btest;

    //    @SummaryAttr(description = "self usage test", required = true)
    //    private test    selftest;

    /**
     * Getter method for property <tt>btest</tt>.
     *
     * @return property value of btest
     */
    public com.ly.maker.test.btest getBtest() {
        return btest;
    }

    /**
     * Setter method for property <tt>btest</tt>.
     *
     * @param btest value to be assigned to property btest
     */
    public void setBtest(com.ly.maker.test.btest btest) {
        this.btest = btest;
    }

    /**
     * Getter method for property <tt>name</tt>.
     *
     * @return property value of name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter method for property <tt>name</tt>.
     *
     * @param name value to be assigned to property name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter method for property <tt>age</tt>.
     *
     * @return property value of age
     */
    public Integer getAge() {
        return age;
    }

    /**
     * Setter method for property <tt>age</tt>.
     *
     * @param age value to be assigned to property age
     */
    public void setAge(Integer age) {
        this.age = age;
    }
}
