package com.xiante.jingwu.qingbao.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.example.eallnetwork.framework.FailCallback;
import com.example.eallnetwork.framework.NetworkFactory;
import com.example.eallnetwork.framework.SuccessfulCallback;
import com.example.eallnetwork.workUtils.OkhttpFactory;
import com.xiante.jingwu.qingbao.Bean.Common.ClickEntity;
import com.xiante.jingwu.qingbao.Bean.Common.UserEntity;
import com.xiante.jingwu.qingbao.CustomView.CommonView.CircularImageView;
import com.xiante.jingwu.qingbao.Dialog.LoaddingDialog;
import com.xiante.jingwu.qingbao.NetWork.UrlManager;
import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.CodeExceptionUtil;
import com.xiante.jingwu.qingbao.Util.Global;
import com.xiante.jingwu.qingbao.Util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

/**
 * Created by zhong on 2018/4/28.
 *
 */

public class MainTab_MeActivity extends BaseActivity {
    TextView userNameTV, powerCountTV;
    CircularImageView meAvatarIV;
    LinearLayout showUserinforLL, showUserScoreLL, showUserReportLL, showUserTaskLL,
            showUserSecurityLL, showUserContactLL, showUserPowerLL, showUserModifyLL,ArtiNaviLL;
    UserEntity userEntity;
    LoaddingDialog loaddingDialog;
    boolean isSuccess;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maintab_me);
        initView();
        initData();
        initListener();
        getNetData();
    }

    @Override
    public void initView() {
        userNameTV = findViewById(R.id.userNameTV);
        powerCountTV = findViewById(R.id.powerCountTV);
        meAvatarIV = findViewById(R.id.meAvatarIV);
        showUserinforLL = findViewById(R.id.showUserinforLL);
        showUserScoreLL = findViewById(R.id.showUserScoreLL);
        showUserReportLL = findViewById(R.id.showUserReportLL);
        showUserTaskLL = findViewById(R.id.showUserTaskLL);
        showUserSecurityLL = findViewById(R.id.showUserSecurityLL);
        showUserContactLL = findViewById(R.id.showUserContactLL);
        showUserPowerLL = findViewById(R.id.showMyPowerLL);
        showUserModifyLL = findViewById(R.id.showUserModifyLL);
        loaddingDialog = new LoaddingDialog(this);
        loaddingDialog.setCancelable(false);
        ArtiNaviLL=findViewById(R.id.ArtiNaviLL);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
//        showUserinforLL.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(userEntity==null){
//                    return;
//                }
//                Intent intent=new Intent(MainTab_MeActivity.this,UserInforActivity.class);
//                intent.putExtra("user",userEntity);
//                startActivity(intent);
//            }
//        });

        showUserScoreLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainTab_MeActivity.this, InformationOfficersActivity.class);
                startActivity(intent);
            }
        });

        showUserReportLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainTab_MeActivity.this, MyListActivity.class);
                ClickEntity clickEntity = new ClickEntity();
                clickEntity.setStrTitle("审核记录");
                String user = getSharedPreferences(Global.USER_ACCOUNT, MODE_PRIVATE).getString(Global.USER_ACCOUNT, "");
                String veryUrl = new StringBuilder(Global.Verify_Record).append("?strAccount=").append(user).toString();
                clickEntity.setStrUrl(veryUrl);
                intent.putExtra(Global.CLICK_ACTION, clickEntity);
                startActivity(intent);
            }
        });

        showUserTaskLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainTab_MeActivity.this, MyListActivity.class);
                ClickEntity clickEntity = new ClickEntity();
                clickEntity.setStrTitle("我的任务");
                String accout = getSharedPreferences(Global.USER_ACCOUNT, MODE_PRIVATE).getString(Global.USER_ACCOUNT, "");
                String taskurl = new StringBuilder(Global.MY_TASK)
                        .append("?isXxy=2&strTypeCode=task&strAccount=").append(accout).toString();
                clickEntity.setStrUrl(taskurl);
                intent.putExtra(Global.CLICK_ACTION, clickEntity);
                startActivity(intent);
            }
        });
        showUserContactLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainTab_MeActivity.this, ContactActivity.class);
                startActivity(intent);
            }
        });
        ArtiNaviLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.gonavi(MainTab_MeActivity.this);
            }
        });
        showUserPowerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userEntity!=null&&!userEntity.getStrUnityGuid().equals("")) {
                    Intent intent = new Intent(MainTab_MeActivity.this, PowerSourceActivity.class);
                    intent.putExtra("strUnityGuid",userEntity.getStrUnityGuid());
                    startActivity(intent);
                } else {
                    Toast.makeText(MainTab_MeActivity.this, "您还未加入社区关怀", Toast.LENGTH_SHORT).show();
                }

            }
        });
        showUserSecurityLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("", "");
                Intent intent = new Intent(MainTab_MeActivity.this, BodySecurityActivity.class);
                startActivity(intent);
            }
        });
        showUserModifyLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userEntity==null){
                    return;
                }
                Intent intent = new Intent(MainTab_MeActivity.this, Edit_UserActivity.class);
                intent.putExtra("phone", userEntity.getStrMobile());
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        getNetData();
    }

    private void getNetData() {
        isSuccess = Utils.isSuccess(MainTab_MeActivity.this);
        if (!isSuccess) {
            Toast.makeText(MainTab_MeActivity.this, getString(R.string.netError), Toast.LENGTH_SHORT).show();
            return;
        }
        loaddingDialog.showDialog();
        NetworkFactory networkFactory = OkhttpFactory.getInstance();
        SuccessfulCallback successfulCallback = new SuccessfulCallback() {
            @Override
            public void success(String str) throws JSONException {
                loaddingDialog.dismissAniDialog();
                if (new CodeExceptionUtil(MainTab_MeActivity.this).dealException(str)) {
                    userEntity = JSON.parseObject(new JSONObject(str).optString("resultData"), UserEntity.class);
                    userNameTV.setText(userEntity.getStrName());
                    Glide.with(MainTab_MeActivity.this)
                            .load(userEntity.getStrPortrait())
                            .error(R.drawable.default_pic)
                            .placeholder(R.drawable.default_pic)
                            .into(meAvatarIV);
                    getSharedPreferences(Global.ME_AVATAR, MODE_PRIVATE).edit().putString(Global.ME_AVATAR, userEntity.getStrPortrait()).commit();
                    powerCountTV.setText(userEntity.getXxyNum());
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

        UrlManager urlManager = new UrlManager(this);
        networkFactory.start(NetworkFactory.METHOD_GET, urlManager.getUserInformation(), null, successfulCallback, failCallback);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        return false;
    }
}
