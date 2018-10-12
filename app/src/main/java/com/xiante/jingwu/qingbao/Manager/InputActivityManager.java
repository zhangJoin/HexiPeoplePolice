package com.xiante.jingwu.qingbao.Manager;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.xiante.jingwu.qingbao.Bean.Input.InputItemBean;
import com.xiante.jingwu.qingbao.CustomView.HouseOwnerView;
import com.xiante.jingwu.qingbao.CustomView.ImageInputView;
import com.xiante.jingwu.qingbao.CustomView.InputView;
import com.xiante.jingwu.qingbao.CustomView.LabelInputView;
import com.xiante.jingwu.qingbao.CustomView.LocationInputView;
import com.xiante.jingwu.qingbao.CustomView.MultiLineTextView;
import com.xiante.jingwu.qingbao.CustomView.MultiMediaView;
import com.xiante.jingwu.qingbao.CustomView.PublishTypeView;
import com.xiante.jingwu.qingbao.CustomView.RenterGroupView;
import com.xiante.jingwu.qingbao.CustomView.ScanIDcardInputView;
import com.xiante.jingwu.qingbao.CustomView.SingleSelectInputView;
import com.xiante.jingwu.qingbao.CustomView.TextInputView;
import com.xiante.jingwu.qingbao.CustomView.VideoInputView;
import com.xiante.jingwu.qingbao.CustomView.VioiceInputView;
import com.xiante.jingwu.qingbao.CustomView.TimeInputView;
import com.xiante.jingwu.qingbao.Util.DisplayUtil;
import com.xiante.jingwu.qingbao.Util.Global;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zhong on 2018/4/19.
 */

public class InputActivityManager {

    private LinearLayout rootView;
    private Context context;
    private HashMap<String,InputView> allInputView;
    public InputActivityManager(Context context, LinearLayout rootView){
        this.context=context;
        this.rootView=rootView;
    }

    public void updateInputView(List<List<InputItemBean>> datalist){
          if(datalist!=null){
              for(int i=0;i<datalist.size();i++){
                  initInputActivityView(datalist.get(i));
                  if(i!=datalist.size()-1){
                      View view=new View(context);
                      view.setBackgroundColor(Color.parseColor("#eaeaea"));
                      LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.dip2px(context,15));
                      view.setLayoutParams(params);
                      rootView.addView(view);
                  }
              }
          }
    }


    public void initInputActivityView(List<InputItemBean> inputItemBeanList){
        for(int i=0;i<inputItemBeanList.size();i++){
            rootView.addView(getItemView(inputItemBeanList.get(i)));
        }

    }

    private View getItemView(InputItemBean inputItemBean){
        InputView inputView=null;
        switch (inputItemBean.getStrFieldType()){
            case Global.TEXT:
                inputView=new TextInputView(context);
                break;
            case Global.IMAGE:
                inputView=new ImageInputView(context);
                break;
            case Global.VOICE:
                inputView=new VioiceInputView(context);
                break;
            case Global.VIDEO:
                inputView=new VideoInputView(context);
                break;
            case Global.RENTER:
                inputView=new RenterGroupView(context);
                break;
            case Global.MULTIMIDEA:
                inputView=new MultiMediaView(context);
                break;
            case Global.TIME:
                inputView=new TimeInputView(context);
                break;
            case Global.MULTILINE_TEXT:
                inputView=new MultiLineTextView(context);
                break;
            case Global.LOCATION:
                inputView=new LocationInputView(context);
                break;
            case Global.MY_LOCATION:
                inputView=new LocationInputView(context);
                ((View)inputView).setVisibility(View.GONE);
                break;
            case Global.LABEL:
                inputView=new LabelInputView(context);
                break;
            case Global.SCAN_IDCARD:
                inputView=new ScanIDcardInputView(context);
                break;
            case Global.HOUSE_OWNER:
                inputView=new HouseOwnerView(context);
                break;
            case Global.PUBLISH_TYPE:
                inputView=new PublishTypeView(context);
                break;
            case Global.SINGLE_SELECT:
                inputView=new SingleSelectInputView(context);
                break;
        }
        inputView.initInputView(inputItemBean);
        if(!inputItemBean.getStrFieldType().equals(Global.LABEL)){
            allInputView.put(inputItemBean.getStrField(),inputView);
        }

        return  (View) inputView;
    }

    public void setAllInputView(HashMap<String, InputView> allInputView) {
        this.allInputView = allInputView;
    }

}
