package com.xiante.jingwu.qingbao.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.IsNullOrEmpty;

/**
 * Created by zhong on 2018/5/18.
 */

public class ChangePhone extends BaseActivity {

    EditText phoneET;
    View commitBt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_phone_activity);
       initView();
       initData();
       initListener();
    }

    @Override
    public void initView() {
         initTitlebar("修改绑定手机","","");
         phoneET=findViewById(R.id.myphoneET);
         commitBt=findViewById(R.id.commitTV);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
         commitBt.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 String phone=phoneET.getText().toString().trim();
                 if(!checkPhone(phone)){
                     Toast.makeText(ChangePhone.this,"手机号码格式有误",Toast.LENGTH_SHORT).show();
                 return;
                 }
                 Intent intent=new Intent(ChangePhone.this,ChangePhoneVerifyActivity.class);
                 intent.putExtra("phone",phone);
                 startActivity(intent);
             //    sendVerifyCode(phone);
             }
         });

         phoneET.addTextChangedListener(new TextWatcher() {
             @Override
             public void beforeTextChanged(CharSequence s, int start, int count, int after) {

             }

             @Override
             public void onTextChanged(CharSequence s, int start, int before, int count) {

             }

             @Override
             public void afterTextChanged(Editable s) {
                   String phone=phoneET.getText().toString().trim();
                   if(phone.length()>11){
                       phoneET.setText(phone.substring(0,11));
                       phoneET.setSelection(11);
                   }
             }
         });

    }





    private boolean checkPhone(String phone){
        boolean flag=false;
        if(!IsNullOrEmpty.isEmpty(phone)&&phone.length()==11){
            flag=true;
        }
        return flag;
    }

}
