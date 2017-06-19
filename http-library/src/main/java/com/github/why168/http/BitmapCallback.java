package com.github.why168.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * BitmapCallback
 *
 * @author Edwin.Wu
 * @version 2017/6/15 22:57
 * @since JDK1.8
 */
public abstract class BitmapCallback extends Callback<Bitmap> {
    @Override
    public Bitmap parseNetworkResponse(Response response, AtomicBoolean isCancelled) throws HttpException, IOException {
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
        byte[] body = out.toByteArray();
        return BitmapFactory.decodeByteArray(body, 0, body.length);
    }

}
