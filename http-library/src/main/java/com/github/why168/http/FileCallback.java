package com.github.why168.http;

import com.github.why168.http.code.HandlerExecutor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * FileCallback
 *
 * @author Edwin.Wu
 * @version 2017/6/15 21:57
 * @since JDK1.8
 */
public abstract class FileCallback extends Callback<File> {
    /**
     * 目标文件存储的文件夹路径
     */
    private String destFilePath;
    /**
     * 目标文件存储的文件名
     */
    private String destFileName;

    public FileCallback(String destFilePath, String destFileName) {
        this.destFilePath = destFilePath;
        this.destFileName = destFileName;
    }

    @Override
    public File parseNetworkResponse(Response response, AtomicBoolean isCancelled) throws HttpException,IOException {
        InputStream is = response.getInputStream();
        final long length = response.getLength();
        FileOutputStream fos;
        byte[] buf = new byte[2048];
        int len;
        long sum = 0;

        //create file
        File dir = new File(destFilePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, destFileName);

        fos = new FileOutputStream(file);
        while ((len = is.read(buf)) != -1) {
            if (isCancelled.get())
                throw new HttpException(HttpException.ErrorType.CANCEL, "http cancel");

            sum += len;
            fos.write(buf, 0, len);
            final long finalSum = sum;
            HandlerExecutor.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    onProgress(finalSum, length);
                }
            });
        }

        fos.flush();
        is.close();
        fos.close();
        return file;
    }

}
