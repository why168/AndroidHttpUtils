package com.github.why168.androidhttputils.http.code;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;

/**
 * @author Edwin.Wu
 * @version 2017/6/13 15:50
 * @since JDK1.8
 */
public class HandlerExecutor implements Executor {
    private static HandlerExecutor instance = new HandlerExecutor();

    private HandlerExecutor() {
    }

    public synchronized static HandlerExecutor getInstance() {
        return instance;
    }

    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void execute(@NonNull Runnable command) {
        handler.post(command);
    }
}
