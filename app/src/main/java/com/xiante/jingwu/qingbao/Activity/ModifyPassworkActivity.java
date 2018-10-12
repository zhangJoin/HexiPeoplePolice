package com.xiante.jingwu.qingbao.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eallnetwork.framework.FailCallback;
import com.example.eallnetwork.framework.NetworkFactory;
import com.example.eallnetwork.framework.SuccessfulCallback;
import com.example.eallnetwork.workUtils.OkhttpFactory;
import com.xiante.jingwu.qingbao.Dialog.LoaddingDialog;
import com.xiante.jingwu.qingbao.NetWork.UrlManager;
import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.AESUtil;
import com.xiante.jingwu.qingbao.Util.CodeExceptionUtil;
import com.xiante.jingwu.qingbao.Util.Global;
import com.xiante.jingwu.qingbao.Util.IsNullOrEmpty;
import com.xiante.jingwu.qingbao.Util.Utils;

import org.json.JSONException;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by zhong on 2018/5/17.
 */

public class ModifyPassworkActivity extends  BaseActivity {
     EditText currentPsdET,newPsdET,againPsdET;
     View commitBT;
     LoaddingDialog loaddingDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_psd_activity);
        initView();
        initData();
        initListener();
    }

    @Override
    public void initView() {
        currentPsdET=findViewById(R.id.currentPsdET);
        newPsdET=findViewById(R.id.newPsdET);
        againPsdET=findViewById(R.id.againPsdET);
        commitBT=findViewById(R.id.commitTV);
        loaddingDialog=new LoaddingDialog(this);
        initTitlebar("修改密码","","");
//        Utils.setEditTextInhibitInputSpace(newPsdET);
//        Utils.setEditTextInhibitInputSpace(againPsdET);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

        commitBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String oldmsg=currentPsdET.getText().toString();
               String newpsd=newPsdET.getText().toString();
               String againpsd=againPsdET.getText().toString();
               if(!newpsd.equals(againpsd)){
                   Toast.makeText(ModifyPassworkActivity.this,"新密码不一致",Toast.LENGTH_SHORT).show();
                   return;
               }
               if(!checkPsd(newpsd)){
                   Toast.makeText(ModifyPassworkActivity.this,"密码格式不正确",Toast.LENGTH_SHORT).show();
                   return;
               }
               modifyPsd(oldmsg,newpsd,againpsd);
            }
        });
        setBtnBackgroudListen(currentPsdET);
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
        String currentPwd = currentPsdET.getText().toString();
        String newPwd = newPsdET.getText().toString();
        String againPwd = againPsdET.getText().toString();
        if (!TextUtils.isEmpty(currentPwd) && !TextUtils.isEmpty(newPwd)&& !TextUtils.isEmpty(againPwd)) {
            commitBT.setEnabled(true);
            commitBT.setBackgroundResource(R.drawable.send_power);
        } else {
            commitBT.setEnabled(false);
            commitBT.setBackgroundResource(R.drawable.send_gray1);
        }
    }

    private void modifyPsd(String oldmsg, String newpsd, String againpsd) {
        boolean isSuccess= Utils.isSuccess(ModifyPassworkActivity.this);
        if(!isSuccess){
            Toast.makeText(ModifyPassworkActivity.this,getString(R.string.netError),Toast.LENGTH_SHORT).show();
            return;
        }
        loaddingDialog.showDialog();
        NetworkFactory networkFactory= OkhttpFactory.getInstance();
        SuccessfulCallback successfulCallback=new SuccessfulCallback() {
            @Override
            public void success(String str) throws JSONException {
                      if(new CodeExceptionUtil(ModifyPassworkActivity.this).dealException(str)){
                          Toast.makeText(ModifyPassworkActivity.this,"修改密码成功",Toast.LENGTH_SHORT).show();
//                          getSharedPreferences(Global.USER_ACCOUNT,MODE_PRIVATE).edit().putString(Global.USER_ACCOUNT,"").commit();
                          getSharedPreferences(Global.USER_PASSWORD,MODE_PRIVATE).edit().putString(Global.USER_PASSWORD,"").commit();
                          Intent intent=new Intent(ModifyPassworkActivity.this,LoginActivity.class);
                          startActivity(intent);
                      }
                      loaddingDialog.dismissAniDialog();
            }

            @Override
            public void success(InputStream ism, long conentLength) {

            }
        };
        FailCallback failCallback=new FailCallback() {
            @Override
            public void fail(String str) {
                loaddingDialog.dismissAniDialog();
                Toast.makeText(ModifyPassworkActivity.this,"修改密码失败，请重试",Toast.LENGTH_SHORT).show();
            }
        };
        HashMap<String,String> param=new HashMap<>();
        param.put("strOldPasswd", AESUtil.encodeByMD5(oldmsg));
        try {
            param.put("strPasswd", URLEncoder.encode(URLEncoder.encode(newpsd,"utf-8")));
            param.put("strRePasswd",URLEncoder.encode(URLEncoder.encode(againpsd,"utf-8")));
            param.put("strModifyPasswdEnter","modify");
            UrlManager urlManager=new UrlManager(this);
            networkFactory.start(NetworkFactory.METHOD_GET,urlManager.getModifyPsd(),param,successfulCallback,failCallback);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    private boolean checkPsd(String psd){
        boolean flag=false;
        if(!IsNullOrEmpty.isEmpty(psd)&&psd.length()>=8&&!psd.contains(" ")){
        flag=true;
        }
       return  flag;
    }

}
