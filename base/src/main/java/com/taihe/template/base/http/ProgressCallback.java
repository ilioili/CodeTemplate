package com.taihe.template.base.http;

/**
 * Created by Administrator on 2016/1/7.
 */
public interface ProgressCallback {
    /**
     * @param current 已完成字计数
     * @param total 总字节数
     */
    void onProgress(long current, long total);

    void onSucceed();

    void onFail();
}
