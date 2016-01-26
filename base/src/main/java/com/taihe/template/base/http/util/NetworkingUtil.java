package com.taihe.template.base.http.util;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * 网络状态工具类
 *
 * @author Leo.Chang
 */
public class NetworkingUtil {
    private static Context applicationContext;

    /**
     * 检查网络状态
     *
     * @return
     */
    public static boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static void init(Context context) {
        applicationContext = context;
    }


    /**
     * 检查网络是否可用
     *
     * @return 0：使用的GPRS网络；-1：没有可用网络；1：使用的WIFI
     */
    public static int checkNetworkingState() {
        ConnectivityManager connectivity = (ConnectivityManager) applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            // 没有网络连接
            return -1;
        } else {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if ((null != info) && info.isAvailable()) {
                if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                    // 如果使用的WIFI网络
                    return 1;
                } else {
                    // 使用非WIFI网络
                    return 0;
                }
            } else {
                // 没有网络连接
                return -1;
            }
        }
    }

    /**
     * 关闭WIFI
     *
     * @param context 上下文对象
     */
    public static void closeWifi(Context context) {
        Log.i("TAG", "close wifi");
        WifiManager wifi = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        // wifi.disconnect();
        wifi.setWifiEnabled(false);
    }

    /**
     * 判断是否CTWAP连接 ，如果不是，则修改APN为CTWAP
     *
     * @param context 上下文对象
     * @return 0：使用CTWAP；-1：没有可用GPRS网络；-2：使用CTNET
     */
    public static int isCTWAP(Context context) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(
                Uri.parse("content://telephony/carriers/preferapn"),
                new String[]{"user", "proxy"}, null, null, null);
        if (cursor.moveToFirst()) {
            String user = cursor.getString(cursor.getColumnIndex("user"));
            String proxy = cursor.getString(cursor.getColumnIndex("proxy"));
            Log.i("TAG", "user is : " + user);
            Log.i("TAG", "proxy is : " + proxy);
            if ((null == user) || !user.toLowerCase().contains("mycdma")) {
                // 如果用户名为空，或者不属于C网用户
                if (null != cursor) {
                    cursor.close();
                    cursor = null;
                }
                return -1;
            }
            if (user.toLowerCase().contains("wap")
                    && "10.0.0.200".equals(proxy)) {
                // 如果当前APN为CTWAP
                if (null != cursor) {
                    cursor.close();
                    cursor = null;
                }
                return 0;
            }
        }
        if (null != cursor) {
            cursor.close();
            cursor = null;
        }
        return -2;
    }

    /**
     * 获取CTWAP的APN对应的ID
     *
     * @param context 上下文对象
     * @return CTWAP APN对应的ID
     */
    private static String getCTWAPAPN(Context context) {
        String apnId = null;
        Uri uri = Uri.parse("content://telephony/carriers");
        Cursor cr = context.getContentResolver().query(uri, null, null, null,
                null);
        while (cr != null && cr.moveToNext()) {
            String user = cr.getString(cr.getColumnIndex("user"));
            String proxy = cr.getString(cr.getColumnIndex("proxy"));
            if ((null != user) && (null != proxy) && user.contains("ctwap")
                    && "10.0.0.200".equals(proxy)) {
                apnId = cr.getString(cr.getColumnIndex("_id"));
                break;
            }
        }
        return apnId;
    }

    /**
     * 修改当前APN为CTWAP
     *
     * @param context 上下文对象
     */
    public static void updateAPN(Context context) {
        Uri uri = Uri.parse("content://telephony/carriers/preferapn");
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put("apn_id", getCTWAPAPN(context));
        resolver.update(uri, values, null, null);
    }
}
