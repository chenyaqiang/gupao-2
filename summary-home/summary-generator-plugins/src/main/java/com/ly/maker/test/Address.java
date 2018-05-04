package com.ly.maker.test;

import com.ly.maker.annotations.DataType;
import com.ly.maker.annotations.SummaryRoot;

/**
 * @author zhangliang
 * @version Id: Address, v 0.1 2018/5/3 12:13 zhangliang Exp $
 */
@SummaryRoot(description = "Address desc", name = "Address")
public class Address {

    private String street;

    /**
     * Getter method for property <tt>street</tt>.
     *
     * @return property value of street
     */
    public String getStreet() {
        return street;
    }

    /**
     * Setter method for property <tt>street</tt>.
     *
     * @param street value to be assigned to property street
     */
    public void setStreet(String street) {
        this.street = street;
    }
}
