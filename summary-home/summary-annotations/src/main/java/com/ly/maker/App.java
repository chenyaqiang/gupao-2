package com.ly.maker;

import sun.security.acl.AclImpl;

import java.lang.reflect.Field;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) throws ClassNotFoundException {
        Class<?> aClass = Class.forName("com.ly.maker.test");
        for (Field field : aClass.getDeclaredFields()) {
            System.out.println(field.getName());
            System.out.println(field);
        }
        System.out.println(aClass.getName());
    }
}
