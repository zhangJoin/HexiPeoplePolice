package com.xiante.jingwu.qingbao.CustomView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xiante.jingwu.qingbao.Bean.Input.InputItemBean;
import com.xiante.jingwu.qingbao.Bean.Input.UploadBean;
import com.xiante.jingwu.qingbao.Util.DisplayUtil;
import com.xiante.jingwu.qingbao.Util.Global;
import com.xiante.jingwu.qingbao.Util.IsNullOrEmpty;
import com.xiante.jingwu.qingbao.Util.Utils;

/**
 * Created by zhong on 2018/4/26.
 */

public class MultiMediaVideoView extends ImageView implements InputView {

    String filepath;
    int width=60;
    int margin=10;
    public MultiMediaVideoView(Context context) {
        super(context);
        init();
    }

    public MultiMediaVideoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MultiMediaVideoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        setScaleType(ScaleType.CENTER_CROP);
        margin= DisplayUtil.dip2px(getContext(),margin);
        width=(getContext().getResources().getDisplayMetrics().widthPixels-5*margin-margin)/5;

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
       LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(width,width);
       params.setMargins(margin,margin,0,0);
       setLayoutParams(params);
       setImageBitmap(Utils.getMP4LastFrame(string));
    }
}
