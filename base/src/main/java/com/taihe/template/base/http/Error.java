package com.taihe.template.base.http;

/**
 * Created by Administrator on 2015/12/8.
 */
public class Error {
    private ErrorCode errorCode;//
    private int serverCode;//请求成功，服务器处理失败返回的错误码
    private String serverMsg;//请求成功，服务器处理失败返回的错误提示
    private Object extra;//请求成功，服务器处理失败返回的错误附加信息

    Error(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;

    }

    public int getServerCode() {
        return serverCode;
    }

    public void setServerCode(int serverCode) {
        this.serverCode = serverCode;
    }

    public String getServerMsg() {
        return serverMsg;
    }

    public void setServerMsg(String serverMsg) {
        this.serverMsg = serverMsg;
    }

    public Object getExtra() {
        return extra;
    }

    public void setExtra(Object extra) {
        this.extra = extra;
    }
    public enum ErrorCode {
        SERVER_LOGIC_ERROR,//服务端处理异常
        CONNECTION_TIME_OUT,//网络超时
        CONNECTION_ERROR,//网络连接异常
        SERVER_DOWN,//服务器未正常相应，如404， 501等
        UNKNOW_ERROR//位置错误
    }
}
