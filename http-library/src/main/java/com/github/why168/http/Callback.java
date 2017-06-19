package com.github.why168.http;

import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Callback
 *
 * @author Edwin.Wu
 * @version 2017/6/13 16:18
 * @since JDK1.8
 */
public abstract class Callback<T> {
    /**
     * 目标文件存储的文件夹路径
     */
    private String destFilePath;
    /**
     * 目标文件存储的文件名
     */
    private String destFileName;


    public abstract T parseNetworkResponse(Response response, AtomicBoolean isCancelled) throws HttpException, IOException, JSONException;

    public abstract void onFailure(Exception e);

    public abstract void onSuccessful(Response response, T results) throws IOException;

    public void onCanceled(HttpException e) {
    }

    public void onProgress(long progress, long total) {

    }
}
