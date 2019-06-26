package cn.stormbirds.stpaylib;

import android.app.Activity;

import com.alipay.sdk.app.EnvUtils;

import java.util.concurrent.TimeUnit;

import cn.stormbirds.stpaylib.bean.OrderRequest;
import cn.stormbirds.stpaylib.handler.PayHandler;
import okhttp3.OkHttpClient;

/**
 * Copyright (c) 小宝 @2019
 *
 * @ Package Name:    cn.stormbirds.stpaylib
 * @ Author：         stormbirds
 * @ Email：          xbaojun@gmail.com
 * @ Created At：     2019/6/25 09:30
 * @ Description：
 */


public enum STPaySDK implements ISTPayService {
    INSTANCE;

    protected static PayHandler mPayHandler;
    private String wxAppId;

    private boolean isTest = false;

    protected static String toPayURL = "https://pay.snhanyue.top/topay";
    protected static OkHttpClient mOkHttpClient=new OkHttpClient.Builder()
            .callTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .build();

    STPaySDK() {
    }

    public void setWxAppId(String appid){
        this.wxAppId = appid;
    }

    public String getWxAppId(){
        return this.wxAppId;
    }

    public boolean isTest() {
        return isTest;
    }

    public void setTest(boolean test) {
        isTest = test;
        if(isTest){
            EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        }
    }

    @Override
    public void pay(Activity activity, OrderRequest order, PayType payType, STPayCallback callback) {
        PaymentTask paymentTask = new PaymentTask(activity,payType,callback);
        paymentTask.execute(order);
    }
}
