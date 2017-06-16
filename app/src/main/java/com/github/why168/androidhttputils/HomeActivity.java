package com.github.why168.androidhttputils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.why168.http.Call;
import com.github.why168.http.Callback;
import com.github.why168.http.HttpUtils;
import com.github.why168.http.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    private TextView tv_text;
    private HttpUtils goHttp;

    // 魂斗罗下载 http://124.193.230.12/imtt.dd.qq.com/16891/A1BFDC1BD905CEF01F3076509F920FD3.apk?mkey=59424b6446b6ee89&f=ae12&c=0&fsname=com.tencent.shootgame_1.2.33.7260_337260.apk&csr=1bbd&p=.apk


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        tv_text = (TextView) findViewById(R.id.tv_text);
        goHttp = new HttpUtils();

    }

    public void getHttp() {
        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", "ADX-SDK-GET");
        headers.put("Content-Type", "text/html;charset=utf-8;application/octet-stream;application/json");

        // http://jubi.com/api/v1/ticker?coin=mryc
        Request request = new Request.Builder()
                .url("http://www.jubi.com/api/v1/ticker?coin=mryc")
                .method("GET")
                .headers(headers)
                .build();

        Call call = goHttp.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Exception e) {
                tv_text.setText(e.toString());
            }

            @Override
            public void onSuccessful(String results) throws IOException {
                try {
                    JSONObject jsonObject = new JSONObject(results);
                    String last = jsonObject.optString("last");
                    double i = Double.valueOf(last);
                    double def = 0.43;

                    Log.e("Edwin", "results = " + results + "\nlast = " + i + "\n"
                            + "百分比 = " + new BigDecimal(((i - def) / def) * 100).floatValue() + "%");
                } catch (JSONException e) {
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
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Exception e) {
                String s = e.toString();
                tv_text.setText(s);
                System.out.println(s);
            }

            @Override
            public void onSuccessful(String results) throws IOException {
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
                .url("http://124.193.230.12/imtt.dd.qq.com/16891/A1BFDC1BD905CEF01F3076509F920FD3.apk?mkey=59424b6446b6ee89&f=ae12&c=0&fsname=com.tencent.shootgame_1.2.33.7260_337260.apk&csr=1bbd&p=.apk")
                .method("GET")
                .headers(headers)
                .build();


        Call call = goHttp.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Exception e) {
                tv_text.setText(e.toString());
            }

            @Override
            public void onSuccessful(String results) throws IOException {
                tv_text.setText("GET_" + results);
            }
        });
    }


    public void onGet(View view) {
        Log.e("Edwin", "onGet");
        getHttp();
    }

    public void onPost(View view) {
        Log.e("Edwin", "onPost");
        postHttp();
    }

    public void onGetFile(View view) {
        Log.e("Edwin", "onGetFile");
        getFileHttp();
    }

    public void onStop(View view) {
        Log.e("Edwin", "onStop");
        goHttp.getDispatcher().cancelAll();
    }
}
