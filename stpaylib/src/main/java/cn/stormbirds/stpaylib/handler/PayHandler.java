package cn.stormbirds.stpaylib.handler;

import android.app.Activity;

import com.alibaba.fastjson.JSONObject;

import cn.stormbirds.stpaylib.PayLog;
import cn.stormbirds.stpaylib.PayResult;
import cn.stormbirds.stpaylib.STPayCallback;
import cn.stormbirds.stpaylib.bean.OrderRequest;

/**
 * Copyright (c) 小宝 @2019
 *
 * @Package Name:    cn.stormbirds.stpaylib
 * @Author：         stormbirds
 * @Email：          xbaojun@gmail.com
 * @Created At：     2019/6/21 18:04
 * @Description：
 */


public abstract class PayHandler {
    protected STPayCallback mCallback;
    protected JSONObject mPreOrder;

    public abstract void pay(Activity activity);

    PayHandler(STPayCallback callback, JSONObject preOrder) {
        this.mCallback = callback;
        this.mPreOrder = preOrder;
    }

    public void onPayFinished(PayResult result) {
        if (this.mCallback != null) {
            this.mCallback.onPayFinished(result);
            this.mCallback = null;
            return;
        }
        PayLog.d("Callback object is null.");
    }
}
