package com.taihe.template.base.http;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/12/23.
 */
public class DataWrapper implements Serializable {
    private Object body;
    private Error error;
    private int status;

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}

