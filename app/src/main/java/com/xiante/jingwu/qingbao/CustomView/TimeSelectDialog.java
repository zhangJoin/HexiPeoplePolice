package com.xiante.jingwu.qingbao.CustomView;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.xiante.jingwu.qingbao.CustomView.CommonView.TimeSelectView;
import com.xiante.jingwu.qingbao.MessageEvent.UpdateViewMessage;
import com.xiante.jingwu.qingbao.R;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by zhong on 2018/4/27.
 */

public class TimeSelectDialog extends Dialog  {

    private String inputkey;
    TextView timeCancelTV,timeSureTV;
    TimeSelectView timeSelectView;
    public TimeSelectDialog(@NonNull Context context,String inputkey) {
        super(context, R.style.MyDialog);
        this.inputkey=inputkey;
        setContentView(R.layout.time_picker_dialog);
        setCancelable(false);
        timeCancelTV=findViewById(R.id.timeCancelTV);
        timeSureTV=findViewById(R.id.timeSureTV);
        timeSelectView=findViewById(R.id.timeview);
        initListener();
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
    }
private void initListener(){
        timeCancelTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        timeSureTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new UpdateViewMessage(inputkey,timeSelectView.getSelectTime()));
                dismiss();
            }
        });

}

}
