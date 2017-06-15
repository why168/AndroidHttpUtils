package com.github.why168.androidhttputils.http;

import android.text.TextUtils;
import android.util.Log;

import com.github.why168.androidhttputils.http.code.HandlerExecutor;
import com.github.why168.androidhttputils.util.StringUtils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipInputStream;

import static com.github.why168.androidhttputils.util.StringUtils.getPrintSize;

/**
 * @author Edwin.Wu
 * @version 2017/6/14 10:06
 * @since JDK1.8
 */
public class RealCall implements Call {
    private final Request request;
    private final GoHttp client;
    private boolean executed;
    private final AtomicBoolean isCancelled = new AtomicBoolean();

    public RealCall(GoHttp client, Request request) {
        this.client = client;
        this.request = request;
        this.isCancelled.set(false);
    }

    @Override
    public Request request() {
        return request;
    }


    @Override
    public void enqueue(Callback responseCallback) {
        client.getDispatcher().enqueue(new AsyncCall(responseCallback));
    }

    @Override
    public void cancel() {
        isCancelled.set(true);
    }

    @Override
    public synchronized boolean isExecuted() {
        return executed;
    }

    @Override
    public boolean isCanceled() {
        return isCancelled.get();
    }

    @Override
    public Call clone() {
        return new RealCall(client, request);
    }

    final class AsyncCall extends NickRunnable {
        private final Callback responseCallback;

        private AsyncCall(Callback responseCallback) {
            super("xHttp %s", System.currentTimeMillis());
            this.responseCallback = responseCallback;
        }

        RealCall get() {
            return RealCall.this;
        }

        void cancel() {
            RealCall.this.cancel();
        }

        @Override
        protected void execute() {
            synchronized (this) {
                if (executed) throw new IllegalStateException("Already Executed");
                executed = true;
            }

            if (isCancelled.get())
                return;
            String method = request.getMethod();
            if ("GET".equalsIgnoreCase(method)) {
                getHttpRequest(request, client);
            } else if ("POST".equalsIgnoreCase(method)) {
                postHttpRequest(request, client);
            }
        }

        private void postHttpRequest(Request request, GoHttp httpUtils) {
            HttpURLConnection connection = null;
            InputStream is = null;
            BufferedOutputStream bos = null;
            ByteArrayOutputStream out = null;
            try {
//                String encode = URLEncoder.encode(str, "utf-8");
                connection = (HttpURLConnection) new URL(request.getUrl()).openConnection();
                connection.setRequestMethod(request.getMethod());
                connection.setConnectTimeout(httpUtils.getConnectTimeout());
                connection.setReadTimeout(httpUtils.getReadTimeout());
                connection.setDoOutput(true);//设置是否向httpUrlConnection输出
                connection.setDoInput(true);//设置是否向httpUrlConnection读入
                addHeader(connection, request.getHeaders());
                if (isCancelled.get())
                    return;
                bos = new BufferedOutputStream(connection.getOutputStream());
                byte[] body = request.getBody();
                if (body != null) {
                    if (isCancelled.get())
                        return;
                    bos.write(body);
                }
                bos.flush();
                final int responseCode = connection.getResponseCode();
                if (responseCode >= 200 && responseCode <= 300) {

                    // Log
                    Log.e("Edwin", "responseCode = " + responseCode + "\n" +
                            "contentLength = " + getPrintSize(connection.getContentLength()) + "\n" +
                            request.toString());

                    String encoding = connection.getContentEncoding();
                    if (!TextUtils.isEmpty(encoding)) {
                        if ("gzip".equalsIgnoreCase(encoding)) {
                            is = new GZIPInputStream(connection.getInputStream());
                        } else if ("deflate".equalsIgnoreCase(encoding)) {
                            is = new InflaterInputStream(connection.getInputStream());
                        } else if ("zip".equalsIgnoreCase(encoding)) {
                            is = new ZipInputStream(connection.getInputStream());
                        }
                    } else {
                        is = connection.getInputStream();
                    }
                    if (isCancelled.get())
                        return;
                    out = new ByteArrayOutputStream(2048);
                    byte[] buffer = new byte[2048];
                    int len;
                    while ((len = is.read(buffer)) != -1) {
                        if (isCancelled.get())
                            return;

                        Log.e("Edwin", "len = " + len);

                        out.write(buffer, 0, len);
                        out.flush();
                    }

                    final String result = new String(out.toByteArray(), Charset.forName("UTF-8"));
                    HandlerExecutor.getInstance().execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                responseCallback.onResponse(result);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    HandlerExecutor.getInstance().execute(new Runnable() {
                        @Override
                        public void run() {
                            responseCallback.onFailure(new RuntimeException("error code = " + responseCode));
                        }
                    });
                }
            } catch (final Exception e) {
                e.printStackTrace();
                HandlerExecutor.getInstance().execute(new Runnable() {
                    @Override
                    public void run() {
                        responseCallback.onFailure(e);
                    }
                });
            } finally {
                try {
                    if (bos != null)
                        bos.close();
                    if (is != null)
                        is.close();
                    if (out != null)
                        out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null)
                        connection.disconnect();
                    httpUtils.getDispatcher().finished(this);
                }
            }
        }

