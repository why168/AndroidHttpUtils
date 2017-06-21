package com.github.why168.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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
        if (isCancelled.get())
            throw new HttpException(HttpException.ErrorType.CANCEL, "http cancel");
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        is.close();
        return bitmap;
    }

}
