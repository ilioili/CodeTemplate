package com.taihe.template.base.http;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.taihe.template.base.util.LogUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;


/**
 * Created by Administrator on 2015/12/8.
 */
public class HttpClient {

    private static OkHttpClient okHttpClient;
    private static Handler handler;

    static {
        okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(50, TimeUnit.SECONDS);
        okHttpClient.setWriteTimeout(50, TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(100, TimeUnit.SECONDS);
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void dispatchMessage(Message msg) {
                try {
                    super.dispatchMessage(msg);
                } catch (Exception e) {
                    Log.e(HttpClient.class.getName(), Log.getStackTraceString(e));
                } catch (java.lang.Error e) {
                    Log.e(HttpClient.class.getName(), Log.getStackTraceString(e));
                }
            }
        };
    }

    public static Call download(String url, final File file, final ProgressCallback progressCallback) {
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressCallback.onFail();
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.code() == 200) {
                    File tmpFile = new File(file + ".tmp");
                    FileOutputStream fos = new FileOutputStream(tmpFile, false);
                    InputStream is = response.body().byteStream();
                    long readSum = 0;
                    byte[] buffer = new byte[32 * 1024];
                    int byteRead = is.read(buffer);
                    while (-1 != byteRead) {
                        readSum += byteRead;
                        fos.write(buffer, 0, byteRead);
                        progressCallback.onProgress(readSum, response.body().contentLength());
                    }
                    if (readSum == response.body().contentLength()) {
                        tmpFile.renameTo(file);
                        handler.post(new Runnable() {
                            public void run() {
                                progressCallback.onSucceed();
                            }
                        });
                    } else {
                        handler.post(new Runnable() {
                            public void run() {
                                progressCallback.onFail();
                            }
                        });
                    }
                }
            }
        });
        return call;
    }

    static void enqueue(final Task task) {
        task.getRequestCall().enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                if (task.getRequestCall().isCanceled()) {

                } else {
                    if (e instanceof SocketTimeoutException) {
                        onFail(task.callback, new Error(Error.ErrorCode.CONNECTION_ERROR));
                    } else if (e instanceof UnknownHostException) {
                        onFail(task.callback, new Error(Error.ErrorCode.CONNECTION_TIME_OUT));
                    } else {
                        onFail(task.callback, new Error(Error.ErrorCode.UNKNOW_ERROR));
                    }
                }
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (task.getRequestCall().isCanceled()) {

                } else {
                    if (response.isSuccessful()) {
                        String responseStr = response.body().string();
                        log(task.url, task.requestBody, responseStr);
                        parseResponse(task, responseStr);
                    } else {
                        onFail(task.callback, new Error(Error.ErrorCode.SERVER_DOWN));
                    }
                }
            }
        });
    }

    public static OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }


    private static void log(String url, RequestBody requestBody, String response) {
        try {
            if (null == requestBody) {
                LogUtil.e("HttpRequest", "Url:" + url);
            } else {
                Field content = requestBody.getClass().getDeclaredField("val$content");
                content.setAccessible(true);
                byte[] data = (byte[]) content.get(requestBody);
                String params = new String(data, "UTF-8");
                LogUtil.e("HttpRequest", "Url:" + url + "    Params:" + params);
            }
            LogUtil.e("HttpResponse", response);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static <T> void parseResponse(Task task, String responseBody) {
        try {
            if (task.isCanceled()) {//JSON反序列化之前检查一下是否已经取消任务
                return;
            }
            Gson gson = new Gson();
            DataWrapper dataWrapper = gson.fromJson(responseBody, new TypeToken<DataWrapper>() {
            }.getType());
            switch (dataWrapper.getStatus()) {
                case 1:
                    Type type = task.callback.getClass().getGenericInterfaces()[0];
                    type = ((ParameterizedType) type).getActualTypeArguments()[0];
                    T t;
                    if (type == String.class) {
                        t = (T) dataWrapper.getBody();
                    } else {
                        t = gson.fromJson(gson.toJson(dataWrapper.getBody()), type);
                    }
                    if (!task.isCanceled()) {
                        onSuccess(task, t);
                    }
                    break;
                case 2://请求错误，读取errorMsg
                    onFail(task.callback, new Error(Error.ErrorCode.SERVER_LOGIC_ERROR));
                    break;
            }
        } catch (Exception e) {
            onFail(task.callback, new Error(Error.ErrorCode.UNKNOW_ERROR));
            LogUtil.e(HttpClient.class.getName(), e);
        }
    }

    private static void onSuccess(final Task task, final Object obj) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Object o = task.callback.onSucceed(obj);
                Task next = task.getNext(o);
                if (null != next) {
                    enqueue(next);
                }
            }
        });
    }

    private static void onFail(final HttpCallback httpCallback, final Error error) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                httpCallback.onFail(error);
            }
        });
    }
}
