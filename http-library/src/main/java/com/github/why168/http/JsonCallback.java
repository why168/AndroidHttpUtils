package com.github.why168.http;

import org.json.JSONObject;

/**
 * @author Edwin.Wu
 * @version 2017/6/15 21:56
 * @since JDK1.8
 */
public abstract class JsonCallback extends Callback<JSONObject> {

    @Override
    public JSONObject parseNetworkResponse(Response response) throws Exception {
        String results = new String(response.getBody());
        return new JSONObject(results);
    }
}
