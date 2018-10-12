package com.xiante.jingwu.qingbao.CustomView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.idcard.TRECAPIImpl;
import com.idcard.TStatus;
import com.idcard.TengineID;
import com.turui.bank.ocr.CaptureActivity;
import com.xiante.jingwu.qingbao.Activity.InputActivity;
import com.xiante.jingwu.qingbao.Bean.Input.InputItemBean;
import com.xiante.jingwu.qingbao.Bean.Input.UploadBean;
import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.Global;
import com.xiante.jingwu.qingbao.Util.IsNullOrEmpty;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by zhong on 2018/6/5.
 */

public class HouseOwnerView extends LinearLayout implements InputView {

    private EditText addressET,nameET,idNumberET,phoneET;
    private TextView titleTV;
    private InputItemBean inputItemBean;
    private TRECAPIImpl engineDemo;
    public HouseOwnerView(Context context) {
        super(context);
       init(context);
    }

    public HouseOwnerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
       init(context);
    }

    public HouseOwnerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context){
        View.inflate(context, R.layout.house_owner_view,this);
        titleTV=findViewById(R.id.titleTV);
        addressET=findViewById(R.id.addressET);
        idNumberET=findViewById(R.id.idNumberET);
        nameET=findViewById(R.id.nameET);
        phoneET=findViewById(R.id.telphoneET);
        engineDemo = new TRECAPIImpl();
        TStatus tStatus = engineDemo.TR_StartUP(context,engineDemo.TR_GetEngineTimeKey());
        if (tStatus == TStatus.TR_TIME_OUT ) {
            Toast.makeText(context, "引擎过期", Toast.LENGTH_SHORT).show();
        }
        else  if (tStatus == TStatus.TR_FAIL) {
            Toast.makeText(context, "引擎初始化失败", Toast.LENGTH_SHORT).show();
        }

        phoneET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text=   phoneET.getText().toString();
                if(phoneET.getText().toString().length()>11){
                       phoneET.setText(text.substring(0,11));
                   }
            }
        });
        findViewById(R.id.goScanIDcardV).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AndPermission.with(getContext())
                        .permission(Permission.CAMERA)
                        .onGranted(new Action() {
                            @Override
                            public void onAction(List<String> permissions) {
                                InputActivity.selectkey=inputItemBean.getStrField();
                                InputActivity.selectid="3432";
                                CaptureActivity.tengineID = TengineID.TIDCARD2;
                                CaptureActivity.ShowCopyRightTxt = "Esint";
                                Intent intent = new Intent(context, CaptureActivity.class);
                                intent.putExtra("engine", engineDemo);
                                ((Activity)context).startActivityForResult(intent, 0);
                            }
                        })
                        .onDenied(new Action() {
                            @Override
                            public void onAction(List<String> permissions) {
                                Toast.makeText(getContext(),"请开启相机权限",Toast.LENGTH_SHORT).show();
                            }
                        }).start();

            }
        });


        addressET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
               String text=addressET.getText().toString();
               if(text.length()>100){
                   addressET.setText(text.substring(0,100));
               }
            }
        });
    }

    @Override
    public UploadBean getUploadValue() {
        JSONObject jsonObject=new JSONObject();
        String value="";
        try {
            jsonObject.put("strHouseAddr",addressET.getText().toString());
            jsonObject.put("strOwner",nameET.getText().toString());
            jsonObject.put("strIdcard",idNumberET.getText().toString());
            jsonObject.put("strOwnerTel",phoneET.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        value=jsonObject.toString();
        try {
            value= URLEncoder.encode(value,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new UploadBean(Global.HOUSE_OWNER,value);
    }

    @Override
    public boolean checkUploadValue() {
        if(inputItemBean.getIsMust().equals("1")){
            if(IsNullOrEmpty.isEmpty(addressET.getText().toString())){
                Toast.makeText(getContext(),"房主地址不能为空",Toast.LENGTH_SHORT).show();
                return false;
            }
            if(IsNullOrEmpty.isEmpty(nameET.getText().toString())){
                Toast.makeText(getContext(),"房主姓名不能为空",Toast.LENGTH_SHORT).show();
                return false;
            }
            if(IsNullOrEmpty.isEmpty(idNumberET.getText().toString())){
                Toast.makeText(getContext(),"房主身份证不能为空",Toast.LENGTH_SHORT).show();
                return false;
            }
            if(IsNullOrEmpty.isEmpty(phoneET.getText().toString())){
                Toast.makeText(getContext(),"房主电话不能为空",Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
        }else {
            return true;
        }
    }

    @Override
    public void initInputView(InputItemBean inputItemBean) {
        this.inputItemBean=inputItemBean;
        titleTV.setText(inputItemBean.getStrFieldName());
    }

    @Override
    public void updateInputView(String string) {
        try {
            JSONObject jsonObject=new JSONObject(string);
            String value=jsonObject.optString("value");
            if(!IsNullOrEmpty.isEmpty(value)){
                String[] partinfor=value.split("\n");
                String namepart=partinfor[0];
                String numberPart=partinfor[5];
                nameET.setText(namepart.split(":")[1]);
                idNumberET.setText(numberPart.split(":")[1]);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
