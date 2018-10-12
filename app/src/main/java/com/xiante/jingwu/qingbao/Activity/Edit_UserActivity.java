package com.xiante.jingwu.qingbao.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.xiante.jingwu.qingbao.Dialog.LoginOutDialog;
import com.xiante.jingwu.qingbao.R;

/**
 * Created by zhong on 2018/5/17.
 */

public class Edit_UserActivity extends  BaseActivity {

    View editPhoneV,editKeyV;
    TextView myPhoneTV;
    View loginoutTV;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_user_activity);
        initView();
        initData();
        initListener();
    }

    @Override
    public void initView() {
        initTitlebar("账号信息","","");
        editPhoneV=findViewById(R.id.editPhoneV);
        editKeyV=findViewById(R.id.editKeyV);
        myPhoneTV=findViewById(R.id.myPhoneTV);
        loginoutTV=findViewById(R.id.loginoutTV);
        titlebarLineView.setVisibility(View.GONE);
        String phone=getIntent().getStringExtra("phone");
        myPhoneTV.setText(phone);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        loginoutTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginOutDialog dialog=new LoginOutDialog(Edit_UserActivity.this);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        });

        editKeyV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Edit_UserActivity.this,ModifyPassworkActivity.class);
                startActivity(intent);
            }
        });

        editPhoneV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Edit_UserActivity.this,ChangePhone.class);
                startActivity(intent);
            }
        });
    }
}
