package com.xiante.jingwu.qingbao.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.eallnetwork.framework.FailCallback;
import com.example.eallnetwork.framework.NetworkFactory;
import com.example.eallnetwork.framework.SuccessfulCallback;
import com.example.eallnetwork.workUtils.OkhttpFactory;
import com.xiante.jingwu.qingbao.Activity.PowerSourceActivity;
import com.xiante.jingwu.qingbao.Bean.Common.PowerMemberEntity;
import com.xiante.jingwu.qingbao.Dialog.LoaddingDialog;
import com.xiante.jingwu.qingbao.MessageEvent.PowermemberMessage;
import com.xiante.jingwu.qingbao.NetWork.UrlManager;
import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.CodeExceptionUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zhong on 2018/4/28.
 */

public class PowerMemberAdapter extends BaseAdapter {

    LayoutInflater inflater;
    Context context;
    List<PowerMemberEntity> datalist;
    LoaddingDialog loaddingDialog;

    public PowerMemberAdapter(Context context, List<PowerMemberEntity> datalist) {
        this.context = context;
        this.datalist = datalist;
        inflater = LayoutInflater.from(context);
        loaddingDialog = new LoaddingDialog(context);
    }

    @Override
    public int getCount() {
        return datalist.size();
    }

    @Override
    public Object getItem(int position) {
        return datalist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Member member = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.power_member_item, null);
            member = new Member(convertView);
            convertView.setTag(member);
        } else {
            member = (Member) convertView.getTag();
        }
        final PowerMemberEntity entity = datalist.get(position);
        Glide.with(context).load(entity.getStrPortrait()).placeholder(R.drawable.default_pic).error(R.drawable.default_pic).into(member.memberImageIV);
        member.memberNameTV.setText(entity.getStrName().trim());
        member.memberPhoneTV.setText(entity.getStrMobile());
        if (entity.getStrUserType().equals(PowerSourceActivity.MANAGER)) {
            member.setManagerTV.setText("取消管理员");
        } else {
            member.setManagerTV.setText("设为管理");
        }
        member.setManagerTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (entity.getStrUserType().equals(PowerSourceActivity.MANAGER)) {
                    cancelManagerPower(entity);
                } else {
                    setToManager(entity);
                }
            }
        });
        member.deleteMemberTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMember(entity);
            }
        });

        if (entity.getStrUserType().equals(PowerSourceActivity.GROUP_OWNER)) {
            member.setManagerTV.setVisibility(View.GONE);
            member.deleteMemberTV.setVisibility(View.GONE);
        } else if (entity.getStrUserType().equals(PowerSourceActivity.MANAGER)) {
//            member.setManagerTV.setVisibility(View.GONE);
        } else {
            member.setManagerTV.setVisibility(View.VISIBLE);
            member.deleteMemberTV.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    class Member {

        ImageView memberImageIV;
        TextView memberNameTV, memberPhoneTV;
        TextView setManagerTV, deleteMemberTV;

        public Member(View itemView) {
            memberImageIV = itemView.findViewById(R.id.memberImageIV);
            memberNameTV = itemView.findViewById(R.id.memberNameTV);
            memberPhoneTV = itemView.findViewById(R.id.memberPhoneTV);
            setManagerTV = itemView.findViewById(R.id.setManagerTV);
            deleteMemberTV = itemView.findViewById(R.id.deleteMemberTV);
        }
    }

    private void setToManager(PowerMemberEntity powerMemberEntity) {
        loaddingDialog.showDialog();
        NetworkFactory networkFactory = OkhttpFactory.getInstance();
        SuccessfulCallback successfulCallback = new SuccessfulCallback() {
            @Override
            public void success(String str) throws JSONException {
                loaddingDialog.dismissAniDialog();
                if (new CodeExceptionUtil(context).dealException(str)) {
                    EventBus.getDefault().post(new PowermemberMessage());
                }
            }

            @Override
            public void success(InputStream ism, long conentLength) {

            }
        };
        FailCallback failCallback = new FailCallback() {
            @Override
            public void fail(String str) {

            }
        };
        HashMap<String, String> param = new HashMap<>();
        param.put("strUnityGuid", powerMemberEntity.getStrUnityGuid());
        param.put("strUserGuid", powerMemberEntity.getStrUserGuid());
        UrlManager urlManager = new UrlManager(context);
        networkFactory.start(NetworkFactory.METHOD_GET, urlManager.getSetMemberManager(), param, successfulCallback, failCallback);
    }


    private void cancelManagerPower(PowerMemberEntity powerMemberEntity) {
        loaddingDialog.showDialog();
        NetworkFactory networkFactory = OkhttpFactory.getInstance();
        SuccessfulCallback successfulCallback = new SuccessfulCallback() {
            @Override
            public void success(String str) throws JSONException {
                loaddingDialog.dismissAniDialog();
                if (new CodeExceptionUtil(context).dealException(str)) {
                    EventBus.getDefault().post(new PowermemberMessage());
                }
            }

            @Override
            public void success(InputStream ism, long conentLength) {

            }
        };
        FailCallback failCallback = new FailCallback() {
            @Override
            public void fail(String str) {

            }
        };
        HashMap<String, String> param = new HashMap<>();
        param.put("strUnityGuid", powerMemberEntity.getStrUnityGuid());
        param.put("strUserGuid", powerMemberEntity.getStrUserGuid());
        UrlManager urlManager = new UrlManager(context);
        networkFactory.start(NetworkFactory.METHOD_GET, urlManager.getCancelManagerPower(), param, successfulCallback, failCallback);
    }

    private void deleteMember(PowerMemberEntity powerMemberEntity) {
        loaddingDialog.showDialog();
        NetworkFactory networkFactory = OkhttpFactory.getInstance();
        SuccessfulCallback successfulCallback = new SuccessfulCallback() {
            @Override
            public void success(String str) throws JSONException {
                loaddingDialog.dismissAniDialog();
                if (new CodeExceptionUtil(context).dealException(str)) {
                    EventBus.getDefault().post(new PowermemberMessage());
                }
            }

            @Override
            public void success(InputStream ism, long conentLength) {

            }
        };
        FailCallback failCallback = new FailCallback() {
            @Override
            public void fail(String str) {

            }
        };
        HashMap<String, String> param = new HashMap<>();
        param.put("strUnityGuid", powerMemberEntity.getStrUnityGuid());
        param.put("strUserGuid", powerMemberEntity.getStrUserGuid());
        UrlManager urlManager = new UrlManager(context);
        networkFactory.start(NetworkFactory.METHOD_GET, urlManager.deleteMemberManager(), param, successfulCallback, failCallback);
    }

}
