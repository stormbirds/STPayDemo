package cn.stormbirds.stpaylib;

/**
 * Copyright (c) 小宝 @2019
 *
 * @Package Name:    cn.stormbirds.stpaylib
 * @Author：         stormbirds
 * @Email：          xbaojun@gmail.com
 * @Created At：     2019/6/21 17:34
 * @Description： 支付结果回调
 */


public interface STPayCallback {
    void onPayFinished(PayResult payResult);
}
