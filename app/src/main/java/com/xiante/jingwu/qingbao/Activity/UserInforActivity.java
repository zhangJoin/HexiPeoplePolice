package com.xiante.jingwu.qingbao.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.xiante.jingwu.qingbao.Bean.Common.UserEntity;
import com.xiante.jingwu.qingbao.R;

/**
 * Created by zhong on 2018/5/9.
 */

public class UserInforActivity extends  BaseActivity {

    UserEntity userEntity;
    TextView user_nameTV,user_sexTV,user_nationTV,user_birthTV,user_idcardTV,
            user_addressTV,user_phoneTV,user_policeTV,user_createTimeTV;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfor);
        initView();
        initData();
        initListener();
    }

    @Override
    public void initView() {
        findViewById(R.id.titlebarLineView).setVisibility(View.INVISIBLE);
        initTitlebar("个人信息","","");
        user_nameTV=findViewById(R.id.user_nameTV);
        user_sexTV=findViewById(R.id.user_sexTV);
        user_nationTV=findViewById(R.id.user_nationTV);
        user_birthTV=findViewById(R.id.user_birthTV);
        user_idcardTV=findViewById(R.id.user_idcardTV);
        user_addressTV=findViewById(R.id.user_addressTV);
        user_phoneTV=findViewById(R.id.user_phoneTV);
        user_policeTV=findViewById(R.id.user_policeTV);
        user_createTimeTV=findViewById(R.id.user_createTimeTV);
    }

    @Override
    public void initData() {
      userEntity= (UserEntity) getIntent().getSerializableExtra("user");
        if(userEntity==null){
            return;
        }
        user_nameTV.setText(userEntity.getStrName());
        user_sexTV.setText(userEntity.getStrSex());
        user_nationTV.setText(userEntity.getStrNation());
        user_birthTV.setText(userEntity.getStrBirthDate());
        user_idcardTV.setText(userEntity.getStrCardNo());
        user_addressTV.setText(userEntity.getStrAddress());
        user_phoneTV.setText(userEntity.getStrMobile());
        user_policeTV.setText(userEntity.getStrPolice());
        user_createTimeTV.setText(userEntity.getStrCreateTime());
    }

    @Override
    public void initListener() {

    }
}
