package cn.stormbirds.stpaylib;

/**
 * Copyright (c) 小宝 @2019
 *
 * @Package Name:    cn.stormbirds.stpaylib
 * @Author：         stormbirds
 * @Email：          xbaojun@gmail.com
 * @Created At：     2019/6/21 17:34
 * @Description：
 */


public class PayResult {
    /**
     * 支付渠道
     */
    private int mChannel;
    /**
     * 支付方式代码
     */
    private int mTransactionCode;
    /**
     * 支付状态代码
     */
    private int mCode;
    /**
     * 支付结果说明
     */
    private String mDesc;

    public static PayResult makeResult(int code, int channel) {
        return new PayResult(code, channel, -1, null);
    }

    public static PayResult makeResult(int code, int channel, int transactionCode) {
        return new PayResult(code, channel, transactionCode, null);
    }

    public static PayResult makeResult(int code, int channel, int transactionCode, String desc) {
        return new PayResult(code, channel, transactionCode, desc);
    }

    public PayResult(int code, int channel, int transactionCode, String desc) {
        this.mChannel = channel;
        this.mCode = code;
        this.mTransactionCode = transactionCode;
        this.mDesc = desc;
    }

    public int getCode() {
        return this.mCode;
    }

    public int getChannel() {
        return this.mChannel;
    }

    public int getChannelCode() {
        return this.mTransactionCode;
    }

    public String getDesc() {
        return this.mDesc;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("code=").append(this.mCode).append(", channel=").append(this.mChannel).append(", transactionCode=").append(this.mTransactionCode).append(", desc=").append(this.mDesc);
        return s.toString();
    }
}
