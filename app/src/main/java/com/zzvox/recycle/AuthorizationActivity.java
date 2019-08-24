package com.zzvox.recycle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    OkHttpClient client = new OkHttpClient();
    private TextView tvCommit;
    private String deviceCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorizedlogin);
        tvCommit = findViewById(R.id.tvCommit);
        if (getIntent() != null) {
            if (getIntent().getStringExtra("deviceCode") != null) {
                deviceCode = getIntent().getStringExtra("deviceCode");
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
            }
        });

    }

//    private void setName() {
//        HttpUrl.Builder builder = HttpUrl.parse(Constans.SET_NAME).newBuilder();
//        builder.addQueryParameter("name", "17304463597");
//        final Request request = new Request.Builder()
//                .url(builder.build())
//                .build();
//        Call call = client.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        ToastUtil.showMessage("发送失败，请重新获取验证码");
//                    }
//                });
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    String data = response.body().string();
//                    Log.i("------->", data);
//                    isGetCode = true;
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                        }
//                    });
//                }
//            }
//        });
//    }

//    private void getCode() {
//        HttpUrl.Builder builder = HttpUrl.parse(Constans.url + Constans.GET_CODE).newBuilder();
//        builder.addQueryParameter("mobile", "17304463597");
//        final Request request = new Request.Builder()
//                .url(builder.build())
//                .build();
//        Call call = client.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        llCode.setVisibility(View.GONE);
//                        isGetCode = false;
//                        ToastUtil.showMessage("发送失败，请重新获取验证码");
//                    }
//                });
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    String data = response.body().string();
//                    Log.i("------->", data);
//                    isGetCode = true;
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            llCode.setVisibility(View.VISIBLE);
//                            tvCommit.setText("确认提交");
//                            timer.start();
//                            ToastUtil.showMessage("验证码已发送，请在输入框输入.....");
//                        }
//                    });
//                }
//            }
//        });
//    }

//    private void getToken() {
//        String code = editText.getText().toString().trim();
//        if (TextUtils.isEmpty(code)) {
//            ToastUtil.showMessage("请先输入验证码");
//            return;
//        }
//        FormBody.Builder builder = new FormBody.Builder();
//        builder.add("mobile", "17304463597");
//        builder.add("captcha", code);
//        builder.add("roleType", "1");
//        builder.add("wxSex", "1");
//        FormBody formBody = builder.build();
//        final Request request = new Request.Builder()
//                .url(Constans.url + Constans.GET_TOKEN)
//                .post(formBody)
//                .build();
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        ToastUtil.showMessage("确认失败，请重新确认");
//                    }
//                });
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    String data = response.body().string();
//                    Log.i("------->", data);
//                    try {
//                        JSONObject jsonObject = new JSONObject(data);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            ToastUtil.showMessage("确认成功");
//                            startActivity(new Intent(AuthorizationActivity.this, SucessActivity.class));
//                        }
//                    });
//                }
//            }
//        });
//    }

//    CountDownTimer timer = new CountDownTimer(60000, 1000) {
//        @Override
//        public void onTick(long millisUntilFinished) {
//            int time = (int) (millisUntilFinished / 1000);
//            getCode.setText(time + "秒后重发");
//        }
//
//        @Override
//        public void onFinish() {
//            llCode.setVisibility(View.GONE);
//            getCode.setText("请输入验证码");
//            tvCommit.setText("获取验证码");
//            isGetCode = false;
//        }
//    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (timer != null) {
//            timer.cancel();
//        }
    }

}
