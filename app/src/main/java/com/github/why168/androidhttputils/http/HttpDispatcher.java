package com.github.why168.androidhttputils.http;

import android.support.annotation.NonNull;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Edwin.Wu
 * @version 2017/6/13 16:14
 * @since JDK1.8
 */
public class HttpDispatcher {
    private int maxRequests = 64;
    private ExecutorService executorService;

    private final Deque<RealCall.AsyncCall> runningAsyncCalls = new ArrayDeque<>();
    private final Deque<RealCall.AsyncCall> readyAsyncCalls = new ArrayDeque<>();

    public HttpDispatcher(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public HttpDispatcher() {
    }

    public synchronized ExecutorService executorService() {
        if (executorService == null) {
            executorService = new ThreadPoolExecutor(0,
                    Integer.MAX_VALUE,
                    60, TimeUnit.SECONDS,
                    new SynchronousQueue<Runnable>(),
                    threadFactory("Http Dispatcher", false));
        }
        return executorService;
    }


    public synchronized void setMaxRequests(int maxRequests) {
        if (maxRequests < 1) {
            throw new IllegalArgumentException("max < 1: " + maxRequests);
        }
        this.maxRequests = maxRequests;
        promoteCalls();
    }

    public synchronized int getMaxRequests() {
        return maxRequests;
    }

    synchronized void enqueue(RealCall.AsyncCall call) {
        if (runningAsyncCalls.size() < maxRequests) {
            runningAsyncCalls.add(call);
            executorService().execute(call);
        } else {
            readyAsyncCalls.add(call);
        }
    }

    public synchronized int queuedCallsCount() {
        return readyAsyncCalls.size();
    }

    public synchronized void cancelAll() {
        for (RealCall.AsyncCall call : readyAsyncCalls) {
            call.cancel();
        }

        for (RealCall.AsyncCall call : runningAsyncCalls) {
            call.cancel();
        }
    }

    void finished(RealCall.AsyncCall call) {
        int runningCallsCount;
        synchronized (this) {
            if (!runningAsyncCalls.remove(call))
                throw new AssertionError("AsyncCall wasn't running!");
            promoteCalls();
            runningCallsCount = runningCallsCount();
        }
        if (runningCallsCount == 0) {
            //TODO
        }
    }


    private void promoteCalls() {
        if (runningAsyncCalls.size() >= maxRequests)
            return; // Already running max capacity.
        if (readyAsyncCalls.isEmpty())
            return; // No ready calls to promote.

        for (Iterator<RealCall.AsyncCall> i = readyAsyncCalls.iterator(); i.hasNext(); ) {
            RealCall.AsyncCall call = i.next();
            i.remove();
            runningAsyncCalls.add(call);
            executorService().execute(call);

            if (runningAsyncCalls.size() >= maxRequests)
                return; // Reached max capacity.
        }
    }

    public synchronized int runningCallsCount() {
        return runningAsyncCalls.size();
    }

    private static ThreadFactory threadFactory(final String name, final boolean daemon) {
        return new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull Runnable runnable) {
                Thread result = new Thread(runnable, name);
                result.setDaemon(daemon);
                return result;
            }
        };
    }

    /**
     * Returns a snapshot of the calls currently awaiting execution.
     */
    public synchronized List<Call> queuedCalls() {
        List<Call> result = new ArrayList<>();
        for (RealCall.AsyncCall asyncCall : readyAsyncCalls) {
            result.add(asyncCall.get());
        }
        return Collections.unmodifiableList(result);
    }

    /**
     * Returns a snapshot of the calls currently being executed.
     */
    public synchronized List<Call> runningCalls() {
        List<Call> result = new ArrayList<>();
        for (RealCall.AsyncCall asyncCall : runningAsyncCalls) {
            result.add(asyncCall.get());
        }
        return Collections.unmodifiableList(result);
    }

}
