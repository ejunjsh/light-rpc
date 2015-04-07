package com.sky.test;

/**
 * Created by shaojunjie on 2015/4/6.
 */
public class TestStatic {

    {
        System.out.println("non static 1");
    }

    static
    {
        System.out.println("static 1");
    }

    public static void main(String[] args)
    {
        TestStatic testStatic=new TestStatic();
    }
}
