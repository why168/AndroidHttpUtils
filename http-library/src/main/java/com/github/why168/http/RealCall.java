package com.github.why168.http;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

import com.github.why168.http.code.HandlerExecutor;
import com.github.why168.http.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.jar.JarInputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipInputStream;

/**
 * RealCall
 *
 * @author Edwin.Wu
 * @version 2017/6/14 10:06
 * @since JDK1.8
 */
class RealCall implements Call {
    private final Request request;
    private final HttpUtils client;
    private boolean executed;
    private final AtomicBoolean isCancelled = new AtomicBoolean();

    RealCall(HttpUtils client, Request request) {
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

    /**
     * 异步任务
     */
    final class AsyncCall extends NickRunnable {
        private final Callback responseCallback;

        @SuppressLint("SimpleDateFormat")
        private AsyncCall(Callback responseCallback) {
            super("AndroidHttp %s", new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS").format(Calendar.getInstance().getTime()));
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

            String method = request.getMethod();
            if ("GET".equalsIgnoreCase(method)) {
                getHttpRequest(request, client);
            } else if ("POST".equalsIgnoreCase(method)) {
                postHttpRequest(request, client);
            }

        }

        private void postHttpRequest(Request request, HttpUtils httpUtils) {

            HttpURLConnection connection = null;
            InputStream is = null;
            DataOutputStream dataOut = null;
            ByteArrayOutputStream out = null;
            try {
                checkIfCancelled();

                connection = (HttpURLConnection) new URL(request.getUrl()).openConnection();
                connection.setRequestMethod(request.getMethod());
                connection.setConnectTimeout(httpUtils.getConnectTimeout());
                connection.setReadTimeout(httpUtils.getReadTimeout());
                connection.setDoOutput(true);//设置是否向httpUrlConnection输出
                connection.setDoInput(true);//设置是否向httpUrlConnection读入
                addHeader(connection, request.getHeaders());

                checkIfCancelled();

                dataOut = new DataOutputStream(connection.getOutputStream());
                byte[] body = request.getBody();
                if (body != null) {

                    checkIfCancelled();

                    dataOut.write(body);
                }
                dataOut.flush();
                final int responseCode = connection.getResponseCode();

                if (responseCode == -1) {
                    // -1 is returned by getResponseCode() if the response code could not be retrieved.
                    // Signal to the caller that something was wrong with the connection.
                    throw new IOException("Could not retrieve response code from HttpUrlConnection.");
                }

                if (responseCode >= 200 && responseCode <= 300) {
                    // Log
                    Log.e("Edwin", "responseCode = " + responseCode + "\n" +
                            "contentLength = " + StringUtils.getPrintSize(connection.getContentLength()) + "\n" +
                            request.toString());

                    String encoding = connection.getContentEncoding();
                    if (!TextUtils.isEmpty(encoding)) {
                        if ("gzip".equalsIgnoreCase(encoding)) {
                            is = new GZIPInputStream(connection.getInputStream());
                        } else if ("zip".equalsIgnoreCase(encoding)) {
                            is = new ZipInputStream(connection.getInputStream());
                        } else if ("jar".equalsIgnoreCase(encoding)) {
                            is = new JarInputStream(connection.getInputStream());
                        } else if ("deflate".equalsIgnoreCase(encoding)) {
                            is = new InflaterInputStream(connection.getInputStream());
                        }
                    } else {
                        is = connection.getInputStream();
                    }

                    checkIfCancelled();

                    out = new ByteArrayOutputStream(2048);
                    byte[] buffer = new byte[2048];
                    int len;
                    while ((len = is.read(buffer)) != -1) {

                        checkIfCancelled();

                        out.write(buffer, 0, len);
                        out.flush();
                    }

                    Map<String, String> headers = new HashMap<>();

                    Map<String, List<String>> headerFields = connection.getHeaderFields();
                    for (Map.Entry<String, List<String>> entry : headerFields.entrySet()) {
                        String key = entry.getKey();
                        StringBuilder sb = new StringBuilder();
                        for (String value : entry.getValue()) {
                            sb.append(value).append(";");
                        }
                        headers.put(key, sb.toString());
                    }

                    final Response response = new Response.Builder()
                            .request(request)
                            .inputStream(is)
                            .body(out.toByteArray())
                            .code(responseCode)
                            .header(headers)
                            .message("成功:" + connection.getResponseMessage())
                            .build();

                    final Object o = responseCallback.parseNetworkResponse(response, isCancelled);

                    HandlerExecutor.getInstance().execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                responseCallback.onSuccessful(response, o);
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
                    if (dataOut != null)
                        dataOut.close();
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

        private void getHttpRequest(Request request, HttpUtils httpUtils) {
            HttpURLConnection connection = null;
            InputStream is = null;
            ByteArrayOutputStream out = null;
            try {

                checkIfCancelled();

                connection = (HttpURLConnection) new URL(request.getUrl()).openConnection();
                connection.setRequestMethod(request.getMethod());
                connection.setConnectTimeout(httpUtils.getConnectTimeout());
                connection.setReadTimeout(httpUtils.getReadTimeout());
                connection.setDoInput(true);//设置是否向httpUrlConnection读入
                addHeader(connection, request.getHeaders());

                checkIfCancelled();

                int responseCode = connection.getResponseCode();

                if (responseCode == -1) {
                    // -1 is returned by getResponseCode() if the response code could not be retrieved.
                    // Signal to the caller that something was wrong with the connection.
                    throw new IOException("Could not retrieve response code from HttpUrlConnection.");
                }

                if (responseCode >= 200 && responseCode <= 300) {

                    // Log
                    Log.e("Edwin", "responseCode = " + responseCode + "\n" +
                            "contentLength = " + StringUtils.getPrintSize(connection.getContentLength()) + "\n" +
                            request.toString());

                    String encoding = connection.getContentEncoding();
                    if (!TextUtils.isEmpty(encoding)) {
                        if ("gzip".equalsIgnoreCase(encoding)) {
                            is = new GZIPInputStream(connection.getInputStream());
                        } else if ("zip".equalsIgnoreCase(encoding)) {
                            is = new ZipInputStream(connection.getInputStream());
                        } else if ("jar".equalsIgnoreCase(encoding)) {
                            is = new JarInputStream(connection.getInputStream());
                        } else if ("deflate".equalsIgnoreCase(encoding)) {
                            is = new InflaterInputStream(connection.getInputStream());
                        }
                    } else {
                        is = connection.getInputStream();
                    }

                    checkIfCancelled();

//                    out = new ByteArrayOutputStream(2048);
//                    byte[] buffer = new byte[2048];
//                    int len = 0;
//                    long sum = 0;
//                    while ((len = is.read(buffer)) != -1) {
//
//                        if (isCancelled.get())
//                            return;
//
//                        final long finalSum = sum;
//                        final HttpURLConnection finalConnection = connection;
//
//                        HandlerExecutor.getInstance().execute(new Runnable() {
//                            @Override
//                            public void run() {
//                                responseCallback.onProgress(finalSum, finalConnection.getContentLength());
//                            }
//                        });
//
//
//                        out.write(buffer, 0, len);
//                    }
//                    out.flush();

                    Map<String, String> headers = new HashMap<>();

                    Map<String, List<String>> headerFields = connection.getHeaderFields();
                    for (Map.Entry<String, List<String>> entry : headerFields.entrySet()) {
                        String key = entry.getKey();
                        StringBuilder sb = new StringBuilder();
                        for (String value : entry.getValue()) {
                            sb.append(value).append(";");
                        }
                        headers.put(key, sb.toString());
                    }

                    final Response response = new Response.Builder()
                            .request(request)
                            .lenght(connection.getContentLength())
                            .inputStream(is)
//                            .body(out.toByteArray())
                            .code(responseCode)
                            .header(headers)
                            .message("成功:" + connection.getResponseMessage())
                            .build();

                    final Object o = responseCallback.parseNetworkResponse(response,isCancelled);

                    HandlerExecutor.getInstance().execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                responseCallback.onSuccessful(response, o);
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

    private void checkIfCancelled() {
        if (isCancelled.get()) {
            throw new RuntimeException("http cancel");
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
