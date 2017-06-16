package com.github.why168.http;

import java.util.Arrays;
import java.util.Map;

/**
 * 响应体
 *
 * @author Edwin.Wu
 * @version 2017/6/16 14:12
 * @since JDK1.8
 */
public class Response {
    private final Request request;
    private final int code;
    private final String message;
    private final Map<String, String> headers;
    private final byte[] body;

    public Response(Builder builder) {
        request = builder.request;
        code = builder.code;
        message = builder.message;
        headers = builder.headers;
        body = builder.body;
    }

    public static class Builder {
        private Request request;
        private int code = -1;
        private String message;
        private Map<String, String> headers;
        private byte[] body;

        public Builder() {
        }

        public Builder request(Request request) {
            this.request = request;
            return this;
        }

        public Builder code(int code) {
            this.code = code;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder header(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public Builder body(byte[] body) {
            this.body = body;
            return this;
        }

        public Response build() {
            return new Response(this);
        }
    }

    public Request getRequest() {
        return request;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "Response{" +
                "request=" + request +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", headers=" + headers +
                ", body=" + Arrays.toString(body) +
                '}';
    }
}
