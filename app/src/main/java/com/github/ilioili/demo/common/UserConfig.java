package com.github.ilioili.demo.common;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ilioili on 2015/4/24.
 */
public class UserConfig {
    public static final String SP_TOKEN = "SP_TOKEN";
    private static Context context;
    private static SharedPreferences sp;
    private static String requestToken;

    public static String getRequestToken() {
        if(null==requestToken){
            requestToken = sp.getString(SP_TOKEN, null);
        }
        return requestToken;
    }

    public static void init(Context c) {
        if (context == null) {
            context = c.getApplicationContext();
            sp = context.getSharedPreferences("UserConfig", Context.MODE_PRIVATE);
        }
    }

    public static void setRequestToken(String requestToken) {
        requestToken = requestToken;
        sp.edit().putString(SP_TOKEN, requestToken).commit();
    }


}
