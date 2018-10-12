package com.xiante.jingwu.qingbao.CustomView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xiante.jingwu.qingbao.Bean.Input.InputItemBean;
import com.xiante.jingwu.qingbao.Bean.Input.UploadBean;
import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.EmojiFilter;
import com.xiante.jingwu.qingbao.Util.Global;
import com.xiante.jingwu.qingbao.Util.IsNullOrEmpty;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by zhong on 2018/4/19.
 */

public class TextInputView extends LinearLayout implements InputView{
     private TextView nameView;
     private EditText valueET;
     private InputItemBean inputItemBean;
    public TextInputView(Context context) {
        super(context);
        initView(context);
    }

    public TextInputView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public TextInputView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context){
        View.inflate(context, R.layout.text_input_view,this);
        nameView=findViewById(R.id.inputNameTV);
        valueET=findViewById(R.id.inputValueET);
    }

    public void setInputName(String name){
        nameView.setText(name);
    }

    @Override
    public UploadBean getUploadValue() {
        String value="";
        try {
            value= URLEncoder.encode(valueET.getText().toString().trim(),"utf-8");
           // value= URLEncoder.encode(value,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new UploadBean(Global.TEXT, value);
    }

    @Override
    public boolean checkUploadValue() {
        boolean flag=true;
        if(inputItemBean.getIsMust().equals("1")){
            String value=valueET.getText().toString().trim();
            if(IsNullOrEmpty.isEmpty(value)){
                Toast.makeText(getContext(),inputItemBean.getStrFieldName()+"不能为空",Toast.LENGTH_SHORT).show();
                return false;
            }
             if(inputItemBean.getIsCheck().equals("1")){

                 if(!value.matches(inputItemBean.getStrCheckRule())){
                     Toast.makeText(getContext(),inputItemBean.getStrCheckErrInfo(),Toast.LENGTH_LONG).show();
                     flag=false;
                 }
             }
        }
        return  flag;
    }

    @Override
    public void initInputView(InputItemBean inputItemBean) {
        this.inputItemBean=inputItemBean;
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(params);
        valueET.setHint(inputItemBean.getStrPlaceHolder());
        nameView.setText(inputItemBean.getStrFieldName()+":");
    }

    @Override
    public void updateInputView(String string) {

    }
}
