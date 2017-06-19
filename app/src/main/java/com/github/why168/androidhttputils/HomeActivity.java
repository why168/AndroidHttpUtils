package com.github.why168.androidhttputils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.why168.androidhttputils.callback.BitmapCallbackActivity;
import com.github.why168.androidhttputils.callback.FileCallbackActivity;
import com.github.why168.androidhttputils.callback.JsonCallbackActivity;
import com.github.why168.androidhttputils.callback.StringCallbackActivity;

/**
 * HomeActivity
 *
 * @author Edwin.Wu
 * @version 2017/6/19 23:30
 * @since JDK1.8
 */
public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void startStringCallback(View view) {
        startActivity(new Intent(this, StringCallbackActivity.class));
    }

    public void startJsonCallback(View view) {
        startActivity(new Intent(this, JsonCallbackActivity.class));
    }

    public void startFileCallback(View view) {
        startActivity(new Intent(this, FileCallbackActivity.class));
    }

    public void startBitmapCallback(View view) {
        startActivity(new Intent(this, BitmapCallbackActivity.class));
    }


}
