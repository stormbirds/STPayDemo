package cn.stormbirds.stpaylib.bean;

/**
 * Copyright (c) 小宝 @2019
 *
 * @ Package Name:    cn.stormbirds.stpaylib.bean
 * @ Author：         stormbirds
 * @ Email：          xbaojun@gmail.com
 * @ Created At：     2019/6/25 11:49
 * @ Description：
 */


public class WxOrderRequest extends OrderRequest {
    private String transactionType;

    public WxOrderRequest(String transactionType, OrderRequest order) {
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
