package com.xiante.jingwu.qingbao.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xiante.jingwu.qingbao.CustomView.RenterPersonView;
import com.xiante.jingwu.qingbao.MessageEvent.UpdateViewMessage;
import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.Global;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhong on 2018/4/26.
 */

public class ScanIDcardActivity extends BaseActivity {

   private EditText idcardET;
   private Button commitBT;
   private int viewid;
   private String inputkey;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_idcard);
        initView();
        initData();
        initListener();
    }

    @Override
    public void initView() {
       idcardET=findViewById(R.id.idcardinputET);
       commitBT=findViewById(R.id.commitBT);
    }

    @Override
    public void initData() {
         viewid=getIntent().getIntExtra(RenterPersonView.VIEW_ID,0);
         inputkey=getIntent().getStringExtra(Global.INPUTKEY);
    }

    @Override
    public void initListener() {
          commitBT.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  JSONObject jsonObject=new JSONObject();
                  try {
                      jsonObject.put("id",viewid);
                      jsonObject.put("value",idcardET.getText().toString().trim());
                  } catch (JSONException e) {
                      e.printStackTrace();
                  }
                  EventBus.getDefault().post(new UpdateViewMessage(inputkey,jsonObject.toString()));
                  finish();
              }
          });
    }
}
