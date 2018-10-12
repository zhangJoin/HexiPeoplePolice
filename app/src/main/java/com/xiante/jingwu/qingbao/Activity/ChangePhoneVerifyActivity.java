package com.xiante.jingwu.qingbao.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
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
import com.xiante.jingwu.qingbao.Util.Utils;

import org.json.JSONException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zhong on 2018/5/18.
 */

public class ChangePhoneVerifyActivity extends BaseActivity {
    String phone;
    List<String> codes;
    EditText codeET;
    TextView codeOneTV, codeTwoTV, codeThreeTV, codeFourTV, currentPhoneTV;
    View lineoneV, lineTwoV, lineThreeV, lineFourV;
    TextView obtionVerifyCodeTV;
    View commitTV;
    List<View> viewList;
    Timer timer;
    int lefttime = 0;
    LoaddingDialog loaddingDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_verify_activity);
        initView();
        initData();
        initListener();


    }

    @Override
    public void initView() {
        initTitlebar("更换手机号", "", "");
        viewList = new ArrayList<>();
        currentPhoneTV = findViewById(R.id.currentPhoneTV);
        codeET = findViewById(R.id.codeET);
        codeOneTV = findViewById(R.id.firstCodeTV);
        codeTwoTV = findViewById(R.id.secondCodeTV);
        codeThreeTV = findViewById(R.id.thirdCodeTV);
        codeFourTV = findViewById(R.id.fourCoedtv);
        lineoneV = findViewById(R.id.lineOneV);
        lineTwoV = findViewById(R.id.lineTwoV);
        lineThreeV = findViewById(R.id.lineThreeV);
        lineFourV = findViewById(R.id.lineFourV);
        obtionVerifyCodeTV = findViewById(R.id.obtionVerifyCodeTV);
        commitTV = findViewById(R.id.commitTV);
        viewList.add(lineoneV);
        viewList.add(lineTwoV);
        viewList.add(lineThreeV);
        viewList.add(lineFourV);
        loaddingDialog=new LoaddingDialog(this);
    }

    @Override
    public void initData() {
        lefttime = 60;
        phone = getIntent().getStringExtra("phone");
        codes = new ArrayList<>();
        currentPhoneTV.setText(phone);
        sendVerifyCode(phone);
        startCount();
    }

    @Override
    public void initListener() {

        codeET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable != null && editable.length() > 0) {
                    codeET.setText("");
                    if (codes.size() < 4) {
                        codes.add(editable.toString());
                        showcode();
                    }
                }
            }


        });
        // 监听验证码删除按键

        codeET.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN && codes.size() > 0) {
                    codes.remove(codes.size() - 1);
                    showcode();
                    return true;
                }
                return false;
            }
        });

        obtionVerifyCodeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lefttime <= 0) {
                    lefttime = 60;
                    sendVerifyCode(phone);
                }
            }
        });

        commitTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSuccess = Utils.isSuccess(ChangePhoneVerifyActivity.this);
                if (!isSuccess){
                    Toast.makeText(ChangePhoneVerifyActivity.this,getString(R.string.netError),Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(codeET.getText().toString().trim())){
                    Toast.makeText(ChangePhoneVerifyActivity.this,"请输入验证码",Toast.LENGTH_SHORT).show();
                    return;
                }
                changePhone();
            }
        });

    }

    private void changePhone() {
        loaddingDialog.showDialog();
        NetworkFactory networkFactory = OkhttpFactory.getInstance();
        SuccessfulCallback successfulCallback = new SuccessfulCallback() {
            @Override
            public void success(String str) throws JSONException {
                loaddingDialog.dismissAniDialog();
                CodeExceptionUtil codeExceptionUtil = new CodeExceptionUtil(ChangePhoneVerifyActivity.this);
                if (codeExceptionUtil.dealException(str)) {
                   startActivity(new Intent(ChangePhoneVerifyActivity.this,Edit_UserActivity.class));
                }
            }

            @Override
            public void success(InputStream ism, long conentLength) {

            }
        };
        FailCallback failCallback = new FailCallback() {
            @Override
            public void fail(String str) {
                Log.i("urlfail", str);
            }
        };
        HashMap<String, String> param = new HashMap<>();
        param.put("strMobile", phone);
        UrlManager urlManager = new UrlManager(this);
        networkFactory.start(NetworkFactory.METHOD_GET, urlManager.getModifyPhoneUrl(), param, successfulCallback, failCallback);
    }

    private void startCount() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                lefttime--;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (lefttime > 0) {
                            obtionVerifyCodeTV.setText(lefttime + "秒后重新发送");
                        } else {
                            obtionVerifyCodeTV.setText("发送验证码");
                        }

                    }
                });
            }
        }, 1000, 1000);
    }


    private void sendVerifyCode(String phone) {

    }


    /**
     * 显示输入的验证码
     */
    private void showcode() {
        String code1 = "";
        String code2 = "";
        String code3 = "";
        String code4 = "";
        if (codes.size() >= 1) {
            code1 = codes.get(0);
        }
        if (codes.size() >= 2) {
            code2 = codes.get(1);
        }
        if (codes.size() >= 3) {
            code3 = codes.get(2);
        }
        if (codes.size() >= 4) {
            code4 = codes.get(3);
        }

        for (int i = 0; i < codes.size(); i++) {
            viewList.get(i).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
        for (int i = 3; i >= codes.size(); i--) {
            viewList.get(i).setBackgroundColor(Color.parseColor("#999999"));
        }

        codeOneTV.setText(code1);
        codeTwoTV.setText(code2);
        codeThreeTV.setText(code3);
        codeFourTV.setText(code4);

//        setcolor();//设置高亮颜色
//        callback();//回调
    }
}
