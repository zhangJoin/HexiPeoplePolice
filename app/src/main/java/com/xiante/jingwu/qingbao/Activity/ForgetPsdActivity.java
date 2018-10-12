package com.xiante.jingwu.qingbao.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eallnetwork.framework.FailCallback;
import com.example.eallnetwork.framework.NetworkFactory;
import com.example.eallnetwork.framework.SuccessfulCallback;
import com.example.eallnetwork.workUtils.OkhttpFactory;
import com.xiante.jingwu.qingbao.Dialog.LoaddingDialog;
import com.xiante.jingwu.qingbao.NetWork.UrlManager;
import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.CodeExceptionUtil;
import com.xiante.jingwu.qingbao.Util.Global;
import com.xiante.jingwu.qingbao.Util.IsNullOrEmpty;
import com.xiante.jingwu.qingbao.Util.Utils;

import org.json.JSONException;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zhong on 2018/5/18.
 */

public class ForgetPsdActivity extends BaseActivity {

    EditText currentPhoneET, verycodeET, newPsdET, againPsdET;
    TextView obtionVerifyCodeTV;
    View commitBT;
    private LoaddingDialog loaddingDialog;
    private int leftTime = 0;
    Timer timer;
    Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_psd_activity);
        initView();
        initData();
        initListener();
    }

    @Override
    public void initView() {
        currentPhoneET = findViewById(R.id.currentPhoneET);
        verycodeET = findViewById(R.id.verycodeET);
        newPsdET = findViewById(R.id.newPsdET);
        againPsdET = findViewById(R.id.againPsdET);
        obtionVerifyCodeTV = findViewById(R.id.obtionVerifyCodeTV);
        commitBT = findViewById(R.id.commitTV);
        loaddingDialog = new LoaddingDialog(this);
        initTitlebar("找回密码", "", "");
//        Utils.setEditTextInhibitInputSpace(currentPhoneET);
//        Utils.setEditTextInhibitInputSpace(newPsdET);
//        Utils.setEditTextInhibitInputSpace(againPsdET);
    }

    @SuppressLint("HandlerLeak")
    @Override
    public void initData() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (leftTime <= 0) {
                    timer.cancel();
                    obtionVerifyCodeTV.setEnabled((!TextUtils.isEmpty(phone) && phone.length() >= 11) ? true : false);
                    obtionVerifyCodeTV.setTextColor((!TextUtils.isEmpty(phone) && phone.length() >= 11) ? getResources().getColor(R.color.textBlue) : getResources().getColor(R.color.texthintcolor));
                    obtionVerifyCodeTV.setText("发送验证码");
                } else {
                    obtionVerifyCodeTV.setTextColor(getResources().getColor(R.color.texthintcolor));
                    obtionVerifyCodeTV.setText(leftTime + "秒后重新发送");
                }
            }
        };
    }

    private String phone;

    @Override
    public void initListener() {
        currentPhoneET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                phone = currentPhoneET.getText().toString();
                if (phone.length() > 11) {

                    currentPhoneET.setText(phone.substring(0, 11));
                }
                obtionVerifyCodeTV.setEnabled((!TextUtils.isEmpty(phone) && phone.length() >= 11) ? true : false);
                obtionVerifyCodeTV.setTextColor(s.length() >= 11 && leftTime <= 0 ? getResources().getColor(R.color.textBlue) : getResources().getColor(R.color.texthintcolor));
            }
        });


        obtionVerifyCodeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (leftTime > 0) {
                    return;
                }
                String phone = currentPhoneET.getText().toString();
                if (checkPhone(phone)) {
                    sendVerifyCode(phone);
                } else {
                    Toast.makeText(ForgetPsdActivity.this, "手机号有误", Toast.LENGTH_SHORT).show();
                }
            }
        });

        commitBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commitDialog();
            }
        });
        setBtnBackgroudListen(verycodeET);
        setBtnBackgroudListen(newPsdET);
        setBtnBackgroudListen(againPsdET);
    }

    private void setBtnBackgroudListen(EditText mEditText) {
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                changeBtnCommitBackground();
            }
        });
    }

    private void changeBtnCommitBackground() {
        String currentPhone = currentPhoneET.getText().toString();
        String veryCode = verycodeET.getText().toString();
        String newPwd = newPsdET.getText().toString();
        String againPwd = againPsdET.getText().toString();
        if (!TextUtils.isEmpty(currentPhone) && !TextUtils.isEmpty(veryCode) && !TextUtils.isEmpty(newPwd) && !TextUtils.isEmpty(againPwd)) {
            commitBT.setEnabled(true);
            commitBT.setBackgroundResource(R.drawable.send_power);
        } else {
            commitBT.setEnabled(false);
            commitBT.setBackgroundResource(R.drawable.send_gray1);
        }
    }

    private void commitDialog() {
        final String phone = currentPhoneET.getText().toString();
        final String veryfycode = verycodeET.getText().toString();
        final String newpsd = newPsdET.getText().toString();
        final String againcode = againPsdET.getText().toString();
        if (phone.equals("")) {
            Toast.makeText(this, "手机号不能为空,请重新输入", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!checkPhone(phone)) {
            Toast.makeText(ForgetPsdActivity.this, "手机号有误", Toast.LENGTH_SHORT).show();
        }
        if (veryfycode.equals("")) {
            Toast.makeText(this, "验证码为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!newpsd.equals(againcode)) {
            Toast.makeText(this, "密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!checkPsd(newpsd)) {
            Toast.makeText(this, "密码格式不正确", Toast.LENGTH_SHORT).show();
            return;
        }
        if (veryfycode.equals("")) {
            Toast.makeText(this, "验证码为空", Toast.LENGTH_SHORT).show();
            return;
        }
        verifyCode(veryfycode, phone, newpsd);
    }

    private void verifyCode(String veryfycode, final String phoneStr, final String psd) {

        loaddingDialog.showDialog();
        if (IsNullOrEmpty.isEmpty(veryfycode)) {
            Toast.makeText(this, "验证码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        NetworkFactory networkFactory = OkhttpFactory.getInstance();
        SuccessfulCallback successfulCallback = new SuccessfulCallback() {
            @Override
            public void success(String str) throws JSONException {
                loaddingDialog.dismissAniDialog();
                if (new CodeExceptionUtil(ForgetPsdActivity.this).dealException(str)) {
                    modifyPsd(phoneStr, psd, psd);
                }
            }

            @Override
            public void success(InputStream ism, long conentLength) {

            }
        };

        FailCallback failCallback = new FailCallback() {
            @Override
            public void fail(String str) {
                loaddingDialog.dismissAniDialog();
            }
        };
        HashMap<String, String> param = new HashMap<>();
        param.put("strSmsMobile", phoneStr);
        param.put("strSmsCode", veryfycode);
        StringBuilder builder = new StringBuilder(new UrlManager(this).getData_url()).append(Global.VERYFY_PHONE_CODE);
        networkFactory.start(NetworkFactory.METHOD_GET, builder.toString(), param, successfulCallback, failCallback);
    }


    private void startCountLeftTime() {
        leftTime = 60;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                leftTime--;
                handler.sendEmptyMessage(0);
            }
        }, 1000, 1000);
    }

    private void sendVerifyCode(String phoneStr) {
        // 注册一个事件回调，用于处理发送验证码操作的结果
        boolean chinaPhoneLegal = Utils.isChinaPhoneLegal(phoneStr);
        if (chinaPhoneLegal) {
            send_phoneCode(phoneStr);
        } else {
            Toast.makeText(ForgetPsdActivity.this, getString(R.string.correntPhone), Toast.LENGTH_SHORT).show();
        }
    }


    public void send_phoneCode(String phonecode) {
        final LoaddingDialog senddialog = new LoaddingDialog(this);
        senddialog.setCancelable(false);
        senddialog.showDialog();
        NetworkFactory networkFactory = OkhttpFactory.getInstance();
        SuccessfulCallback successfulCallback = new SuccessfulCallback() {
            @Override
            public void success(String str) throws JSONException {
                senddialog.dismissAniDialog();
                if (new CodeExceptionUtil(ForgetPsdActivity.this).dealException(str)) {
                    startCountLeftTime();
                } else {
                    Toast.makeText(ForgetPsdActivity.this, "获取验证码失败，请重试！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void success(InputStream ism, long conentLength) {

            }
        };
        FailCallback failCallback = new FailCallback() {
            @Override
            public void fail(String str) {
                senddialog.dismissAniDialog();
            }
        };
        HashMap<String, String> param = new HashMap<>();
        param.put("strSmsMobile", phonecode);
        UrlManager urlManager = new UrlManager(this);
        StringBuilder builder = new StringBuilder(urlManager.getData_url()).append(Global.SEND_PHONE_VERIFYCODE);
        networkFactory.start(NetworkFactory.METHOD_GET, builder.toString(), param, successfulCallback, failCallback);
    }


    private void modifyPsd(final String phone, String newpsd, String againpsd) {
        loaddingDialog.showDialog();
        NetworkFactory networkFactory = OkhttpFactory.getInstance();
        SuccessfulCallback successfulCallback = new SuccessfulCallback() {
            @Override
            public void success(String str) throws JSONException {
                if (new CodeExceptionUtil(ForgetPsdActivity.this).dealException(str)) {
                    getSharedPreferences(Global.USER_ACCOUNT, MODE_PRIVATE).edit().putString(Global.USER_ACCOUNT, phone).commit();
                    getSharedPreferences(Global.USER_PASSWORD, MODE_PRIVATE).edit().putString(Global.USER_PASSWORD, "").commit();
                    Toast.makeText(ForgetPsdActivity.this, "找回密码成功", Toast.LENGTH_SHORT).show();
                    finish();
                }
                loaddingDialog.dismissAniDialog();
            }

            @Override
            public void success(InputStream ism, long conentLength) {

            }
        };
        FailCallback failCallback = new FailCallback() {
            @Override
            public void fail(String str) {
                loaddingDialog.dismissAniDialog();
                Toast.makeText(ForgetPsdActivity.this, "找回密码失败，请重试", Toast.LENGTH_SHORT).show();
            }
        };
        HashMap<String, String> param = new HashMap<>();
        param.put("strMobile", phone);
        try {
            param.put("strPasswd", URLEncoder.encode(URLEncoder.encode(newpsd,"utf-8")));
            param.put("strRePasswd", URLEncoder.encode(URLEncoder.encode(againpsd,"utf-8")));
            param.put("strModifyPasswdEnter", "retrieve");
            UrlManager urlManager = new UrlManager(this);
            networkFactory.start(NetworkFactory.METHOD_GET, urlManager.getModifyPsd(), param, successfulCallback, failCallback);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    private boolean checkPsd(String psd) {
        boolean flag = false;
        if (!IsNullOrEmpty.isEmpty(psd) && psd.length() >= 8 && !psd.contains(" ")) {
            flag = true;
        }
        return flag;
    }

    private boolean checkPhone(String phone) {
        boolean flag = false;
        if (!IsNullOrEmpty.isEmpty(phone) && phone.length() == 11) {
            flag = true;
        }
        return flag;
    }


}
