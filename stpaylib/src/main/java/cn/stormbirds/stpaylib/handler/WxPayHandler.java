package cn.stormbirds.stpaylib.handler;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.lang.ref.WeakReference;

import cn.stormbirds.stpaylib.PayLog;
import cn.stormbirds.stpaylib.PayResult;
import cn.stormbirds.stpaylib.PaymentActivity;
import cn.stormbirds.stpaylib.STPayCallback;

/**
 * Copyright (c) 小宝 @2019
 *
 * @ Package Name:    cn.stormbirds.stpaylib.handler
 * @ Author：         stormbirds
 * @ Email：          xbaojun@gmail.com
 * @ Created At：     2019/6/25 11:32
 * @ Description：
 */


public class WxPayHandler extends PayHandler implements IWXAPIEventHandler {

    private IWXAPI wxapi = null;
    private WeakReference<PaymentActivity> mRef;

    public WxPayHandler(STPayCallback callback, JSONObject preOreder) {
        super(callback, preOreder);
    }

    @Override
    public void pay(final Activity activity) {
        String packageName = activity.getPackageName();
        Intent intent = new Intent();
        ComponentName componentName = new ComponentName(packageName, packageName + ".wxapi.WXPayEntryActivity");
        intent.setComponent(componentName);
        activity.startActivity(intent);
    }

    public void onCreate(PaymentActivity activity) {
        PayLog.d("onCreate() call.");
        String wxAppId = readAppId(WxPayHandler.this.mPreOrder);
        this.mRef = new WeakReference(activity);
        if (wxAppId != null && wxAppId.length() > 0) {
            IWXAPI wxapi = WXAPIFactory.createWXAPI(activity, (String)null);
            this.wxapi = wxapi;
            if(wxapi.registerApp(wxAppId)){
                wxapi.handleIntent(activity.getIntent(), this);
                if (!wxapi.isWXAppInstalled()) {
                    PayLog.d("Wechat not install.");
                    this.onPayFinished(PayResult.makeResult(4101, 1));
                    this.finish();
                } else if (wxapi.getWXAppSupportAPI() < Build.PAY_SUPPORTED_SDK_INT) {
                    PayLog.d("Wechat unsupport pay.");
                    this.onPayFinished(PayResult.makeResult(4102, 1));
                    this.finish();
                } else {
                    try {
                        this.realPay(this.mPreOrder, wxAppId);
                    } catch (JSONException var5) {
                        PayLog.d("Json eroor.");
                        this.onPayFinished(PayResult.makeResult(4001, 1));
                        this.finish();
                    }
                }
            }else{
                PayLog.d("Wechat register appid fail.");
                this.onPayFinished(PayResult.makeResult(4002, 1));
                this.finish();
            }


        } else {
            PayLog.d("Json eroor.");
            this.onPayFinished(PayResult.makeResult(4002, 1));
            this.finish();
        }

    }

    public void onNewIntent(PaymentActivity activity, Intent intent) {
        PayLog.d("WXPayEntryActivity() call.");
        IWXAPI wxapi = this.wxapi;
        this.mRef = new WeakReference(activity);
        if (wxapi != null) {
            activity.setIntent(intent);
            wxapi.handleIntent(activity.getIntent(), this);
        }

    }

    public void onDestroy() {
        PayLog.d("onDestroy() call.");
        IWXAPI wxapi = this.wxapi;
        if (wxapi != null) {
            wxapi.detach();
            this.wxapi = null;
        }

    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        PayLog.d("IWXAPIEventHandler onResp() call.");
        if (baseResp.getType() == 5) {
            switch(baseResp.errCode) {
                case -2:
                    PayLog.d("Pay is ERR_USER_CANCEL.");
                    this.onPayFinished(PayResult.makeResult(4004, 1));
                    break;
                case -1:
                    PayLog.d("Pay is ERR_COMM.");
                    this.onPayFinished(PayResult.makeResult(4005, 1));
                    break;
                case 0:
                    PayLog.d("Pay is ERR_OK.");
                    this.onPayFinished(PayResult.makeResult(2000, 1));
                    break;
                default:
                    PayLog.d("Pay is CODE_ERROR_FAIL.");
                    this.onPayFinished(PayResult.makeResult(4005, 1));
            }

            this.finish();
        }
    }

    private void finish() {
        if (this.mRef.get() != null) {
            this.mRef.get().finish();
        }

    }

    private void realPay(JSONObject jsonObject, String wxAppId) throws JSONException {
        if (this.wxapi != null) {
            PayReq req = new PayReq();
            req.appId = wxAppId;
            req.partnerId = jsonObject.getString("partnerid");
            req.prepayId = jsonObject.getString("prepayid");
            req.nonceStr = jsonObject.getString("noncestr");
            if (jsonObject.get("timestamp") instanceof String) {
                req.timeStamp = jsonObject.getString("timestamp");
            } else {
                req.timeStamp = jsonObject.getIntValue("timestamp") + "";
            }

            req.packageValue = jsonObject.getString("package");
            req.sign = jsonObject.getString("sign");
            req.extData         = jsonObject.getString("outTradeNo");
            PayLog.d(jsonObject.toJSONString());
            if (!this.wxapi.sendReq(req)) {
                PayLog.d("Pay is CODE_ERROR_WX_UNKNOW.");
                this.onPayFinished(PayResult.makeResult(4103, 1));
                this.finish();
            }

        }
    }

    private String readAppId(JSONObject preOrder){
        return preOrder.getString("appid");
    }
}
