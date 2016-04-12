package com.github.ilioili.demo.base;

import android.content.Context;

import com.github.ilioili.demo.common.loading.CommonLoading;
import com.github.ilioili.demo.common.loading.CommonLoadingFactory;
import com.github.ilioili.demo.tmplate.activity.NavigationDrawerActivity;
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
