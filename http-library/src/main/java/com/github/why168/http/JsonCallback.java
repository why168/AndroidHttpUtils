package com.github.why168.http;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
    public JSONObject parseNetworkResponse(Response response, AtomicBoolean isCancelled) throws HttpException, IOException, JSONException {
        InputStream is = response.getInputStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream(2048);
        byte[] buffer = new byte[2048];
        int len = 0;
        while ((len = is.read(buffer)) != -1) {
            if (isCancelled.get())
                throw new HttpException(HttpException.ErrorType.CANCEL, "http cancel");

            out.write(buffer, 0, len);
        }
        out.flush();
        is.close();
        out.close();
        return new JSONObject(new String(out.toByteArray()));
    }
}
