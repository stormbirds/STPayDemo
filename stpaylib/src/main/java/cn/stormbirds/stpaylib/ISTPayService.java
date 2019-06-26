package cn.stormbirds.stpaylib;

import android.app.Activity;

import cn.stormbirds.stpaylib.bean.OrderRequest;
import cn.stormbirds.stpaylib.handler.PayHandler;

/**
 * Copyright (c) 小宝 @2019
 *
 * @ Package Name:    cn.stormbirds.stpaylib
 * @ Author：         stormbirds
 * @ Email：          xbaojun@gmail.com
 * @ Created At：     2019/6/25 09:31
 * @ Description：
 */


public interface ISTPayService {
    void pay(Activity activity, OrderRequest order, PayType payType, STPayCallback callback);
}
