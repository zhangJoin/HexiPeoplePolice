package com.xiante.jingwu.qingbao.CustomView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.xiante.jingwu.qingbao.Bean.Input.InputItemBean;
import com.xiante.jingwu.qingbao.Bean.Input.UploadBean;
import com.xiante.jingwu.qingbao.Manager.DialogManager;
import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.Global;
import com.xiante.jingwu.qingbao.Util.IsNullOrEmpty;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by zhong on 2018/4/19.
 */

public class VioiceInputView extends LinearLayout implements InputView{
     private String videopath="";
     private MultiMediaVoiceView selectvoiceView;
     private View go_selectVoiceView;
     InputItemBean inputItemBean;
    public VioiceInputView(Context context) {
        super(context);
        initView(context);
    }

    public VioiceInputView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public VioiceInputView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(final Context context){
        View.inflate(context, R.layout.voice_input_view,this);
        selectvoiceView=findViewById(R.id.selectVoiceView);
        go_selectVoiceView=findViewById(R.id.go_selectVoiceView);
        go_selectVoiceView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
            }
        });


    }

    private void checkPermission() {
        AndPermission.with(getContext())
                .permission(Permission.CAMERA,Permission.RECORD_AUDIO)
                .onGranted(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        DialogManager dialogManager=new DialogManager(getContext());
                        dialogManager.showRecordingDialog(inputItemBean.getStrField(),"");
                    }
                })
                .onDenied(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        Toast.makeText(getContext(),"请开启录制音频权限",Toast.LENGTH_SHORT).show();
                    }
                }).start();
    }


    @Override
    public UploadBean getUploadValue() {
        return new UploadBean(Global.VIDEO, videopath);
    }

    @Override
    public boolean checkUploadValue() {
        if(inputItemBean.getIsMust().equals("1")){
            if(IsNullOrEmpty.isEmpty(videopath)){
                Toast.makeText(getContext(),"上传音频不能为空",Toast.LENGTH_SHORT).show();
                return false;
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
//        LayoutParams params=new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        setLayoutParams(params);
//        nameView.setText(inputItemBean.getStrFieldName());

    }
//{"id":id,"value":valuskldjf}
    @Override
    public void updateInputView(String string) {
        try {
            selectvoiceView.setVisibility(View.VISIBLE);
            JSONObject jsonObject=new JSONObject(string);
            this.videopath=jsonObject.optString("value");
            selectvoiceView.updateInputView(videopath);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
