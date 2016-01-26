package com.taihe.template.base.http;

/**
 * Created by Administrator on 2016/1/22.
 */
public abstract class TaskHolder<W> {
    TaskHolder nextHolder;

    public abstract Task getTask(W w);
}
