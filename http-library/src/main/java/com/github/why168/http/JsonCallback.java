package com.github.why168.http;

import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * JsonCallback
 *
 * @author Edwin.Wu
 * @version 2017/6/15 21:56
 * @since JDK1.8
 */
public abstract class JsonCallback extends Callback<JSONObject> {

    @Override
    public JSONObject parseNetworkResponse(Response response, AtomicBoolean isCancelled) throws Exception {
        String results = new String(response.getBody());
        return new JSONObject(results);
    }
}
