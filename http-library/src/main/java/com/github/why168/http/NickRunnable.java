package com.github.why168.http;


import java.util.Locale;

/**
 * @author Edwin.Wu
 * @version 2017/6/13 16:15
 * @since JDK1.8
 */
public abstract class NickRunnable implements Runnable {
    protected final String name;

    public NickRunnable(String format, Object... args) {
        this.name = String.format(Locale.US, format, args);
    }

    @Override
    public final void run() {
        String oldName = Thread.currentThread().getName();
        Thread.currentThread().setName(name + " --- " + oldName);
        try {
            execute();
        } finally {
            Thread.currentThread().setName(oldName);
        }
    }

    protected abstract void execute();
}
