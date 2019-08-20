package com.yalantis.ucrop.util;

import android.content.Context;

/**
 * @auth fxl on 2019/8/20.
 */
public class SizeUtil {

    public static int dp2px(Context context, float dpVal) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (density * dpVal + 0.5f);
    }
}
