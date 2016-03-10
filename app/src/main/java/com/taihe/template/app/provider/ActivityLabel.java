package com.taihe.template.app.provider;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.ilioili.appstart.BuildConfig;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/3/10.
 */
public class ActivityLabel {
    private static HashMap<String, String> map;
    public static String getLabel(Context context, Class activityClass){
        return  getLabel(context, activityClass.getName());
    }

    public static String getLabel(Context context, String classFulName){
        if(null==map){
            try {
                map = new HashMap<>();
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(BuildConfig.APPLICATION_ID, PackageManager.GET_ACTIVITIES);
                for(ActivityInfo activityInfo : packageInfo.activities){
                    map.put(activityInfo.name, activityInfo.loadLabel(context.getPackageManager())+"");
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return map.get(classFulName);
    }
}
