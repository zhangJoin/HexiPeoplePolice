package com.xiante.jingwu.qingbao.CustomView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiante.jingwu.qingbao.Bean.Input.InputItemBean;
import com.xiante.jingwu.qingbao.Bean.Input.UploadBean;
import com.xiante.jingwu.qingbao.R;

/**
 * Created by zhong on 2018/5/23.
 */

public class LabelInputView extends LinearLayout implements InputView {

    private TextView labelView;

    public LabelInputView(Context context) {
        super(context);
      init(context);
    }

    public LabelInputView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
       init(context);
    }

    public LabelInputView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        View.inflate(context, R.layout.inputview_label,this);
        labelView=findViewById(R.id.labelView);
    }
    @Override
    public UploadBean getUploadValue() {
        return null;
    }

    @Override
    public boolean checkUploadValue() {
        return false;
    }

    @Override
    public void initInputView(InputItemBean inputItemBean) {
          labelView.setText(inputItemBean.getStrFieldName());
    }

    @Override
    public void updateInputView(String string) {

    }
}
