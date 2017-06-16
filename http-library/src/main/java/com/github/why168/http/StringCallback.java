package com.github.why168.http;

/**
 * @author Edwin.Wu
 * @version 2017/6/15 21:56
 * @since JDK1.8
 */
public abstract class StringCallback extends Callback<String> {

    @Override
    public String parseNetworkResponse(Response response) throws Exception {
        return new String(response.getBody());
    }
}
