package com.github.why168.androidhttputils;

import android.Manifest;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.why168.http.BitmapCallback;
import com.github.why168.http.Call;
import com.github.why168.http.FileCallback;
import com.github.why168.http.HttpUtils;
import com.github.why168.http.JsonCallback;
import com.github.why168.http.Request;
import com.github.why168.http.Response;
import com.github.why168.http.StringCallback;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

public class HomeActivity extends AppCompatActivity {
    private TextView tv_text;
    private HttpUtils goHttp;
    private ImageView image;

    // 魂斗罗下载 http://124.193.230.12/imtt.dd.qq.com/16891/A1BFDC1BD905CEF01F3076509F920FD3.apk?mkey=59424b6446b6ee89&f=ae12&c=0&fsname=com.tencent.shootgame_1.2.33.7260_337260.apk&csr=1bbd&p=.apk


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        tv_text = (TextView) findViewById(R.id.tv_text);
        image = (ImageView) findViewById(R.id.image);
        goHttp = new HttpUtils();

    }

    public void onGetJSONObject() {
        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", "ADX-SDK-GET");
        headers.put("Content-Type", "text/html;charset=utf-8;application/octet-stream;application/json");

        // http://jubi.com/api/v1/ticker?coin=mryc
        final Request request = new Request.Builder()
                .url("http://www.jubi.com/api/v1/ticker?coin=mryc")
                .method("GET")
                .headers(headers)
                .build();

        Call call = goHttp.newCall(request);
        call.enqueue(new JsonCallback() {
            @Override
            public void onFailure(Exception e) {
                tv_text.setText(e.toString());
            }

            @Override
            public void onSuccessful(Response response, JSONObject results) throws IOException {
                try {
                    String last = results.optString("last");
                    double i = Double.valueOf(last);
                    double def = 0.43;

                    Log.e("Edwin", "results = " + results + "\nlast = " + i + "\n"
                            + "百分比 = " + new BigDecimal(((i - def) / def) * 100).floatValue() + "%");

                    Map<String, String> headers = response.getHeaders();
                    for (Map.Entry<String, String> entry : headers.entrySet()) {
                        System.out.println("headers----" + entry.getKey() + " : " + entry.getValue());
                    }

                    System.out.println("Code = " + response.getCode() + "\nMessage = " + response.getMessage());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void postHttp() {
        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", "ADX-SDK-POST");
        // Content-Type", "text/html;charset=utf-8;application/octet-stream;application/json;
        headers.put("Content-Type", "text/plain");
        String body = "{\"high\":\"0.596000\",\"low\":\"0.500500\",\"buy\":\"0.565000\",\"sell\":\"0.565090\",\"last\":\"0.565090\",\"vol\":40254342.3,\"volume\":22448624.955242}";
        body = "18010476155";
        Request request = new Request.Builder()
                .url("http://dev.xiaobee1.com/api/verification/")
                .method("POST")
                .headers(headers)
                .bodys(body.getBytes())
                .build();

        Call call = goHttp.newCall(request);
        call.enqueue(new StringCallback() {
            @Override
            public void onFailure(Exception e) {
                String s = e.toString();
                tv_text.setText(s);
                System.out.println(s);
            }

            @Override
            public void onSuccessful(Response response, String results) throws IOException {
                tv_text.setText("POST_" + results);
            }
        });
        //TODO 取消单个请求
//        call.cancel();

    }

    public void getFileHttp() {
        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/603.2.4 (KHTML, like Gecko) Version/10.1.1 Safari/603.2.4");
        headers.put("Content-Type", "application/octet-stream");

        // http://jubi.com/api/v1/ticker?coin=mryc
        Request request = new Request.Builder()
//                .url("http://124.193.230.12/imtt.dd.qq.com/16891/A1BFDC1BD905CEF01F3076509F920FD3.apk?mkey=59424b6446b6ee89&f=ae12&c=0&fsname=com.tencent.shootgame_1.2.33.7260_337260.apk&csr=1bbd&p=.apk")
                .url("http://124.193.230.12/imtt.dd.qq.com/16891/3EC8D23CFE6DC65DCA209E5E732ADE93.apk?mkey=5947105e46b6ee89&f=6e20&c=0&fsname=com.u17.comic.phone_3.3.2.1_3320100.apk&csr=1bbd&p=.apk")
                .method("GET")
                .headers(headers)
                .build();


        String path = Environment.getExternalStorageDirectory().getAbsolutePath();

        Call call = goHttp.newCall(request);
        call.enqueue(new FileCallback(path, System.currentTimeMillis() + ".apk") {
            @Override
            public void onFailure(Exception e) {
                Log.e("Edwin", e.toString());
            }

            @Override
            public void onSuccessful(Response response, File results) throws IOException {
                Log.e("Edwin",response.toString());
                Log.e("Edwin", results.getAbsolutePath());
            }

            @Override
            public void onProgress(long progress, long total) {
                Log.e("Edwin", "progress = " + progress + " total = " + total);
            }
        });

    }

    public void onGetBitmap() {
        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", "ADX-SDK-GET");
        headers.put("Content-Type", "text/html;charset=utf-8;application/octet-stream;application/json");

        // http://jubi.com/api/v1/ticker?coin=mryc
        final Request request = new Request.Builder()
                .url("http://f10.baidu.com/it/u=904003689,173509581&fm=76")
                .method("GET")
                .headers(headers)
                .build();

        Call call = goHttp.newCall(request);
        call.enqueue(new BitmapCallback() {
            @Override
            public void onFailure(Exception e) {
                System.out.println(e);
            }

            @Override
            public void onSuccessful(Response response, Bitmap results) throws IOException {
                image.setImageBitmap(results);
            }
        });
    }

    public void onGetJSONObject(View view) {
        Log.e("Edwin", "onGetJSONObject");
        onGetJSONObject();
    }

    public void onPost(View view) {
        Log.e("Edwin", "onPost");
        postHttp();
    }

    public void onGetFile(View view) {
        openGroupPermission();
    }

    public void onStop(View view) {
        Log.e("Edwin", "onStop");
        goHttp.getDispatcher().cancelAll();

    }

    public void onGetBitmap(View view) {
        onGetBitmap();
    }


    @PermissionSuccess(requestCode = 100)
    public void openGroupSuccess() {
        Log.e("Edwin", "onGetFile");
        getFileHttp();
    }

    @PermissionFail(requestCode = 100)
    private void openGroupFail() {
        Toast.makeText(this, "Group permission is not granted", Toast.LENGTH_SHORT).show();
    }

    public void openGroupPermission() {
        PermissionGen
                .with(this)
                .addRequestCode(100)
                .permissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }
}
