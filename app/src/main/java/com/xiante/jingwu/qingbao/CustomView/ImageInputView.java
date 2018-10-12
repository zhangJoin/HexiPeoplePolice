package com.xiante.jingwu.qingbao.CustomView;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.xiante.jingwu.qingbao.Activity.AlbumsActivity;
import com.xiante.jingwu.qingbao.Bean.Input.InputItemBean;
import com.xiante.jingwu.qingbao.Bean.Input.UploadBean;
import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.Global;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhong on 2018/4/19.
 */

public class ImageInputView extends LinearLayout implements InputView{
     private MultiMediaGridView imageGL;
     private View multiMedia_image;
     private InputItemBean inputItemBean;
     private int imageWidth=0;
    public ImageInputView(Context context) {
        super(context);
        initView(context);
    }

    public ImageInputView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ImageInputView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(final Context context){
        View.inflate(context, R.layout.image_input_view,this);
        imageGL=findViewById(R.id.mediaImageDisplay);
        multiMedia_image=findViewById(R.id.multiMedia_image);
        multiMedia_image.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, AlbumsActivity.class);
                intent.putExtra("maxSelect",Integer.parseInt(inputItemBean.getImageNum())-imageGL.getSelectSize()+"");
                intent.putExtra(Global.INPUTKEY,inputItemBean.getStrField());
                intent.putExtra(Global.VIEW_ID,"");
                context.startActivity(intent);
            }
        });
    }

    public void setInputName(String name){
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public UploadBean getUploadValue() {
        return imageGL.getUploadValue();
    }

    @Override
    public boolean checkUploadValue() {
        if(inputItemBean.getIsMust().equals("1")){
            return imageGL.checkUploadValue();
        }else {
            return true;
        }

    }

    @Override
    public void initInputView(InputItemBean inputItemBean) {
        this.inputItemBean=inputItemBean;
        imageGL.initInputView(inputItemBean);
    }
    //{"id":id,"value":skjdf}
    @Override
    public void updateInputView(String string) {
        try {
            JSONObject jsonObject=new JSONObject(string);
            String value=jsonObject.optString("value");
            imageGL.setVisibility(View.VISIBLE);
            imageGL.updateInputView(value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
