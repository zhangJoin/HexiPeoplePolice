package com.xiante.jingwu.qingbao.CustomView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xiante.jingwu.qingbao.Bean.Input.InputItemBean;
import com.xiante.jingwu.qingbao.Bean.Input.UploadBean;
import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.Global;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by zhong on 2018/4/26.
 */

public class RenterGroupView extends LinearLayout implements InputView{
    private Context context;
    private TextView nameView;
    private LinearLayout renterContainer;
    private View addRenterView;
    private List<RenterPersonView> allRenterList;
    private InputItemBean inputItemBean;
    public RenterGroupView(Context context) {
        super(context);
        init(context);
    }

    public RenterGroupView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RenterGroupView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        this.context=context;
        View.inflate(context, R.layout.rent_group,this);
        nameView=findViewById(R.id.inputNameTV);
        renterContainer=findViewById(R.id.renterContainerLL);
        addRenterView=findViewById(R.id.addCustomView);
        allRenterList=new ArrayList<>();

        addRenterView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                addOneRenter();
            }
        });
    }


    @Override
    public UploadBean getUploadValue() {
        JSONArray array=new JSONArray();
        String value="";
        for(int i=0;i<allRenterList.size();i++){
            array.put(allRenterList.get(i).getUploadValue());
        }
        try {
            value= URLEncoder.encode(array.toString(),"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return new UploadBean(Global.RENTER,value);
    }

    @Override
    public boolean checkUploadValue() {
        if(inputItemBean.getIsMust().equals("1")){
            if(allRenterList.size()>0){
                for(int i=0;i<allRenterList.size();i++){
                    if(!allRenterList.get(i).checkValue()){
                       Toast.makeText(context,"租户信息不完整",Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
                return true;
            }else {
                Toast.makeText(getContext(),"租户信息不能为空",Toast.LENGTH_SHORT).show();
                return false;
            }
        }else {
            return true;
        }

    }

    @Override
    public void initInputView(InputItemBean inputItemBean) {
        this.inputItemBean=inputItemBean;
          nameView.setText(inputItemBean.getStrFieldName());
          addOneRenter();
    }
    //{"value":jksl,"id":446656,type:""}
    @Override
    public void updateInputView(String string) {
        try {
            JSONObject jsonObject=new JSONObject(string);
            String value=jsonObject.optString("value");
            int id=jsonObject.optInt("id");
            RenterPersonView personView=findViewById(id);
            if(personView!=null){
                personView.updateView(value);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addOneRenter(){
        final RenterPersonView renterPersonView=new RenterPersonView(context);
        renterPersonView.setInputkey(inputItemBean.getStrField());
        renterPersonView.setId(new Random().nextInt(Integer.MAX_VALUE));
        LinearLayout.LayoutParams params=new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,10,0,0);
        renterPersonView.setLayoutParams(params);
        renterContainer.addView(renterPersonView);
        allRenterList.add(renterPersonView);
        renterPersonView.getDeleteCustomerV().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(renterContainer.getChildCount()==1){
                    return;
                }
                allRenterList.remove(renterPersonView);
                renterContainer.removeView(renterPersonView);
            }
        });
    }
}
