package com.xiante.jingwu.qingbao.CustomView.CommonView;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.xiante.jingwu.qingbao.Activity.VideoActivity;
import com.xiante.jingwu.qingbao.Bean.Input.InputItemBean;
import com.xiante.jingwu.qingbao.Bean.Input.UploadBean;
import com.xiante.jingwu.qingbao.CustomView.InputView;
import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.Global;
import com.xiante.jingwu.qingbao.Util.IsNullOrEmpty;
import com.xiante.jingwu.qingbao.Util.Utils;

/**
 * Created by zhong on 2018/4/26.
 */

public class MultiMediaVoiceView extends FrameLayout implements InputView {
    private ImageView imageView;
    private View deleteView,playView;
    String filepath="";
    int width=60;
    int margin=10;
    public MultiMediaVoiceView(Context context) {
        super(context);
        init(context);
    }

    public MultiMediaVoiceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MultiMediaVoiceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context){
        View.inflate(context, R.layout.multi_select_voice,this);
        imageView=findViewById(R.id.imageviewIV);
        deleteView=findViewById(R.id.deleteView);
        playView=findViewById(R.id.playView);
        width= context.getResources().getDisplayMetrics().widthPixels/5;
        FrameLayout.LayoutParams params=new FrameLayout.LayoutParams(width,width);
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

                }else {
                    Toast.makeText(context,"请先录制音频才能播放",Toast.LENGTH_SHORT).show();
                }

            }
        });
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
       this.filepath=string;
    }
}
