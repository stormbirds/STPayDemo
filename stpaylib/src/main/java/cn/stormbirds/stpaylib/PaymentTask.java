package cn.stormbirds.stpaylib;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;

import java.io.IOException;
import java.lang.ref.WeakReference;

import cn.stormbirds.stpaylib.bean.OrderRequest;
import cn.stormbirds.stpaylib.handler.PayHandler;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static cn.stormbirds.stpaylib.STPaySDK.mOkHttpClient;
import static cn.stormbirds.stpaylib.STPaySDK.toPayURL;

/**
 * Copyright (c) 小宝 @2019
 *
 * @ Package Name:    cn.stormbirds.stpaylib
 * @ Author：         stormbirds
 * @ Email：          xbaojun@gmail.com
 * @ Created At：     2019/6/25 10:15
 * @ Description：
 */


public class PaymentTask extends AsyncTask<OrderRequest,Double,String> {
    private WeakReference<Activity>  mActivityReference;
    private PayType mPayType;
    private STPayCallback mCallback;

    public PaymentTask(Activity activity, PayType payType, STPayCallback callback) {
        this.mActivityReference = new WeakReference<>(activity);
        this.mPayType = payType;
        this.mCallback = callback;
    }

    @Override
    protected String doInBackground(OrderRequest... orderRequests) {
        String data = null;
        try {
            data = postJson(toPayURL, orderRequests[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 获取到服务器订单，开始执行支付操作
     * @param preOrder
     */
    @Override
    protected void onPostExecute(String preOrder) {
        if(preOrder==null && this.mCallback!=null){
            PayLog.d("获取预订单失败.");

            this.mCallback.onPayFinished(PayResult.makeResult(4005, 2));
        }else if(this.mCallback == null){
            return;
        }else{
            JSONObject orderInfo = JSON.parseObject(preOrder).getJSONObject("orderInfo");

            PayHandler payHandler = this.mPayType.getPayService(this.mCallback,orderInfo);
            STPaySDK.mPayHandler = payHandler;
            payHandler.pay(this.mActivityReference.get());

        }
    }

    @Override
    protected void onPreExecute() {

    }

    private String postJson(String url, OrderRequest order) throws IOException {
        RequestBody requestBody = new MultipartBody.Builder()
                .addFormDataPart("payId", String.valueOf(order.getPayId()))
                .addFormDataPart("price", String.valueOf(order.getPrice()))
                .addFormDataPart("transactionType", "APP")
                .build();
        Request request = new Request.Builder().post(requestBody).url(url).build();
        Response response = mOkHttpClient.newCall(request).execute();

//        assert response.body() != null;
        return response.code() == 200 ? response.body().string() : null;
    }

}
