package com.github.why168.androidhttputils.http;

import java.io.IOException;

/**
 * @author Edwin.Wu
 * @version 2017/6/13 16:18
 * @since JDK1.8
 */
public interface Callback {
    void onFailure(Exception e);


    void onResponse(String results) throws IOException;
}
