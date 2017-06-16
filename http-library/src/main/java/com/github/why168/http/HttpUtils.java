package com.github.why168.http;

/**
 * @author Edwin.Wu
 * @version 2017/6/13 16:23
 * @since JDK1.8
 */
public class HttpUtils implements Call.Factory {
    private HttpDispatcher dispatcher;
    private int connectTimeout;
    private int readTimeout;
    private int writeTimeout;

    public HttpUtils() {
        this(new Builder());
    }

    private HttpUtils(Builder builder) {
        dispatcher = builder.dispatcher;
        connectTimeout = builder.connectTimeout;
        readTimeout = builder.readTimeout;
        writeTimeout = builder.writeTimeout;
    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    @Override
    public Call newCall(Request request) {
        return new RealCall(this, request);
    }

    public static final class Builder {
        HttpDispatcher dispatcher;
        int connectTimeout;
        int readTimeout;
        int writeTimeout;

        public Builder(HttpUtils httpUtils) {
            dispatcher = httpUtils.dispatcher;
            connectTimeout = httpUtils.connectTimeout;
            readTimeout = httpUtils.readTimeout;
            writeTimeout = httpUtils.writeTimeout;
        }

        public Builder() {
            dispatcher = new HttpDispatcher();
            connectTimeout = 30 * 1000;
            readTimeout = 30 * 1000;
            writeTimeout = 30 * 1000;
        }
    }

    public HttpDispatcher getDispatcher() {
        return dispatcher;
    }

    public void setDispatcher(HttpDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public void connectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public void readTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public void writeTimeout(int writeTimeout) {
        this.writeTimeout = writeTimeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public int getWriteTimeout() {
        return writeTimeout;
    }
}
