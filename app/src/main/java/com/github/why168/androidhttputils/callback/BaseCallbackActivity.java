package com.github.why168.androidhttputils.callback;

import android.support.v7.app.AppCompatActivity;

import com.github.why168.http.HttpUtils;

public abstract class BaseCallbackActivity extends AppCompatActivity {

    protected static HttpUtils androidHttp;

    static {
        androidHttp = new HttpUtils();
    }
}
