package cn.stormbirds.stpaylib;

import android.util.Log;

/**
  * <p>Copyright (c) 小宝 @ 2019</p>
  *
  * <p>@Package Name:    cn.stormbirds.stpaylib</p>
  * <p>@Author：         stormbirds</p>
  * <p>@Email：          xbaojun@gmail.com</p>
  * <p>@Created At：     2019/6/21 18:09</p>
  * <p>@Description：    </p>
  *
  */
public class PayLog {
    private static final String TAG = "STPay";

    public static void i(String msg) {
        Log.i(TAG, msg);
    }

    public static void d(String msg) {
        Log.d(TAG, msg);
    }

    public static void e(String msg) {
        Log.e(TAG, msg);
    }

    public static void e(Exception e) {
        e.printStackTrace();
        Log.e(TAG, e.getMessage());
    }
}