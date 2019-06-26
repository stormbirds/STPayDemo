package cn.stormbirds.stpaylib;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import cn.stormbirds.stpaylib.handler.WxPayHandler;

/**
 * Copyright (c) 小宝 @2019
 *
 * @ Package Name:    cn.stormbirds.stpaylib
 * @ Author：         stormbirds
 * @ Email：          xbaojun@gmail.com
 * @ Created At：     2019/6/25 16:11
 * @ Description：
 */


public class PaymentActivity extends Activity {
    private boolean mClose = false;

    public PaymentActivity() {
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        if (STPaySDK.mPayHandler != null && STPaySDK.mPayHandler instanceof WxPayHandler) {
            ((WxPayHandler)STPaySDK.mPayHandler).onCreate(this);
        }

    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (STPaySDK.mPayHandler != null && STPaySDK.mPayHandler instanceof WxPayHandler) {
            ((WxPayHandler)STPaySDK.mPayHandler).onNewIntent(this, intent);
        }

    }

    protected void onResume() {
        super.onResume();
        if (this.mClose) {
            this.finish();
        }

        this.mClose = true;
    }

    protected void onPause() {
        super.onPause();
    }

    public void onDestroy() {
        if (STPaySDK.mPayHandler != null && STPaySDK.mPayHandler instanceof WxPayHandler) {
            ((WxPayHandler)STPaySDK.mPayHandler).onDestroy();
            STPaySDK.mPayHandler = null;
        }

        super.onDestroy();
    }

    public void onBackPressed() {
        if (STPaySDK.mPayHandler != null) {
            STPaySDK.mPayHandler.onPayFinished(PayResult.makeResult(4004, 1));
        }

        super.onBackPressed();
    }
}
