package com.github.why168.http;

import java.util.Arrays;
import java.util.Map;

/**
 * @author Edwin.Wu
 * @version 2017/6/13 16:32
 * @since JDK1.8
 */
public final class Request {
    private final String url;
    private final String method;
    private Map<String, String> headers;
    private byte[] bodys;


    private Request(Builder builder) {
        this.url = builder.url;
        this.method = builder.method;
        this.headers = builder.headers;
        this.bodys = builder.bodys;
    }

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return bodys;
    }

    public static class Builder {
        private String url;
        private String method;
        private Map<String, String> headers;
        private byte[] bodys;


        public Builder() {
            this.method = "GET";
        }

        public Builder url(String url) {
            if (url == null) throw new NullPointerException("url == null");
            this.url = url;
            return this;
        }

        public Builder method(String method) {
            if (method == null) throw new NullPointerException("method == null");
            this.method = method;
            return this;
        }

        public Builder headers(Map<String, String> headers) {
            if (headers == null) throw new NullPointerException("headers == null");
            this.headers = headers;
            return this;
        }

        public Builder bodys(byte[] bodys) {
            if (bodys == null) throw new NullPointerException("bodys == null");
            this.bodys = bodys;
            return this;
        }


        public Request build() {
            if (url == null) throw new IllegalStateException("url == null");
            return new Request(this);
        }
    }

    @Override
    public String toString() {
        return "Request{" +
                "url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", headers=" + headers +
                ", bodys=" + Arrays.toString(bodys) +
                '}';
    }
}
