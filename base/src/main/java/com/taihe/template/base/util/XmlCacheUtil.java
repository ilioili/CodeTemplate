package com.taihe.template.base.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.WeakHashMap;

/**
 * Created by Administrator on 2016/1/11.
 */
public class XmlCacheUtil {
    private static WeakHashMap<Object, XmlCacheUtil> cacheMap = new WeakHashMap<>();
    private SharedPreferences sharedPreferences;

    public static XmlCacheUtil getInstance(Context context) {
        XmlCacheUtil xmlCacheUtil = cacheMap.get(context);
        if (null == xmlCacheUtil) {
            xmlCacheUtil = new XmlCacheUtil();
            xmlCacheUtil.sharedPreferences = context.getSharedPreferences(context.getClass().getName(), Context.MODE_PRIVATE);
            cacheMap.put(context, xmlCacheUtil);
        }
        return xmlCacheUtil;
    }

    public synchronized void save(String key, Object obj) {
        if (null != obj) {
            String jsonStr = obj instanceof String ? obj.toString() : new Gson().toJson(obj);
            sharedPreferences.edit().putString(key + obj.getClass().getName(), jsonStr).commit();
        }
    }

    public synchronized void saveAsync(final String key, final Object obj) {
        if (null != obj) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String jsonStr = new Gson().toJson(obj);
                    sharedPreferences.edit().putString(key + obj.getClass().getName(), jsonStr).commit();
                }
            }).start();
        }
    }

    public synchronized <T> T get(String key, Class<T> clazz) {
        String jsonStr = sharedPreferences.getString(key + clazz.getName(), null);
        if (null == jsonStr) {
            return null;
        } else {
            return new Gson().fromJson(jsonStr, clazz);
        }
    }

    public synchronized <T> void getAsync(final String key, final Callback<T> callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Type type = callback.getClass().getGenericInterfaces()[0];
                type = ((ParameterizedType) type).getActualTypeArguments()[0];
                final String jsonStr = sharedPreferences.getString(key + type.getClass().getName(), null);
                final Type finalType = type;
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (null == jsonStr) {
                            callback.onCallback(null);
                        } else {
                            callback.onCallback((T) new Gson().fromJson(jsonStr, finalType));
                        }
                    }
                });

            }
        }).start();

    }

    public synchronized <T> void save(T t) {
        save("", t);
    }

    public synchronized <T> void saveAsync(T t) {
        saveAsync("", t);
    }

    public synchronized <T> T get(Class<T> clazz) {
        return get("", clazz);
    }

    public synchronized <T> void getAsync(final Callback<T> callback) {
        getAsync("", callback);
    }

    public interface Callback<T> {
        void onCallback(T t);
    }

}
