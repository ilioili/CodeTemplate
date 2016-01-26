package com.taihe.template.base.util;

import android.content.Context;

/**
 * Created by ilioili on 2015/3/17.
 */
public class SizeUtil {
    public static int dpToPx(int dp, Context context){
        return (int) (dp*context.getResources().getDisplayMetrics().density+0.5f);
    }

    public static float pxToDp(int px, Context context){
        return px/context.getResources().getDisplayMetrics().density;
    }
}