        private void getHttpRequest(Request request, GoHttp httpUtils) {
            HttpURLConnection connection = null;
            InputStream is = null;
            ByteArrayOutputStream out = null;
            try {
                connection = (HttpURLConnection) new URL(request.getUrl()).openConnection();
                connection.setRequestMethod(request.getMethod());
                connection.setConnectTimeout(httpUtils.getConnectTimeout());
                connection.setReadTimeout(httpUtils.getReadTimeout());
                connection.setDoInput(true);//设置是否向httpUrlConnection读入
                addHeader(connection, request.getHeaders());
                if (isCancelled.get())
                    return;

                int status = connection.getResponseCode();
                if (status >= 200 && status <= 300) {

                    // Log
                    Log.e("Edwin", "responseCode = " + status + "\n" +
                            "contentLength = " + StringUtils.getPrintSize(connection.getContentLength()) + "\n" +
                            request.toString());

                    String encoding = connection.getContentEncoding();
                    if (!TextUtils.isEmpty(encoding)) {
                        if ("gzip".equalsIgnoreCase(encoding)) {
                            is = new GZIPInputStream(connection.getInputStream());
                        } else if ("deflate".equalsIgnoreCase(encoding)) {
                            is = new InflaterInputStream(connection.getInputStream());
                        } else if ("zip".equalsIgnoreCase(encoding)) {
                            is = new ZipInputStream(connection.getInputStream());
                        }
                    } else {
                        is = connection.getInputStream();
                    }

                    if (isCancelled.get())
                        return;

                    out = new ByteArrayOutputStream(2048);
                    byte[] buffer = new byte[2048];
                    int len;
                    while ((len = is.read(buffer)) != -1) {
                        if (isCancelled.get())
                            return;

                        Log.e("Edwin", "len = " + len);

                        out.write(buffer, 0, len);
                        out.flush();
                    }
                    final String result = new String(out.toByteArray(), Charset.forName("UTF-8"));
                    HandlerExecutor.getInstance().execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                responseCallback.onResponse(result);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

            } catch (final Exception e) {
                e.printStackTrace();
                HandlerExecutor.getInstance().execute(new Runnable() {
                    @Override
                    public void run() {
                        responseCallback.onFailure(e);
                    }
                });
            } finally {
                try {
                    if (is != null)
                        is.close();
                    if (out != null)
                        out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                    httpUtils.getDispatcher().finished(this);
                }
            }
        }


    }

    private void addHeader(HttpURLConnection connection, Map<String, String> headers) {
        if (headers == null || headers.size() == 0)
            return;

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            if (isCancelled.get())
                return;
            connection.addRequestProperty(entry.getKey(), entry.getValue());
        }
    }

    String toLoggableString() {
        return (isCanceled() ? "canceled " : "")
                + " to " + redactedUrl();
    }

    String redactedUrl() {
        return request.getUrl();
    }


}
