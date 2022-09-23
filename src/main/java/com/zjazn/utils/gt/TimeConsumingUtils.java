package com.zjazn.utils.gt;

public class TimeConsumingUtils {
    public static<T> Object test(String testName,T t, TestCode testCode) {
        long begin = System.currentTimeMillis();
        Object obj = testCode.accept(t);
        long end = System.currentTimeMillis();
        System.out.println(testName+">> 测试耗时："+(begin-end));
        return obj;
    }
    public static<T> void test(String testName,NotParamTestCode testCode) {
        long begin = System.currentTimeMillis();
        testCode.accept();
        long end = System.currentTimeMillis();
        System.out.println(testName+">> 测试耗时："+(end-begin));
    }
}
