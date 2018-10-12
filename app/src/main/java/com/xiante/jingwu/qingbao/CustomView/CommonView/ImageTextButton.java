package com.xiante.jingwu.qingbao.CustomView.CommonView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.eallnetwork.framework.FailCallback;
import com.example.eallnetwork.framework.NetworkFactory;
import com.example.eallnetwork.framework.SuccessfulCallback;
import com.example.eallnetwork.workUtils.OkhttpFactory;
import com.xiante.jingwu.qingbao.NetWork.UrlManager;
import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.CodeExceptionUtil;
import com.xiante.jingwu.qingbao.Util.IsNullOrEmpty;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by zhong on 2018/4/28.
 */

public class ImageTextButton extends LinearLayout {

    private  ImageView imageView;
    private TextView textView,imagebuttonNum;

    public ImageTextButton(Context context) {
        super(context);
        init(context);
    }

    public ImageTextButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
       init(context);
    }

    public ImageTextButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        View.inflate(context, R.layout.image_textlayout,this);
        imageView=findViewById(R.id.buttonImage);
        textView=findViewById(R.id.buttonText);
        imagebuttonNum=findViewById(R.id.imagebuttonNum);
    }

    public void setImageNum(String numUrl){
        if(numUrl.equals("")){
            return;
        }
        NetworkFactory networkFactory= OkhttpFactory.getInstance();
        SuccessfulCallback successfulCallback=new SuccessfulCallback() {
            @Override
            public void success(String str) throws JSONException {
                if(new CodeExceptionUtil(getContext()).dealException(str)){
                    String num=new JSONObject(str).optString("resultData");
                    if(IsNullOrEmpty.isEmpty(num)||num.equals("0")){
                        imagebuttonNum.setVisibility(View.INVISIBLE);
                    }else {
                        imagebuttonNum.setVisibility(View.VISIBLE);
                        if(Integer.parseInt(num)>10){
                            imagebuttonNum.setText("···");
                        }else {
                            imagebuttonNum.setText(num);
                        }
                    }
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
        String neturl="";
        if(numUrl.contains("?")){
           neturl=new StringBuilder(urlManager.getData_url()).append(numUrl).append("&") .append(urlManager.getExtraStr()).toString();
        }else {
            neturl=new StringBuilder(urlManager.getData_url()).append(numUrl).append("?") .append(urlManager.getExtraStr()).toString();
        }
        networkFactory.start(NetworkFactory.METHOD_GET,neturl,null,successfulCallback,failCallback);
    }

    public void setButtomImage(String url){
        Glide.with(getContext()).load(url).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(imageView);
    }

    public void setButtonText(String title){
        textView.setText(title);
    }
    public void setTextColor(int color){
        textView.setTextColor(color);
    }
}
