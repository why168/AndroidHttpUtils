package com.github.why168.androidhttputils.callback;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.github.why168.androidhttputils.bean.CoinBean;
import com.github.why168.androidhttputils.R;
import com.github.why168.http.HttpUtils;
import com.github.why168.http.JsonCallback;
import com.github.why168.http.Request;
import com.github.why168.http.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class JsonCallbackActivity extends AppCompatActivity {
    private TextView textView;
    private HttpUtils androidHttp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_callback);

        androidHttp = new HttpUtils();
        textView = (TextView) findViewById(R.id.textView);
    }

    public void onPerform(View view) {
        Request get = new Request
                .Builder()
                .method("GET")
                .url("http://www.jubi.com/api/v1/ticker?coin=mtc")
                .build();


        androidHttp.newCall(get).enqueue(new JsonCallback() {
            @Override
            public void onFailure(Exception e) {
                textView.setText(e.toString());
            }

            @Override
            public void onSuccessful(Response response, JSONObject results) throws IOException {
                /**
                 * {
                 "high": "0.210000",
                 "low": "0.175000",
                 "buy": "0.191000",
                 "sell": "0.192000",
                 "last": "0.191000",
                 "vol": 26634144.7705,
                 "volume": 5132711.015175
                 }
                 */

                try {
                    String high = results.getString("high");
                    String low = results.getString("low");
                    String buy = results.getString("buy");
                    String sell = results.getString("sell");
                    String last = results.getString("last");
                    double vol = results.getDouble("vol");
                    double volume = results.getDouble("volume");

                    CoinBean coinBean = new CoinBean(high, low, buy, sell, last, vol, volume);
                    textView.setText("解析成功\n" + coinBean.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                    textView.setText("解析失败");
                }
            }
        });
    }
}
