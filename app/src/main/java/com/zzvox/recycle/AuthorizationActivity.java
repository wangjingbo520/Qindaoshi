package com.zzvox.recycle;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zzvox.recycle.util.Constans;
import com.zzvox.recycle.util.ToastUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AuthorizationActivity extends AppCompatActivity {

    private OkHttpClient client = new OkHttpClient();
    private String deviceCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorizedlogin);
        TextView tvCommit = findViewById(R.id.tvCommit);
        TextView tvUserName = findViewById(R.id.tvUserName);
        TextView tvPhone = findViewById(R.id.tvPhone);
        TextView tvDeviceCode = findViewById(R.id.tvDeviceCode);
        String nick = SPUtils.getString(Constans.roleNick);
        String phone = SPUtils.getString(Constans.phone);
        tvUserName.setText(nick);
        tvPhone.setText(phone);
        if (getIntent() != null) {
            if (getIntent().getStringExtra("deviceCode") != null) {
                deviceCode = getIntent().getStringExtra("deviceCode");
                tvDeviceCode.setText(deviceCode);
            }
        }
        findViewById(R.id.flBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commit();
            }
        });
    }

    private void commit() {
        HttpUrl.Builder builder = HttpUrl.parse(Constans.SET_NAME).newBuilder();
        builder.addQueryParameter("name", deviceCode);
        final Request request = new Request.Builder()
                .url(builder.build())
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showMessage("提交失败，请重新提交");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showSucessDialog();
                        }
                    });
                }
            }
        });
    }

    public void showSucessDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("恭喜您，提交成功");
        builder.setIcon(R.mipmap.touxiang);
        builder.setCancelable(false);
        //正面的按钮
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
