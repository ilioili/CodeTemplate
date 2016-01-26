package com.taihe.template.app.common.loading;

/**
 * Created by Administrator on 2016/1/19.
 * 负责一般的加载显示
 */
public interface CommonLoading {
    public void showLoading();
    public void dismissLoading();
    public void setCancelable(boolean cancelable);
    public void setCanceledOnTouchOutside(boolean canceledOnTouchOutside);
}
