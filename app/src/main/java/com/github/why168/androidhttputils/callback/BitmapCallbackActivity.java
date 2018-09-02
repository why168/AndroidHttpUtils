package com.github.why168.androidhttputils.callback;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.why168.androidhttputils.R;
import com.github.why168.http.BitmapCallback;
import com.github.why168.http.Call;
import com.github.why168.http.HttpUtils;
import com.github.why168.http.Request;
import com.github.why168.http.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BitmapCallbackActivity extends BaseCallbackActivity {
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitmap_callback);
        image = (ImageView) findViewById(R.id.image);
    }

    public void onPerform(View view) {
        Map<String, String> headers = new HashMap<>();

        final Request request = new Request.Builder()
                .url("http://f10.baidu.com/it/u=904003689,173509581&fm=76")
                .method("GET")
                .headers(headers)
                .build();

        Call call = androidHttp.newCall(request);
        call.enqueue(new BitmapCallback() {
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(BitmapCallbackActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccessful(Response response, Bitmap results) throws IOException {
                Toast.makeText(BitmapCallbackActivity.this, "加载成功", Toast.LENGTH_SHORT).show();
                image.setImageBitmap(results);
            }
        });
    }
}
