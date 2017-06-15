package com.github.why168.androidhttputils.http;

/**
 * @author Edwin.Wu
 * @version 2017/6/14 09:47
 * @since JDK1.8
 */
public interface Call extends Cloneable {
    Request request();

    void enqueue(Callback responseCallback);

    void cancel();

    boolean isExecuted();

    boolean isCanceled();

    Call clone();

    interface Factory {
        Call newCall(Request request);
    }
}
