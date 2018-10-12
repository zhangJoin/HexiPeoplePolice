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
import android.widget.Toast;

import com.idcard.TRECAPIImpl;
import com.idcard.TStatus;
import com.idcard.TengineID;
import com.turui.bank.ocr.CaptureActivity;
import com.xiante.jingwu.qingbao.Activity.InputActivity;
import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.IsNullOrEmpty;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by zhong on 2018/4/26.
 */

public class RenterPersonView extends LinearLayout {

    private EditText nameET,idcardET,telET;
    private View scanView;
    private View deleteCustomerV;
    private String inputkey="";
    private Context context;
    public static final String VIEW_ID="VIEW_ID";
    public static final String renter_name="strLessee",renter_idnum="strLesseeIdcard",renter_tel="strLesseeTel";
    private TRECAPIImpl engineDemo;
    public RenterPersonView(Context context) {
        super(context);
       init(context);
    }

    public RenterPersonView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
       init(context);
    }

    public RenterPersonView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        this.context=context;
        View.inflate(context, R.layout.rent_personinfor,this);
        nameET=findViewById(R.id.inputValueNameET);
        idcardET=findViewById(R.id.inputValueIDcardET);
        telET=findViewById(R.id.inputValueTelET);
        scanView=findViewById(R.id.goScanIDcardV);
        deleteCustomerV=findViewById(R.id.deleteCustomerV);
        engineDemo = new TRECAPIImpl();
        TStatus tStatus = engineDemo.TR_StartUP(context,engineDemo.TR_GetEngineTimeKey());
        if (tStatus == TStatus.TR_TIME_OUT ) {
            Toast.makeText(context, "引擎过期", Toast.LENGTH_SHORT).show();
        }
        else  if (tStatus == TStatus.TR_FAIL) {
            Toast.makeText(context, "引擎初始化失败", Toast.LENGTH_SHORT).show();
        }
        initListener();
    }

    public void setInputkey(String inputkey){
        this.inputkey=inputkey;
    }


    public boolean checkValue(){
        boolean flag=true;
        if(IsNullOrEmpty.isEmpty(nameET.getText().toString())){
            flag=false;
        }
        if(IsNullOrEmpty.isEmpty(idcardET.getText().toString())){
            flag=false;
        }
        if(IsNullOrEmpty.isEmpty(telET.getText().toString())){
            flag=false;
        }
        return flag;
    }

    private void initListener(){

        telET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                    String text=telET.getText().toString();
                    if(text.length()>11){
                        telET.setText(text.substring(0,11));
                    }
            }
        });
      scanView.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View v) {

              AndPermission.with(getContext())
                      .permission(Permission.CAMERA)
                      .onGranted(new Action() {
                          @Override
                          public void onAction(List<String> permissions) {
                              InputActivity.selectkey=inputkey;
                              InputActivity.selectid=RenterPersonView.this.getId()+"";
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
    }

    public void updateView(String cardinfor){
       if(!IsNullOrEmpty.isEmpty(cardinfor)){
           String[] partinfor=cardinfor.split("\n");
           String namepart=partinfor[0];
           String numberPart=partinfor[5];
           nameET.setText(namepart.split(":")[1]);
           idcardET.setText(numberPart.split(":")[1]);
       }

    }


    public void setIDcardText(String idNum){
        idcardET.setText(idNum);
    }

    public String getUploadValue(){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put(renter_name,nameET.getText().toString().trim());
            jsonObject.put(renter_idnum,idcardET.getText().toString().trim());
            jsonObject.put(renter_tel,telET.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
   return  jsonObject.toString();
    }

    public View getDeleteCustomerV() {
        return deleteCustomerV;
    }
}
