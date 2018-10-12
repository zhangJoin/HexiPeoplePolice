package com.xiante.jingwu.qingbao.CustomView;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.xiante.jingwu.qingbao.Activity.VideoPlayActivity;
import com.xiante.jingwu.qingbao.Bean.Input.InputItemBean;
import com.xiante.jingwu.qingbao.Bean.Input.UploadBean;
import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.DisplayUtil;
import com.xiante.jingwu.qingbao.Util.Global;
import com.xiante.jingwu.qingbao.Util.IsNullOrEmpty;
import com.xiante.jingwu.qingbao.Util.Utils;

/**
 * Created by zhong on 2018/4/26.
 */

public class SelectVideoView extends FrameLayout implements InputView {
    private ImageView imageView;
    private View deleteView,playView;
    String filepath="";
    int width=60;
    int margin=10;
    public SelectVideoView(Context context) {
        super(context);
        init(context);
    }

    public SelectVideoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SelectVideoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context){
        View.inflate(context, R.layout.multi_select_video,this);
        imageView=findViewById(R.id.imageviewIV);
        deleteView=findViewById(R.id.deleteView);
        playView=findViewById(R.id.playView);
        margin= DisplayUtil.dip2px(getContext(),margin);
        width=(getContext().getResources().getDisplayMetrics().widthPixels-5*margin-margin)/5;
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
                    Intent intent=new Intent(context, VideoPlayActivity.class);
                    intent.putExtra("videopath",filepath);
                    context.startActivity(intent);
                }else {
                    Toast.makeText(context,"请先录制视频才能播放",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    @Override
    public UploadBean getUploadValue() {
        return new UploadBean(Global.VIDEO,filepath);
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
       this.filepath=string;

       imageView.setImageBitmap(Utils.getMP4LastFrame(string));
       setVisibility(View.VISIBLE);
    }
}
