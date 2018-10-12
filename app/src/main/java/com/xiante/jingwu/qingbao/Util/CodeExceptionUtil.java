package com.xiante.jingwu.qingbao.Util;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhong on 2018/5/2.
 */

public class CodeExceptionUtil {
    private Context context;

    public CodeExceptionUtil(Context context) {
        this.context = context;
    }

    public boolean dealException(String result){
        boolean flag=true;
        try {
            JSONObject rootObject=new JSONObject(result);
            int code=rootObject.optInt("resultCode");
            if(code!=200){
                flag=false;
                String msg=rootObject.optString("resultMsg");
                Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            flag=false;
            e.printStackTrace();
            Toast.makeText(context,"数据解析异常",Toast.LENGTH_SHORT).show();
        }
        return flag;
    }

}
