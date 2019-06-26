package cn.stormbirds.stpaylib.bean;

import cn.stormbirds.stpaylib.bean.OrderRequest;


/**
 * Copyright (c) 小宝 @2019
 *
 * @ Package Name:    cn.stormbirds.stpaylib
 * @ Author：         stormbirds
 * @ Email：          xbaojun@gmail.com
 * @ Created At：     2019/6/25 11:54
 * @ Description：
 */


public class AliOrderRequest extends OrderRequest {
    private String transactionType;

    public AliOrderRequest(String transactionType, OrderRequest order) {
        super(order);
        this.transactionType = transactionType;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
}
