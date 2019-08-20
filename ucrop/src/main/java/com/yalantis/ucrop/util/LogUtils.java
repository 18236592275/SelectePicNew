package com.yalantis.ucrop.util;

import android.util.Log;
import android.widget.Toast;

/**
 * @auth fxl on 2019/8/20.
 */
public class LogUtils {
    private static final String TAG = "UCROP::";

    public static void i(String msg) {
        Log.i(TAG, msg);
    }
    public static void i(String tag,String msg) {
        Log.i(tag, msg);
    }
    public static void e(String msg) {
        Log.e(TAG, msg);
    }
}
