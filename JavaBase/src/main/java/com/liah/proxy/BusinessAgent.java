package com.liah.proxy;

import com.liah.proxy.model.Sell;

/**
 * 代理类：静态
 * 缺点运行前需要编好代理类
 */
public class BusinessAgent implements Sell {

    private Sell seller;    // 代理类持有委托类的对象引用

    BusinessAgent(Sell seller){
        this.seller = seller;
    }

    @Override
    public void sell() {
        seller.sell();
    }

    @Override
    public void ad() {
        seller.ad();
    }
}
