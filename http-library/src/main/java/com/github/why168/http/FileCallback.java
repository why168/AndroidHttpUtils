package com.github.why168.http;

import java.io.File;

/**
 * @author Edwin.Wu
 * @version 2017/6/15 21:57
 * @since JDK1.8
 */
public abstract class FileCallback extends Callback<File> {
    @Override
    public File parseNetworkResponse(Response response) throws Exception {
        return null;
    }
}
