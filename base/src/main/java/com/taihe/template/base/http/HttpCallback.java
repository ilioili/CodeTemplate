package com.taihe.template.base.http;

/**
 * Created by Administrator on 2015/12/8.
 */
public interface HttpCallback <T>{
    Object onSucceed(T t);//成功
    void onFail(Error error);//失败
}
