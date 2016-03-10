package com.taihe.template.base.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class StatusBarUtil {
    private final static String TAG = "StatusBar";


    /**
     * 设置状态栏图标为深色
     *
     * @param window 需要设置的窗口

     * @return boolean 成功执行返回true
     */
    public static boolean setMeizuStatusBarDarkIcon(Window window) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                value |= bit;
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "setMeizuStatusBarDarkIcon: failed");
            }
        }
        return result;
    }

    public static void setXiaomiStatusBarDarkIcon(Window window) {
        try {
            int tranceFlag = 0;
            int darkModeFlag = 0;
            Class clazz = window.getClass();
            Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_TRANSPARENT");
            tranceFlag = field.getInt(layoutParams);
            field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(window, tranceFlag | darkModeFlag, tranceFlag | darkModeFlag);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "setXiaomiStatusBarDarkIcon: failed");
        }
    }

    /**
     * 设置沉浸式窗口，设置成功后，状态栏则透明显示
     *
     * @param window    需要设置的窗口

     * @return boolean 成功执行返回true
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean setImmersedWindow(Window window) {
        boolean result = false;
        if (window != null) {
            if((Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)){
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                result = true;
            }
        }
        return result;
    }

    /**
     * 获取状态栏高度
     *
     * @param context 上下文
     * @return int 状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        try {
            int result = 0;
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = context.getResources().getDimensionPixelSize(resourceId);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 75;
    }

}
