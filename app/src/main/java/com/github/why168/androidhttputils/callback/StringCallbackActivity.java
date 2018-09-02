package com.github.why168.androidhttputils.callback;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.github.why168.androidhttputils.R;
import com.github.why168.http.Request;
import com.github.why168.http.Response;
import com.github.why168.http.StringCallback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * StringCallback
 *
 * @author Edwin.Wu
 * @version 2017/6/19 23:28
 * @since JDK1.8
 */
public class StringCallbackActivity extends BaseCallbackActivity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_string_callback);
        textView = (TextView) findViewById(R.id.textView);
    }

    public void onPerform(View view) {
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
                textView.setText(e.toString());
            }

            @Override
            public void onSuccessful(Response response, String results) throws IOException {
                textView.setText(results);
            }
        });
    }
}
