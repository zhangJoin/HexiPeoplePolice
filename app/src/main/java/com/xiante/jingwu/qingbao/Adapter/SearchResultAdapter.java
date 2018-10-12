package com.xiante.jingwu.qingbao.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.eallnetwork.framework.FailCallback;
import com.example.eallnetwork.framework.NetworkFactory;
import com.example.eallnetwork.framework.SuccessfulCallback;
import com.example.eallnetwork.workUtils.OkhttpFactory;
import com.xiante.jingwu.qingbao.Bean.Common.SearchResultBean;
import com.xiante.jingwu.qingbao.Dialog.LoaddingDialog;
import com.xiante.jingwu.qingbao.NetWork.UrlManager;
import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.CodeExceptionUtil;
import com.xiante.jingwu.qingbao.Util.Utils;

import org.json.JSONException;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

/**
 * @author 郑靖廷 Yoda_T
 * @email 17600203706@163.com
 * @time $date$ $time$
 */
public class SearchResultAdapter extends DefaultAdapter<SearchResultBean>{
    private List<SearchResultBean> listData;
    private Context ctx;
    private LoaddingDialog loaddingDialog;
    private String unityGuid="";
    public SearchResultAdapter(List<SearchResultBean> datas, Context context,String unityGuid) {
        super(datas, context);
        this.listData=datas;
        this.ctx=context;
        this.unityGuid=unityGuid;
        loaddingDialog=new LoaddingDialog(ctx);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder mHolder;
        if(convertView==null){
            mHolder=new Holder();
            convertView= LayoutInflater.from(ctx).inflate(R.layout.search_result_item,null);
            mHolder.mPersonUrl=convertView.findViewById(R.id.iv_result_item);
            mHolder.mPersonName=convertView.findViewById(R.id.tv_result_name_item);
            mHolder.mPersonTel=convertView.findViewById(R.id.tv_result_phone_item);
            mHolder.mPersonState=convertView.findViewById(R.id.tv_result_state_item);
            convertView.setTag(mHolder);
        }else{
           mHolder= (Holder) convertView.getTag();
        }
        final SearchResultBean resultBean = listData.get(position);
        mHolder.mPersonName.setText(resultBean.getPersonName().trim());
        mHolder.mPersonTel.setText(resultBean.getPersonTel());
        Glide.with(ctx).load(resultBean.getStrPortrait()).error(R.drawable.default_pic).placeholder(R.drawable.default_pic).into(mHolder.mPersonUrl);
       mHolder.mPersonState.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               commitAddPowerPerson(resultBean.getStrGuid(),unityGuid);
           }
       });


        return convertView;
    }

    private void commitAddPowerPerson(String userguid,String unityguid) {
        loaddingDialog.showDialog();
       boolean isSuccess= Utils.isSuccess(ctx);
        if(!isSuccess){
            Toast.makeText(ctx,ctx.getString(R.string.netError),Toast.LENGTH_SHORT).show();
            return;
        }
        NetworkFactory mNetworkFactory = OkhttpFactory.getInstance();
        SuccessfulCallback mSuccessfulCallback = new SuccessfulCallback() {
            @Override
            public void success(String str) throws JSONException {
                if (new CodeExceptionUtil(ctx).dealException(str)) {
                    Toast.makeText(ctx, "添加成功", Toast.LENGTH_SHORT).show();
                    ((Activity)ctx).finish();
                }
                loaddingDialog.dismissAniDialog();

            }

            @Override
            public void success(InputStream ism, long conentLength) {

            }
        };
        FailCallback mFailCallback = new FailCallback() {
            @Override
            public void fail(String str) {
                Log.i("urlfail", str);
            }
        };
        HashMap<String, String> param = new HashMap<>();
        param.put("strUserGuid", userguid);
        param.put("strUnityGuid", unityguid);
        UrlManager urlManager = new UrlManager(ctx);
        mNetworkFactory.start(NetworkFactory.METHOD_GET, urlManager.getScanPowerUrl(), param, mSuccessfulCallback
                , mFailCallback);
    }



    class Holder{
        TextView mPersonName,mPersonTel,mPersonState;
        ImageView mPersonUrl;
    }
}
