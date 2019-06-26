package cn.stormbirds.stpaylib;

import com.alibaba.fastjson.JSONObject;

import cn.stormbirds.stpaylib.handler.AliPayHandler;
import cn.stormbirds.stpaylib.handler.PayHandler;
import cn.stormbirds.stpaylib.handler.WxPayHandler;

/**
 * Copyright (c) 小宝 @2019
 *
 * @ Package Name:    cn.stormbirds.stpaylib
 * @ Author：         stormbirds
 * @ Email：          xbaojun@gmail.com
 * @ Created At：     2019/6/25 09:38
 * @ Description：
 */


public enum PayType {
    aliPay {
        @Override
        public PayHandler getPayService(STPayCallback callback, JSONObject preOreder) {
            return new AliPayHandler(callback, preOreder);
        }
    }, wxPay {
        @Override
        public PayHandler getPayService(STPayCallback callback, JSONObject preOreder) {
            return new WxPayHandler(callback, preOreder);
        }
    };

    public abstract PayHandler getPayService(STPayCallback callback, JSONObject preOreder);

}
