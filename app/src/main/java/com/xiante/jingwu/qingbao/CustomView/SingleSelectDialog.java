package com.xiante.jingwu.qingbao.CustomView;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.xiante.jingwu.qingbao.CustomView.WheelView.WheelView;
import com.xiante.jingwu.qingbao.MessageEvent.UpdateViewMessage;
import com.xiante.jingwu.qingbao.R;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by zhong on 2018/4/27.
 */

public class SingleSelectDialog extends Dialog  {

    private String inputkey;
    TextView timeCancelTV,timeSureTV;
    WheelView singleWV;
    List<String> keylist,valueList;
    String selectValue="";
    public SingleSelectDialog(@NonNull Context context, String inputkey,String strFieldValue) {
        super(context, R.style.MyDialog);
        this.inputkey=inputkey;
        setContentView(R.layout.single_select_dialog);
        setCancelable(false);
        timeCancelTV=findViewById(R.id.timeCancelTV);
        timeSureTV=findViewById(R.id.timeSureTV);
        singleWV=findViewById(R.id.singleWV);

        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        try {
            JSONObject jsonObject=new JSONObject(strFieldValue);
            keylist= JSON.parseArray(jsonObject.optString("key"),String.class);
            valueList= JSON.parseArray(jsonObject.optString("value"),String.class);
            singleWV.setItems(keylist,0);
           selectValue=0+"";
        } catch (JSONException e) {
            e.printStackTrace();
        }

        singleWV.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int selectedIndex, String item) {
                selectValue=selectedIndex+"";
            }
        });
        initListener();
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
                EventBus.getDefault().post(new UpdateViewMessage(inputkey,selectValue));
                dismiss();
            }
        });

}

}
