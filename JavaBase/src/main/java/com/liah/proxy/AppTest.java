package com.liah.proxy;

import com.liah.proxy.model.Sell;
import com.liah.proxy.model.SellerDao;
import com.liah.proxy.model.SellerImp;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.Proxy;

/**
 * 动态代理测试
 */
public class AppTest {

    public static void main(String[] args) {

        System.out.println("----------Static Proxy---------------");
        Sell seller = new SellerImp(); // 委托类对象
        Sell proxy = new BusinessAgent(seller); // 代理类对象
        // 客户调用代理类方法
        proxy.sell();
        proxy.ad();

        System.out.println("----------Dynamic Proxy---------------");
        DynamicProxy dynamicProxy = new DynamicProxy(seller);   // 中介类
        // 动态生产代理类对象
        Sell proxy2 = (Sell)Proxy.newProxyInstance(seller.getClass().getClassLoader(), seller.getClass().getInterfaces(), dynamicProxy);
        // 调用代理类对象，实际是调用中介类的invoke方法
        // 流程： client -> 代理类对象 -> 中介类 -> 委托类
        proxy2.sell();
        proxy2.ad();

        System.out.println("----------Dynamic Proxy---------------");
        SellerInterceptor sellerInterceptor = new SellerInterceptor();
        Enhancer enhancer = new Enhancer();
        // enhancer设置要代理的委托对象
        enhancer.setSuperclass(SellerDao.class);
        // enhancer设置实际执行代理逻辑的对象
        enhancer.setCallbacks(new Callback[]{sellerInterceptor});
        // 生产动态代理对象
        SellerDao proxy3 = (SellerDao) enhancer.create();
        proxy3.sell();  // 调用sell会调用interceptor的intercept方法
        proxy3.ad();
    }
}