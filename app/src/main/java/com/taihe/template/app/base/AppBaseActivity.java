package com.taihe.template.app.base;

import android.content.Context;

import com.taihe.template.app.common.loading.CommonLoading;
import com.taihe.template.app.common.loading.CommonLoadingFactory;
import com.taihe.template.app.tmplate.activity.NavigationDrawerActivity;
import com.taihe.template.base.BaseActivity;

/**
 * Created by Administrator on 2016/1/9.
 */
public class AppBaseActivity extends BaseActivity {

    private CommonLoading commonLoading;

    public void dismissLoading() {
        try {
            if(null!=commonLoading){
                commonLoading.dismissLoading();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showLoading() {
        try {
            if(commonLoading==null){
                commonLoading = CommonLoadingFactory.getCommonLoadingDialog(this);
            }
            commonLoading.showLoading();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exitApp(Context context){
        startActivity(NavigationDrawerActivity.newIntent(context, NavigationDrawerActivity.Action.EXIT_APP));
    }
}
