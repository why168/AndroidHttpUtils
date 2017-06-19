package com.github.why168.androidhttputils.callback;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.why168.androidhttputils.R;
import com.github.why168.http.Call;
import com.github.why168.http.FileCallback;
import com.github.why168.http.HttpUtils;
import com.github.why168.http.Request;
import com.github.why168.http.Response;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileCallbackActivity extends AppCompatActivity {
    private TextView textView;
    private HttpUtils androidHttp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_callback);
        androidHttp = new HttpUtils();
        textView = (TextView) findViewById(R.id.textView);
    }

    public void onPerform(View view) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("FileCallback");
        progressDialog.setMessage("下载中...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);

        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/603.2.4 (KHTML, like Gecko) Version/10.1.1 Safari/603.2.4");
        headers.put("Content-Type", "application/octet-stream");

        Request request = new Request.Builder()
                .url("http://111.202.99.11/imtt.dd.qq.com/16891/4BD2BF1F1F94E3A499D901A1A70D3F5F.apk?mkey=5948279cda6f518e&f=d573&c=0&fsname=com.haobangshou_3.2.5_45.apk&csr=1bbd&p=.apk")
                .method("GET")
                .headers(headers)
                .build();

        String destFilePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String destFileName = System.currentTimeMillis() + ".apk";

        Call call = androidHttp.newCall(request);
        call.enqueue(new FileCallback(destFilePath, destFileName) {
            @Override
            public void onFailure(Exception e) {
                Log.e("Edwin", e.toString());
                Toast.makeText(FileCallbackActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccessful(Response response, File results) throws IOException {
                Toast.makeText(FileCallbackActivity.this, "下载成功", Toast.LENGTH_SHORT).show();
                textView.setText("文件路径：" + results.getAbsolutePath());
                progressDialog.cancel();
                progressDialog.dismiss();

//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setDataAndType(Uri.fromFile(results), "application/vnd.android.package-archive");
//                startActivity(intent);
            }

            @Override
            public void onProgress(long progress, long total) {
                Log.e("Edwin", "progress = " + progress + " total = " + total);

                progressDialog.setMax((int) total);
                progressDialog.setProgress((int) progress);
                if (!progressDialog.isShowing())
                    progressDialog.show();
            }
        });
    }
}
