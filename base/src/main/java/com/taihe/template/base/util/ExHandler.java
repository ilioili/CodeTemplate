package com.taihe.template.base.util;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.taihe.template.base.injection.InjectionUtil;

/**
 * Created by Administrator on 2016/1/25.
 * 用于替换默认的Handler在处理回掉时候进行异常捕捉。
 */
public class ExHandler extends Handler {

    @Override
    public void dispatchMessage(Message msg) {
        try {
            super.dispatchMessage(msg);
        } catch (Exception e) {
            Log.e(InjectionUtil.class.getName(), Log.getStackTraceString(e));
        } catch (Error e){
            Log.e(InjectionUtil.class.getName(), Log.getStackTraceString(e));
        }
    }
}
