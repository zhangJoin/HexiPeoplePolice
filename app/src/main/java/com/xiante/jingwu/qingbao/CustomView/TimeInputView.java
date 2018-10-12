package com.xiante.jingwu.qingbao.CustomView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eallnetwork.framework.FailCallback;
import com.example.eallnetwork.framework.NetworkFactory;
import com.example.eallnetwork.framework.SuccessfulCallback;
import com.example.eallnetwork.workUtils.OkhttpFactory;
import com.xiante.jingwu.qingbao.Bean.Input.InputItemBean;
import com.xiante.jingwu.qingbao.Bean.Input.UploadBean;
import com.xiante.jingwu.qingbao.NetWork.UrlManager;
import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.CodeExceptionUtil;
import com.xiante.jingwu.qingbao.Util.IsNullOrEmpty;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhong on 2018/4/27.
 */

public class TimeInputView extends LinearLayout implements InputView {

    private TextView nameView,timeview;
    private InputItemBean inputItemBean;
    private LinearLayout inputLlClick;
    private String time="";
    public TimeInputView(Context context) {
        super(context);
         init(context);
    }

    public TimeInputView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TimeInputView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @SuppressLint("SimpleDateFormat")
    private void init(final Context context){
        View.inflate(context, R.layout.time_select_input_view,this);
        nameView=findViewById(R.id.inputNameTV);
        timeview=findViewById(R.id.inputValueTV);
        inputLlClick=findViewById(R.id.inputLlClick);
        time=new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
        timeview.setText(time);
        inputLlClick.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeSelectDialog dialog=new TimeSelectDialog(context,inputItemBean.getStrField());
                dialog.show();
            }
        });
    }

    @Override
    public UploadBean getUploadValue() {
        time=timeview.getText().toString().trim();
        try {
            time= URLEncoder.encode(time,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new UploadBean(inputItemBean.getStrField(),time);
    }

    @Override
    public boolean checkUploadValue() {
        if(inputItemBean.getIsMust().equals("1")){
            if(IsNullOrEmpty.isEmpty(time)){
                Toast.makeText(getContext(),inputItemBean.getStrCheckErrInfo(),Toast.LENGTH_SHORT).show();
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
       nameView.setText(inputItemBean.getStrFieldName()+":");
       if(!IsNullOrEmpty.isEmpty(inputItemBean.getStrUrl())){
              undatePlaceHoldTime(inputItemBean.getStrUrl());
       }
    }

    private void undatePlaceHoldTime(String strUrl) {
        NetworkFactory networkFactory= OkhttpFactory.getInstance();
        SuccessfulCallback successfulCallback=new SuccessfulCallback() {
            @Override
            public void success(String str) throws JSONException {
                if(new CodeExceptionUtil(getContext()).dealException(str)){
                   time=new JSONObject(str).optString("resultData");
                   timeview.setText(time);
                }
            }
            @Override
            public void success(InputStream ism, long conentLength) {

            }
        };
        FailCallback failCallback=new FailCallback() {
            @Override
            public void fail(String str) {

            }
        };
        UrlManager urlManager=new UrlManager(getContext());
        String url="";
        if(strUrl.contains("?")){
            url=new StringBuilder(urlManager.getData_url()).append(strUrl).append("&").append(urlManager.getExtraStr()).toString();
        }else {
            url=new StringBuilder(urlManager.getData_url()).append(strUrl).append("?").append(urlManager.getExtraStr()).toString();
        }
        networkFactory.start(NetworkFactory.METHOD_GET,url,null,successfulCallback,failCallback);
    }

    @Override
    public void updateInputView(String string) {
        this.time=string;
        timeview.setText(string);
    }

    public String getTime() {
        return time;
    }
}
