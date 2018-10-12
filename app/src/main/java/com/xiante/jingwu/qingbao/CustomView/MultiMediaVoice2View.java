package com.xiante.jingwu.qingbao.CustomView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.xiante.jingwu.qingbao.Bean.Common.FileUploadBean;
import com.xiante.jingwu.qingbao.Bean.Input.InputItemBean;
import com.xiante.jingwu.qingbao.Bean.Input.UploadBean;
import com.xiante.jingwu.qingbao.Bean.InputModifyBean;
import com.xiante.jingwu.qingbao.Dialog.PlayRecodeDialog;
import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.Global;
import com.xiante.jingwu.qingbao.Util.IsNullOrEmpty;

/**
 * Created by zhong on 2018/4/26.
 */

public class MultiMediaVoice2View extends FrameLayout implements InputView {
    private ImageView imageView;
    private View deleteView,playView;
    String filepath="";
    int width=60;
    int margin=10;
    InputModifyBean modifyBean;
    public MultiMediaVoice2View(Context context) {
        super(context);
        init(context);
    }

    public MultiMediaVoice2View(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MultiMediaVoice2View(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context){
        View.inflate(context, R.layout.multi_select_voice,this);
        imageView=findViewById(R.id.imageviewIV);
        deleteView=findViewById(R.id.deleteView);
        playView=findViewById(R.id.playView);
        width= context.getResources().getDisplayMetrics().widthPixels/5;
        LayoutParams params=new LayoutParams(width,width);
        findViewById(R.id.rootView).setLayoutParams(params);

        deleteView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                filepath="";
                setVisibility(View.GONE);
            }
        });

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!IsNullOrEmpty.isEmpty(filepath)){
                    PlayRecodeDialog playRecodeDialog=new PlayRecodeDialog(context,filepath);
                    playRecodeDialog.show();
                }else {
                    Toast.makeText(context,"请先录制音频才能播放",Toast.LENGTH_SHORT).show();
                }

            }
        });

        modifyBean=new InputModifyBean();
    }


    @Override
    public UploadBean getUploadValue() {
        return new UploadBean(Global.VOICE,filepath);
    }

    @Override
    public boolean checkUploadValue() {
        return !IsNullOrEmpty.isEmpty(filepath);
    }

    @Override
    public void initInputView(InputItemBean inputItemBean) {

    }

    @Override
    public void updateInputView(String string) {
        setVisibility(View.VISIBLE);
        modifyBean.setImageType(InputModifyBean.LOCAL_IMAGE);
        modifyBean.setLocalpath(string);
       this.filepath=string;
    }

    public void updateNetVoiceView(String string) {
        FileUploadBean fileUploadBean= JSON.parseObject(string,FileUploadBean.class);
        modifyBean.setImageType(InputModifyBean.NET_IMAGE);
        modifyBean.setUploadBean(fileUploadBean);
        this.filepath=fileUploadBean.getPath();
        setVisibility(View.VISIBLE);
    }

    public InputModifyBean getModifyBean() {
        return modifyBean;
    }
}
