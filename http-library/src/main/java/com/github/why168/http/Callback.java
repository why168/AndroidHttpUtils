package com.github.why168.http;

import java.io.IOException;

/**
 * Callback
 *
 * @author Edwin.Wu
 * @version 2017/6/13 16:18
 * @since JDK1.8
 */
public abstract class Callback<T> {

    public abstract T parseNetworkResponse(Response response) throws Exception;

    public abstract void onFailure(Exception e);

    public abstract void onSuccessful(Response response, T results) throws IOException;

    public void onProgress(long progress, long total) {

    }
}
