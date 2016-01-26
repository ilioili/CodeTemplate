package com.taihe.template.base.http;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.lang.ref.WeakReference;


/**
 * Created by Administrator on 2016/1/21.
 */
public class Task {
    HttpCallback callback;
    RequestBody requestBody;
    String url;
    RequestType requestType;
    WeakReference hookRef;
    Task pre;
    Task next;
    TaskHolder taskHolder;
    private Call call;
    private Request.Builder requestBuilder;

    public Task(RequestType requestType, String url, RequestBody requestBody, HttpCallback httpCallback) {
        this.requestType = requestType;
        this.url = url;
        this.requestBody = requestBody;
        this.callback = httpCallback;
        requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        switch (requestType) {
            case GET:
                break;
            case POST:
                requestBuilder.post(requestBody);
                break;
            case PUT:
                requestBuilder.put(requestBody);
                break;
            case DELETE:
                requestBuilder.delete();
                break;
        }
    }

    public static Task post(String url, RequestBody requestBody, HttpCallback httpCallback) {
        return new Task(RequestType.POST, url, requestBody, httpCallback);
    }

    public static Task get(String url, HttpCallback httpCallback) {
        return new Task(RequestType.GET, url, null, httpCallback);
    }

    public void addHeader(String name, String value) {
        requestBuilder.addHeader(name, value);
    }

    public Call getRequestCall() {
        if (call == null) {
            call = HttpClient.getOkHttpClient().newCall(requestBuilder.build());
        }
        return call;
    }

    public boolean isCanceled() {
        if (hookRef != null && hookRef.get() == null) {//the actvity or other obj has been recycled, very possiblely means that the actvity has been finished
            return true;
        } else if (null != call && call.isCanceled()) {
            return true;
        } else {
            return false;
        }
    }

    public void cancelTask() {
        if (null != hookRef) {
            hookRef.clear();
        }
        if (null != call && !call.isCanceled()) {
            call.cancel();
        }
    }

    public void execute() {
        execute(null);
    }

    /**
     * @param hook if this object has been recycled by jvm, the task will the canceled properly and the callback will not be called
     */
    public void execute(Object hook) {
        setHookRef(hook);
        HttpClient.enqueue(this);
    }

    private void setHookRef(Object hook) {
        if (null == hook) return;
        hookRef = new WeakReference(hook);
    }

    public Task append(TaskHolder taskHolder) {
        if (this.taskHolder == null) {
            this.taskHolder = taskHolder;
        } else {
            TaskHolder t = this.taskHolder;
            while (t.nextHolder != null) {
                t = this.taskHolder.nextHolder;
            }
            t.nextHolder = taskHolder;
        }
        return this;
    }

    public Task append(Task task) {
        Task t = this;
        while (t.next != null) {
            t = t.next;
        }
        t.next = task;
        return this;
    }

    public Task getNext(Object obj) {
        if (null != next) {
            return next;
        } else if (null != taskHolder) {
            next = taskHolder.getTask(obj);
            next.hookRef = hookRef;
            next.pre = this;
            return next;
        } else {
            return null;
        }
    }


}
