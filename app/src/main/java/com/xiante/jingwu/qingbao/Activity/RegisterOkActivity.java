package com.xiante.jingwu.qingbao.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xiante.jingwu.qingbao.R;

/**
 * 注册完成页面
 */
public class RegisterOkActivity extends BaseActivity {
    private TextView tvRegionShow, tvAddressShow, tvPhoneShow;
    private Button btRegisterOK;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registeroklayout);
        initView();
        initData();
        initListener();
    }

    @Override
    public void initView() {
        tvAddressShow = findViewById(R.id.tv_addressShow);
        tvPhoneShow = findViewById(R.id.tv_phoneShow);
        tvRegionShow = findViewById(R.id.tv_regionShow);
        btRegisterOK = findViewById(R.id.bt_registerOK);
        this.initTitlebar(getString(R.string.okTitle), null, "");
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void initData() {
        Intent intentExtra = getIntent();
        Bundle bundle = intentExtra.getExtras();
        if (bundle != null) {
            String policeAddress = intentExtra.getStringExtra("strRegionAddress");
            String policePhone = intentExtra.getStringExtra("strPhone");
            String houseAddress = intentExtra.getStringExtra("policeAddress");
            if(!TextUtils.isEmpty(policeAddress)){
                tvRegionShow.setText(policeAddress + "派出所");
            }
            if(!TextUtils.isEmpty(houseAddress)){
                tvAddressShow.setText("地址:" + houseAddress);
            }
            if(!TextUtils.isEmpty(policePhone)){
                tvPhoneShow.setText("联系电话:" + policePhone);
            }
        }
    }

    @Override
    public void initListener() {
        btRegisterOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterOkActivity.this, MainActivity.class));
            }
        });
    }
}
