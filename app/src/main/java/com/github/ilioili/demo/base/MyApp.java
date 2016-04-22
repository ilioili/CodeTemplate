package com.github.ilioili.demo.base;

import com.github.ilioili.demo.common.config.UserConfig;
import com.taihe.template.base.BaseApplication;
import com.taihe.template.base.util.FileUtil;
import com.taihe.template.base.util.ToastUtil;


public class MyApp extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        FileUtil.init(this);
        UserConfig.init(this);
        ToastUtil.init(this);
    }
}
