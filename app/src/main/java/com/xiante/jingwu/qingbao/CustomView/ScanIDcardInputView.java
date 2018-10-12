package com.xiante.jingwu.qingbao.CustomView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
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
import com.xiante.jingwu.qingbao.Activity.ScanIDcardActivity;
import com.xiante.jingwu.qingbao.Bean.Input.InputItemBean;
import com.xiante.jingwu.qingbao.Bean.Input.UploadBean;
import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.Global;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhong on 2018/5/23.
 */

public class ScanIDcardInputView extends LinearLayout implements InputView {

    private TextView nameView;
    private EditText idnumET;
    private View go_scanView;
    private InputItemBean inputItemBean;
    private TRECAPIImpl engineDemo;
    public ScanIDcardInputView(Context context) {
        super(context);
        init(context);
    }

    public ScanIDcardInputView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
       init(context);
    }

    public ScanIDcardInputView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context){
        View.inflate(context, R.layout.scan_idcardview,this);
        nameView=findViewById(R.id.nameview);
        idnumET=findViewById(R.id.inputValueIDcardET);
        go_scanView=findViewById(R.id.goScanIDcardV);
        go_scanView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                InputActivity.selectkey=inputItemBean.getStrField();
                InputActivity.selectid="";
                CaptureActivity.tengineID = TengineID.TIDCARD2;
                CaptureActivity.ShowCopyRightTxt = "Esint";
                Intent intent = new Intent(context, CaptureActivity.class);
                intent.putExtra("engine", engineDemo);
                ((Activity)context).startActivityForResult(intent, 0);
            }
        });
        engineDemo = new TRECAPIImpl();
        TStatus tStatus = engineDemo.TR_StartUP(context,engineDemo.TR_GetEngineTimeKey());
        if (tStatus == TStatus.TR_TIME_OUT ) {
            Toast.makeText(context, "引擎过期", Toast.LENGTH_SHORT).show();
        }
        else  if (tStatus == TStatus.TR_FAIL) {
            Toast.makeText(context, "引擎初始化失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public UploadBean getUploadValue() {
        return new UploadBean(Global.SCAN_IDCARD,idnumET.getText().toString().trim());
    }

    @Override
    public boolean checkUploadValue() {
        if(inputItemBean.getIsMust().equals("1")){
            if(inputItemBean.getIsCheck().equals("1")){
                String value=idnumET.getText().toString().trim();
                if(value.matches(inputItemBean.getStrCheckRule())){
                    return  true;
                }else {
                    Toast.makeText(getContext(),inputItemBean.getStrCheckErrInfo(),Toast.LENGTH_LONG).show();
                    return false;
                }
            }else {
                return true;
            }
        }else {
            return true;
        }

    }

    @Override
    public void initInputView(InputItemBean inputItemBean) {
    this.inputItemBean=inputItemBean;
    nameView.setText(inputItemBean.getStrFieldName());
    }
    //{"value":jksl,"id":446656,type:""}
    @Override
    public void updateInputView(String string) {
        try {
            JSONObject jsonObject=new JSONObject(string);
            String value=jsonObject.optString("value");
            idnumET.setText(value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
