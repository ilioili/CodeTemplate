package com.taihe.template.base.util;

import android.view.View;

/**
 * Created by Administrator on 2016/1/20.
 */
public class ViewUtil {
    public static <T extends View> T f(View host, int id) {
        return (T) host.findViewById(id);
    }

    public static void setGone(View... views) {
        for (View v : views) {
            v.setVisibility(View.GONE);
        }
    }

    public static void setVisiable(View... views) {
        for (View v : views) {
            v.setVisibility(View.VISIBLE);
        }
    }

    public static void setInvisiable(View... views) {
        for (View v : views) {
            v.setVisibility(View.INVISIBLE);
        }
    }

}
