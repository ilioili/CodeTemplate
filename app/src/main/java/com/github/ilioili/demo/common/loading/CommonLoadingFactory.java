package com.github.ilioili.demo.common.loading;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;

import com.github.ilioili.demo.R;


/**
 * Created by Administrator on 2016/1/19.
 */
public class CommonLoadingFactory {
    public static CommonLoading getCommonLoadingDialog(final Activity activity) {
        return new CommonLoading() {

            private Dialog dialog;
            private Activity host = activity;

            private void initDialog() {
                //找沈，李东辉，张庆和*2，马松波，舞东风，崔耀方，王茹*2，孤影*2，呼啸*2，徐鹏*2，刘洋*2，花小波，遇见个*2
                if (null != host) {
                    dialog = new Dialog(host);
                    Window window = dialog.getWindow();
                    if (Build.VERSION.SDK_INT >= 14)
                        window.setDimAmount(0);
                    window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
                    dialog.setContentView(LayoutInflater.from(activity).inflate(R.layout.common_loading, null));
                    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                }
            }

            @Override
            public void showLoading() {
                if (null == host) {
                    return;
                }
                if (null == dialog) {
                    initDialog();
                }
                dialog.show();
            }

            @Override
            public void dismissLoading() {
                if (null != dialog && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }

            @Override
            public void setCancelable(boolean cancelable) {
                dialog.setCancelable(cancelable);
            }

            @Override
            public void setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
                dialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
            }
        };
    }
}
