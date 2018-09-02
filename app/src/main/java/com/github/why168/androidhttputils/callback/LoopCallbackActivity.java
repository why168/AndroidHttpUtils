package com.github.why168.androidhttputils.callback;

import android.os.Bundle;
import android.view.View;

import com.github.why168.androidhttputils.R;
import com.github.why168.http.Call;
import com.github.why168.http.Request;
import com.github.why168.http.Response;
import com.github.why168.http.StringCallback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class LoopCallbackActivity extends BaseCallbackActivity {
    int count = 1;

    {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                Map<String, String> headers = new HashMap<>();
                headers.put("User-Agent", "ANDROID-HTTP-GET");
                headers.put("Content-Type", "text/html;charset=utf-8;application/octet-stream;application/json");


                Request get = new Request
                        .Builder()
                        .method("GET")
                        .url("http://www.jubi.com/api/v1/ticker?coin=mtc")
                        .headers(headers)
                        .build();

                androidHttp.newCall(get).enqueue(new StringCallback() {
                    @Override
                    public void onFailure(Exception e) {
                        System.out.println("------loop------onFailure 执行" + count + "次------loop------");
                    }

                    @Override
                    public void onSuccessful(Response response, String results) throws IOException {
                        System.out.println("------loop------onSuccessful 执行" + count + "次------loop------");
                    }
                });

                count++;

            }
        }, 0, 1000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loop_callback);
    }

    public void onLoop(View view) {


        Request get = new Request
                .Builder()
                .method("GET")
                .url("http://www.hao123.com/")
                .tag("GG")
                .build();
        Call call = androidHttp.newCall(get);
        call.enqueue(new StringCallback() {
            @Override
            public void onFailure(Exception e) {
                System.out.println("http://www.hao123.com/ onFailure");
            }

            @Override
            public void onSuccessful(Response response, String results) throws IOException {
                System.out.println("http://www.hao123.com/ onSuccessful");

            }
        });

        call.cancel();
        System.out.println("http://www.hao123.com/ cancel");

    }
}
