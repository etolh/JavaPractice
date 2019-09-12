package com.liah.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 中介类：不实现代理类，而是在运行时动态生产代理类对象，并调用代理类方法
 */
public class DynamicProxy implements InvocationHandler {

    private Object obj; // 委托类对象

    DynamicProxy(Object obj) {
        this.obj = obj;
    }

    /**
     * 当调用代理类对象的方法，会调用该方法
     * @param proxy 代理类对象
     * @param method 方法
     * @param args 方法参数
     * @return 方法结果
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        System.out.println("before");
        Object res = method.invoke(obj, args);  // 调用委托类的方法
        System.out.println("after");

        return res;
    }
}
