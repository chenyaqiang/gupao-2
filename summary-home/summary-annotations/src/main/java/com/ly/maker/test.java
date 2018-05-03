package com.ly.maker;

/**
 * @author zhangliang
 * @version Id: test, v 0.1 2018/5/3 11:39 zhangliang Exp $
 */
public class test {
    private String  name;
    private Integer age;
    private btest   btest;

    /**
     * Getter method for property <tt>btest</tt>.
     *
     * @return property value of btest
     */
    public com.ly.maker.btest getBtest() {
        return btest;
    }

    /**
     * Setter method for property <tt>btest</tt>.
     *
     * @param btest value to be assigned to property btest
     */
    public void setBtest(com.ly.maker.btest btest) {
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
