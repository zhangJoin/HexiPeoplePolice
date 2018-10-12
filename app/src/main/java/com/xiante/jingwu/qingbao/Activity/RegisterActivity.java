package com.xiante.jingwu.qingbao.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.xiante.jingwu.qingbao.Util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by zhong on 2018/5/15.
 */


public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private TextView mFinishTV;
    private ImageView mImageRegister;
    private TextView mTvGoLogin;
    private EditText mEtPhone;
    private EditText mEtCheck;
    private TextView mTvGetCode;
    private EditText mEtPassword;
    private View psdControloneView;
    private View psdControltwoView;
    private EditText mEtConformPassword;
    private Button mBtCommit;
    boolean showpsd1 = false;
    boolean showpsd2 = false;
    private LoaddingDialog loaddingDialog;
    private Timer timer;
    private int leftTime = 0;
    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registerlayout);
        initView();
        initData();
        initListener();
    }

    /**
     * 初始化控件
     */
    @Override
    public void initView() {
        mFinishTV = findViewById(R.id.finishTV);
        mImageRegister = findViewById(R.id.iv_register);
        mTvGoLogin = findViewById(R.id.tv_goLogin);
        mEtPhone = findViewById(R.id.et_phone);
        mEtCheck = findViewById(R.id.et_check);
        mTvGetCode = findViewById(R.id.tv_getCode);
        mEtPassword = findViewById(R.id.et_password);
        psdControloneView = findViewById(R.id.psdControloneView);
        mEtConformPassword = findViewById(R.id.et_conform_password);
        mBtCommit = findViewById(R.id.commitSureBT);
        psdControltwoView = findViewById(R.id.psdControltwoView);
        loaddingDialog = new LoaddingDialog(this);
    }

    @Override
    public void initData() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (leftTime <= 0) {
                    mTvGetCode.setText("获取验证码");
                } else {
                    mTvGetCode.setText(leftTime + "秒后重新发送");
                }
            }
        };

    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void initListener() {
        mFinishTV.setOnClickListener(this);
        mTvGoLogin.setOnClickListener(this);
        psdControloneView.setOnClickListener(this);
        psdControltwoView.setOnClickListener(this);
        mEtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                phoneStr = mEtPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phoneStr)) {
                    mTvGetCode.setEnabled(false);
                    mTvGetCode.setBackgroundResource(R.drawable.send_gray);
                    mBtCommit.setEnabled(false);
                    mBtCommit.setBackgroundResource(R.drawable.send_gray);
                } else {
                    mTvGetCode.setEnabled(true);
                    mBtCommit.setEnabled(true);
                    mTvGetCode.setBackgroundResource(R.drawable.send_power);
                    mTvGetCode.setOnClickListener(RegisterActivity.this);
                }
            }
        });
        setBtnBackgroudListen(mEtConformPassword);
        setBtnBackgroudListen(mEtPassword);
        setBtnBackgroudListen(mEtCheck);
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
                inputPwdStr = mEtPassword.getText().toString().trim();
                inputCwdStr = mEtConformPassword.getText().toString().trim();
                codeStr = mEtCheck.getText().toString().trim();
                phoneStr = mEtPhone.getText().toString().trim();
                if (!TextUtils.isEmpty(inputPwdStr) && !TextUtils.isEmpty(inputCwdStr) && !TextUtils.isEmpty(codeStr) && !TextUtils.isEmpty(phoneStr)) {
                    mBtCommit.setBackgroundResource(R.drawable.send_power);
                    mBtCommit.setOnClickListener(RegisterActivity.this);
                } else {
                    mBtCommit.setBackgroundResource(R.drawable.send_gray);
                }
            }
        });
    }

    //inputPwdStr输入密码 inputCwdStr确认密码  codeStr 验证码
    String phoneStr, inputPwdStr, inputCwdStr, codeStr;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.finishTV:
                finish();
                break;
            case R.id.tv_goLogin:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.tv_getCode:
                if (leftTime > 0) {
                    return;
                }
                getCode();
                break;
            case R.id.psdControloneView:
                if (showpsd1) {
                    psdControloneView.setBackgroundResource(R.drawable.hide_psd);
                    mEtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mEtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    psdControloneView.setBackgroundResource(R.drawable.show_psd);
                    mEtPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                    mEtPassword.setTransformationMethod(null);
                }
                showpsd1 = !showpsd1;
                break;
            case R.id.psdControltwoView:
                if (showpsd2) {
                    psdControltwoView.setBackgroundResource(R.drawable.hide_psd);
                    mEtConformPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mEtConformPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    psdControltwoView.setBackgroundResource(R.drawable.show_psd);
                    mEtConformPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                    mEtConformPassword.setTransformationMethod(null);
                }
                showpsd2 = !showpsd2;
                break;
            case R.id.commitSureBT:
                checkEmpty();
                break;
        }

    }

    private void checkEmpty() {
        inputPwdStr = mEtPassword.getText().toString().trim();
        inputCwdStr = mEtConformPassword.getText().toString().trim();
        codeStr = mEtCheck.getText().toString().trim();
        if (TextUtils.isEmpty(codeStr)) {
            Toast.makeText(this, getString(R.string.getCode), Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(inputPwdStr)) {
            Toast.makeText(this, getString(R.string.inputPassword), Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(inputCwdStr)) {
            Toast.makeText(this, getString(R.string.inputComformPassword), Toast.LENGTH_SHORT).show();
            return;
        } else if (inputPwdStr.length() < 7) {
            Toast.makeText(this, getString(R.string.inputPasswordLength), Toast.LENGTH_SHORT).show();
            return;
        } else if (!inputPwdStr.equals(inputCwdStr)) {
            Toast.makeText(this, getString(R.string.noEqual), Toast.LENGTH_SHORT).show();
            return;
        }
        //验证验证码是否正确
//        SMSSDK.submitVerificationCode("86",phoneStr,codeStr);
        loaddingDialog.showDialog();
        registerUser();
    }

    public String getCode() {
        boolean chinaPhoneLegal = Utils.isChinaPhoneLegal(phoneStr);
        if (chinaPhoneLegal) {
            checkPhoneExist();
        } else {
            Toast.makeText(RegisterActivity.this, getString(R.string.correntPhone), Toast.LENGTH_SHORT).show();
        }


        return null;
    }
    private void checkPhoneExist() {
        boolean isSuccess = Utils.isSuccess(RegisterActivity.this);
        if (!isSuccess) {
            Toast.makeText(RegisterActivity.this, getString(R.string.netError), Toast.LENGTH_SHORT).show();
            return;
        }
        NetworkFactory mNetworkFactory = OkhttpFactory.getInstance();
        SuccessfulCallback mSuccessfulCallback = new SuccessfulCallback() {
            @Override
            public void success(String str) throws JSONException {
                if (new CodeExceptionUtil(RegisterActivity.this).dealException(str)) {
                    JSONObject jsonObject = new JSONObject(str);
                    String resultData = jsonObject.optString("resultData");
                    if(!TextUtils.isEmpty(resultData)){
                        Toast.makeText(RegisterActivity.this,"该用户已存在",Toast.LENGTH_SHORT).show();
                        return;
                    }else{
                        mTvGetCode.setEnabled(true);
//                        SMSSDK.getVerificationCode("86", phoneStr);
//                        mTvGetCode.setEnabled(false);
//                        startCountLeftTime();
                    }
                }
                loaddingDialog.dismissAniDialog();
            }

            @Override
            public void success(InputStream ism, long conentLength) {

            }
        };
        FailCallback mFailCallback = new FailCallback() {
            @Override
            public void fail(String str) {
                Log.i("urlfail", str);
            }
        };
        HashMap<String, String> param = new HashMap<>();
        param.put("strMobileNoId", phoneStr);
        UrlManager urlManager = new UrlManager(this);
        mNetworkFactory.start(NetworkFactory.METHOD_GET, urlManager.getCheckExistUrl(), param, mSuccessfulCallback
                , mFailCallback);

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

    //项服务器提交
    private void registerUser() {
        boolean isSuccess = Utils.isSuccess(RegisterActivity.this);
        if (!isSuccess) {
            Toast.makeText(RegisterActivity.this, getString(R.string.netError), Toast.LENGTH_SHORT).show();
            return;
        }
        NetworkFactory mNetworkFactory = OkhttpFactory.getInstance();
        SuccessfulCallback mSuccessfulCallback = new SuccessfulCallback() {
            @Override
            public void success(String str) throws JSONException {
                if (new CodeExceptionUtil(RegisterActivity.this).dealException(str)) {
                    JSONObject jsonObject = new JSONObject(str);
                    JSONObject resultData = jsonObject.optJSONObject("resultData");
                    String token = resultData.optJSONObject("person").optString("strGuid");
                    getSharedPreferences(Global.SHARE_TOKEN, MODE_PRIVATE).edit().putString(Global.SHARE_TOKEN, token).apply();
                    Intent intent = new Intent(RegisterActivity.this, RegisterPerfectActivity.class);
                    intent.putExtra("user", str);
                    startActivity(intent);
                }
                loaddingDialog.dismissAniDialog();

            }

            @Override
            public void success(InputStream ism, long conentLength) {

            }
        };
        FailCallback mFailCallback = new FailCallback() {
            @Override
            public void fail(String str) {
                Log.i("urlfail", str);
            }
        };
        HashMap<String, String> param = new HashMap<>();
        param.put("strMobile", phoneStr);
        param.put("strPasswd", inputPwdStr);
        param.put("strRePasswd", inputCwdStr);
        UrlManager urlManager = new UrlManager(this);
        mNetworkFactory.start(NetworkFactory.METHOD_GET, urlManager.getRegisterUrl(), param, mSuccessfulCallback
                , mFailCallback);
    }

}
