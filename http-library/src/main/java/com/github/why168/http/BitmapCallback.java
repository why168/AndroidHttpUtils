package com.github.why168.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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
    public Bitmap parseNetworkResponse(Response response, AtomicBoolean isCancelled) throws Exception {
        byte[] body = response.getBody();
        return BitmapFactory.decodeByteArray(body, 0, body.length);
    }

}
