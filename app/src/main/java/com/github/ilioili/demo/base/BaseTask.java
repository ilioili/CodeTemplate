package com.github.ilioili.demo.base;

import com.squareup.okhttp.RequestBody;
import com.taihe.template.base.http.HttpCallback;
import com.taihe.template.base.http.RequestType;
import com.taihe.template.base.http.Task;

/**
 * Created by Administrator on 2016/1/25.
 */
public class BaseTask extends Task {
    public BaseTask(RequestType requestType, String url, RequestBody requestBody, HttpCallback httpCallback) {
        super(requestType, url, requestBody, httpCallback);
        addHeader("X-Bmob-Application-Id", "64717f313b145893c1d0ce4d387e4ae6");
        addHeader("X-Bmob-REST-API-Key", "475e2759729a7f74182072d422ad0eeb");
        addHeader("Content-Type", "application/json");
    }
}
