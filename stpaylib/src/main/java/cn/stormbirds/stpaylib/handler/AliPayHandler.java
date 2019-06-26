package cn.stormbirds.stpaylib.handler;

import android.app.Activity;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;

import java.util.Map;

import cn.stormbirds.stpaylib.PayLog;
import cn.stormbirds.stpaylib.PayResult;
import cn.stormbirds.stpaylib.STPayCallback;

import static cn.stormbirds.stpaylib.utils.ConvertUtils.getMapToParameters;

/**
 * Copyright (c) 小宝 @2019
 *
 * @ Package Name:    cn.stormbirds.stpaylib.handler
 * @ Author：         stormbirds
 * @ Email：          xbaojun@gmail.com
 * @ Created At：     2019/6/25 11:32
 * @ Description：
 */


public class AliPayHandler extends PayHandler {


    public AliPayHandler(STPayCallback callback, JSONObject preOreder) {
        super(callback,preOreder);
    }

    @Override
    public void pay(final Activity activity) {
        (new Thread(new Runnable() {
            public void run() {
                PayTask alipay = new PayTask(activity);
                Map<String, String> aliResult = alipay.payV2(getMapToParameters(AliPayHandler.this.mPreOrder) , true);
                Log.d("magical", "aliResult=" + aliResult);
                AliPayHandler.this.onPayFinished(AliPayHandler.this.check(aliResult));
            }
        })).start();
    }

    /**
     * 9000	订单支付成功
     * 8000	正在处理中，支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
     * 4000	订单支付失败
     * 5000	重复请求
     * 6001	用户中途取消
     * 6002	网络连接出错
     * 6004	支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
     * 其它	其它支付错误
     * @param aliResult
     * @return
     */
    private PayResult check(Map<String,String> aliResult) {
        PayResult payResult;
        if (aliResult.containsKey("resultStatus") && aliResult.containsKey("memo") && aliResult.containsKey("result")) {
            int resultStatus = Integer.parseInt(aliResult.get("resultStatus"));
            String desc = aliResult.get("memo");
//            String result = aliResult.get("result");
            switch (resultStatus) {
                case 9000:
                    PayLog.d("Pay is CODE_SUCCESS.");
                    payResult = PayResult.makeResult(2000, 2, 1, desc);
                    break;
                case 4000:
                    PayLog.d("Pay is CODE_ERROR_FAIL.");
                    payResult = PayResult.makeResult(4005, 2, 1, desc);
                    break;
                case 6001:
                    PayLog.d("Pay is CODE_ERROR_CANCEL.");
                    payResult = PayResult.makeResult(4004, 2, 1, desc);
                    break;
                case 8000:
                    PayLog.d("Pay is CODE_ERROR_ALI_DEAL.");
                    payResult = PayResult.makeResult(4201, 2, 1, desc);
                    break;
                default:
                    PayLog.d("Pay is CODE_ERROR_ALI_CONNECT.");
                    payResult = PayResult.makeResult(4202, 2, 1, desc);
                    break;
            }
        } else {
            PayLog.d("Pay is CODE_ERROR_FAIL.");
            payResult = PayResult.makeResult(4005, 2);
        }
        return payResult;
    }
}
