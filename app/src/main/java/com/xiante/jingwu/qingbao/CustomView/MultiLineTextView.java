package com.xiante.jingwu.qingbao.CustomView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
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
 * Created by zhong on 2018/5/21.
 */

public class MultiLineTextView extends LinearLayout implements InputView {

    EditText inputTV;
    private InputItemBean inputItemBean;

    public MultiLineTextView(Context context) {
        super(context);
        init(context);
    }

    public MultiLineTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MultiLineTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        View.inflate(context,R.layout.multiline_text,this);
        inputTV=findViewById(R.id.inputTV);
    }



    @Override
    public UploadBean getUploadValue() {
        String value="";
        try {
             value=URLEncoder.encode(inputTV.getText().toString().trim(),"utf-8");
           // value= URLEncoder.encode(value,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new UploadBean(Global.MULTILINE_TEXT, value);
    }

    @Override
    public boolean checkUploadValue()
    {
        if(inputItemBean.getIsMust().equals("1")){
            String value=inputTV.getText().toString().trim();
            if(IsNullOrEmpty.isEmpty(value)){
                Toast.makeText(getContext(),inputItemBean.getStrFieldName()+"不能为空",Toast.LENGTH_SHORT).show();
                return false;
            }
        if(inputItemBean.getIsCheck().equals("1")){
                String tempstr=value.replaceAll("\n","");
            if(tempstr.matches(inputItemBean.getStrCheckRule())){
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
      inputTV.setHint(inputItemBean.getStrPlaceHolder());
    }

    @Override
    public void updateInputView(String string) {
       inputTV.setText(string);
    }
}
