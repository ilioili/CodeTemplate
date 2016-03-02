package com.taihe.template.base.util;

import android.os.Environment;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 封装系统的Log，
 */
public class LogUtil {
    private static boolean isDebug = true;
    private static String prefix = "";
    private static boolean isInvoked;
    private static SimpleDateFormat fileNameFormater;
    private static SimpleDateFormat sdf;

    static {
        fileNameFormater = new SimpleDateFormat("yyyy-MM-dd-hh");
        sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    }

    /**
     * @param debugable BuildConfig.DEBUG is recommended
     * @param tagPrefix appName is recommended
     */
    public static void config(boolean debugable, String tagPrefix) {
        if (isInvoked) {
            throw new RuntimeException("This method can only be invoked once");
        } else {
            isInvoked = true;
        }
        isDebug = debugable;
        prefix = tagPrefix;
    }

    public static void v(String str) {
        v(prefix, str);
    }

    public static void v(String tag, String str) {
        if (isDebug)
            Log.v(prefix + tag, str);
    }

    //---------------------------------------------------------------------------------------------------------------------

    public static void v(Throwable e) {
        v(prefix, e);
    }

    public static void v(String tag, Throwable e) {
        if (isDebug)
            Log.v(prefix + tag, Log.getStackTraceString(e));
    }

    public static void d(String str) {
        d(prefix, str);
    }

    public static void d(String tag, String str) {
        if (isDebug)
            Log.d(prefix + tag, str);
    }

    //---------------------------------------------------------------------------------------------------------------------

    public static void d(Throwable e) {
        d(prefix, e);
    }

    public static void d(String tag, Throwable e) {
        if (isDebug)
            Log.d(prefix + tag, Log.getStackTraceString(e));
    }

    public static void i(String str) {
        i(prefix, str);
    }

    public static void i(String tag, String str) {
        if (isDebug)
            Log.i(prefix + tag, str);
    }

    //---------------------------------------------------------------------------------------------------------------------

    public static void i(Throwable e) {
        i(prefix, e);
    }

    public static void i(String tag, Throwable e) {
        if (isDebug)
            Log.i(prefix + tag, Log.getStackTraceString(e));
    }

    public static void w(String str) {
        w(prefix, str);
    }

    public static void w(String tag, String str) {
        if (isDebug)
            Log.w(prefix + tag, str);
    }

    //---------------------------------------------------------------------------------------------------------------------

    public static void w(Throwable e) {
        w(prefix, e);
    }

    public static void w(String tag, Throwable e) {
        e.printStackTrace();
        if (isDebug)
            Log.w(prefix + tag, Log.getStackTraceString(e));
    }

    public static void e(String str) {
        e(prefix, str);
    }

    public static void e(String tag, String str) {
        if (isDebug)
            Log.e(prefix + tag, str);
    }

    //---------------------------------------------------------------------------------------------------------------------

    public static void e(Throwable e) {
        e(prefix, e);
    }

    public static void e(String tag, Throwable e) {
        e.printStackTrace();
        if (isDebug)
            Log.e(prefix + tag, Log.getStackTraceString(e));
    }

    /**
     * 输出日志
     */
    public static void f(String str) {
        if (isDebug) {
            try {
                FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory().getPath() + "/.log/" + fileNameFormater.format(new Date())+".txt");
                String logContent = sdf.format(new Date()) + "\n" + str + "\n\n";
                fos.write(logContent.getBytes());
                fos.flush();
                fos.close();
            } catch (IOException e) {
                LogUtil.e(e);
            }
        }
    }

    public static void f(Throwable e) {
        if (isDebug)
            f(Log.getStackTraceString(e));
    }


}
