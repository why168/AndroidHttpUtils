package com.github.why168.androidhttputils;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

/**
 * WelcomeActivity
 *
 * @author Edwin.Wu
 * @version 2017/6/19 17:17
 * @since JDK1.8
 */
public class WelcomeActivity extends AppCompatActivity {
    private Button button;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        button = (Button) findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.textView);

        Chronometer mChronometer = (Chronometer) findViewById(R.id.chronometer);
        mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.start();
        mChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if (SystemClock.elapsedRealtime() - chronometer.getBase() > 3 * 1000) {
                    chronometer.stop();
                    openGroupPermission();
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this, HomeActivity.class));
                finish();
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGroupPermission();
            }
        });
    }

    @PermissionFail(requestCode = 100)
    private void openGroupFail() {
        Toast.makeText(this, "读写权限未打开", Toast.LENGTH_SHORT).show();
        textView.setVisibility(View.VISIBLE);
    }

    @PermissionSuccess(requestCode = 100)
    public void openGroupSuccess() {
        Toast.makeText(this, "权限开启成功", Toast.LENGTH_SHORT).show();
        button.setVisibility(View.VISIBLE);
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
