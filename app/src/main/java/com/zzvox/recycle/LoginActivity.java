package com.zzvox.recycle;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zzvox.recycle.util.Constans;
import com.zzvox.recycle.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etName;
    private EditText etCode;

    private TextView getCode;
    private TextView tvLogin;

    private OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etName = findViewById(R.id.etName);
        etCode = findViewById(R.id.etCode);
        getCode = findViewById(R.id.getCode);
        tvLogin = findViewById(R.id.tvLogin);
        tvLogin.setOnClickListener(this);
        getCode.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SPUtils.getString(Constans.phone) != null) {
            if (!TextUtils.isEmpty(SPUtils.getString(Constans.phone))) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvLogin:
                login();
                break;
            case R.id.getCode:
                getCode();
                break;
            default:
                break;
        }
    }

    private void getCode() {
        String userName = etName.getText().toString().trim();
        if (TextUtils.isEmpty(userName)) {
            ToastUtil.showMessage("请先输入用户账号");
            return;
        }
        HttpUrl.Builder builder = HttpUrl.parse(Constans.url + Constans.GET_CODE).newBuilder();
        builder.addQueryParameter("mobile", userName);
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
                        ToastUtil.showMessage("发送失败，请重新获取验证码");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String data = response.body().string();
                    Log.i("----->", data);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getCode.setClickable(false);
                            timer.start();
                            ToastUtil.showMessage("验证码已发送");
                        }
                    });
                }
            }
        });
    }

    private void login() {
        String userName = etName.getText().toString().trim();
        if (TextUtils.isEmpty(userName)) {
            ToastUtil.showMessage("请先输入用户名");
            return;
        }

        String code = etCode.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            ToastUtil.showMessage("请先输入验证码");
            return;
        }

        FormBody.Builder builder = new FormBody.Builder();
        builder.add("mobile", userName);
        builder.add("captcha", code);
        builder.add("wxSex", "1");
        FormBody formBody = builder.build();
        final Request request = new Request.Builder()
                .url(Constans.url + Constans.USER_LOGIN)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showMessage("登录异常，请重新登录");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String data = response.body().string();
                    Log.i("------->", data);
                    try {
                        JSONObject jo = new JSONObject(data);
                        final String message = jo.getString("message");
                        String code = jo.getString("code");
                        String errorCode = "短信验证码不正确";
                        if ("success".equals(code) && !isChinese(message) && !errorCode.equals(message)) {
                            JSONObject jsonObject = jo.getJSONObject("data");
                            int roleType = jsonObject.getInt("roleType");
                            if (1 == roleType) {
                                String roleNick = jsonObject.getString("nick");
                                String phone = jsonObject.getString("phone");
                                SPUtils.putInt(Constans.roleType, roleType);
                                SPUtils.putString(Constans.roleNick, roleNick);
                                SPUtils.putString(Constans.phone, phone);
                                SPUtils.putString(Constans.token,message);
                                mHandler.sendEmptyMessage(0);
                            } else {
                                mHandler.sendEmptyMessage(1);
                            }
                        } else {
                            Message tempMsg = mHandler.obtainMessage();
                            tempMsg.what = 2;
                            tempMsg.obj = message;
                            mHandler.sendMessage(tempMsg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    ToastUtil.showMessage("登录成功");
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                    break;
                case 1:
                    ToastUtil.showMessage("不好意思，您不是回收人员，无法登陆");
                    break;
                case 2:
                    String message = (String) msg.obj;
                    ToastUtil.showMessage(message);
                default:
                    break;
            }
        }
    };

    public static boolean isChinese(String string) {
        int n = 0;
        for (int i = 0; i < string.length(); i++) {
            n = (int) string.charAt(i);
            if (!(19968 <= n && n < 40869)) {
                return false;
            }
        }
        return true;
    }

    CountDownTimer timer = new CountDownTimer(60000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            int time = (int) (millisUntilFinished / 1000);
            getCode.setText(time + "秒后重发");
        }

        @Override
        public void onFinish() {
            getCode.setClickable(true);
            getCode.setText("获取验证码");
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }

}
