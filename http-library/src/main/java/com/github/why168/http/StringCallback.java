package com.github.why168.http;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * StringCallback
 *
 * @author Edwin.Wu
 * @version 2017/6/15 21:56
 * @since JDK1.8
 */
public abstract class StringCallback extends Callback<String> {

    @Override
    public String parseNetworkResponse(Response response, AtomicBoolean isCancelled) throws Exception {
        InputStream is = response.getInputStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream(2048);
        byte[] buffer = new byte[2048];
        int len = 0;
        while ((len = is.read(buffer)) != -1) {
            if (isCancelled.get()) {
                throw new RuntimeException("http cancel");
            }
            out.write(buffer, 0, len);
        }
        out.flush();
        is.close();
        out.close();
        return new String(out.toByteArray());
    }
}
