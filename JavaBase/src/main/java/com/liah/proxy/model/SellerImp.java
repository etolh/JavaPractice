package com.liah.proxy.model;

/**
 * 业务实现类：生产厂家
 */
public class SellerImp implements Sell {
    @Override
    public void sell() {
        System.out.println("sell phone");
    }

    @Override
    public void ad() {
        System.out.println("ad phone");
    }
}
