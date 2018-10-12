package com.xiante.jingwu.qingbao.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.eallnetwork.framework.FailCallback;
import com.example.eallnetwork.framework.NetworkFactory;
import com.example.eallnetwork.framework.SuccessfulCallback;
import com.example.eallnetwork.workUtils.OkhttpFactory;
import com.idcard.CardInfo;
import com.xiante.jingwu.qingbao.Bean.Common.ClickEntity;
import com.xiante.jingwu.qingbao.Bean.Input.InputItemBean;
import com.xiante.jingwu.qingbao.Bean.Input.UploadBean;
import com.xiante.jingwu.qingbao.CustomView.InputView;
import com.xiante.jingwu.qingbao.CustomView.MultiMediaView;
import com.xiante.jingwu.qingbao.Dialog.LoaddingDialog;
import com.xiante.jingwu.qingbao.Manager.InputActivityManager;
import com.xiante.jingwu.qingbao.MessageEvent.UpdateViewMessage;
import com.xiante.jingwu.qingbao.NetWork.UrlManager;
import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.CodeExceptionUtil;
import com.xiante.jingwu.qingbao.Util.Global;
import com.xiante.jingwu.qingbao.Util.IsNullOrEmpty;
import com.xiante.jingwu.qingbao.Util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by zhong on 2018/4/19.
 */

public class InputActivity extends BaseActivity {

    private LinearLayout rootView;
    private TextView comfirmTV;

    private HashMap<String,InputView> allInputView;
    private List<List<InputItemBean>> inputItemBeanList;
    private InputActivityManager inputActivityManager;
    private LoaddingDialog loaddingDialog;
    private ClickEntity clickEntity;
    private String uploadurl="";


    public static String selectid="",selectkey="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        EventBus.getDefault().register(this);
        initView();
        initData();
        initListener();
        getNetData();
        //textRxAndroid();
    }


    private void textRxAndroid(){
        List<String> all=new ArrayList<>();
        for(int i=0;i<10;i++){
            all.add("ksdjfklsjf"+i);
        }
        Observable observable=Observable.from(all);
        Subscriber subscriber=new Subscriber() {
            @Override
            public void onCompleted() {
                Log.i("onnext","fninish");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object o) {
                Log.i("onnext",o.toString());
                comfirmTV.setText(o.toString());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        observable.observeOn(Schedulers.newThread()).subscribe(subscriber);
    }

    @Override
    public void initView() {
        rootView=findViewById(R.id.rootView);
        comfirmTV=findViewById(R.id.comfirmTV);
        loaddingDialog=new LoaddingDialog(this);
        loaddingDialog.setCancelable(false);
    }

    @Override
    public void initData() {
      allInputView=new HashMap<>();
      inputItemBeanList=new ArrayList<>();
      inputActivityManager=new InputActivityManager(this,rootView);
      inputActivityManager.setAllInputView(allInputView);
      clickEntity= (ClickEntity) getIntent().getSerializableExtra(Global.CLICK_ACTION);
      initTitlebar(clickEntity.getStrTitle(),clickEntity.getStrText(),clickEntity.getStrIco());
    }

    @Override
    public void initListener() {
        comfirmTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadValue();
            }
        });

    }
//
//    private void uploadValue() {
//        Set<String> keyset=allInputView.keySet();
//        for (String key:keyset) {
//            if(!allInputView.get(key).checkUploadValue()){
//                return;
//            }
//        }
//
//        loaddingDialog.showDialog();
//        Thread thread=new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String fileurl=new UrlManager(InputActivity.this).getFile_url();
//                HashMap<String,String> netparam=new HashMap<>();
//                Set<String> keyset= allInputView.keySet();
//                for (String key:keyset) {
//                    UploadBean uploadBean=allInputView.get(key).getUploadValue();
//                    String value=uploadBean.getValue();
//                    switch (uploadBean.getType()){
//                        case Global.IMAGE:
//                            value=uploadFileList(fileurl,value);
//                            netparam.put(key,value);
//                            break;
//                        case Global.VOICE:
//                            value=uploadFile(fileurl,value);
//                            netparam.put(key,value);
//                            break;
//                        case Global.MULTIMIDEA:
//                            try {
//                                JSONObject jsonObject=new JSONObject(value);
//                                String imagestr=uploadFileList(fileurl,jsonObject.optJSONObject(MultiMediaView.media_image).optString("value"));
//                                String video=uploadFile(fileurl,jsonObject.optJSONObject(MultiMediaView.media_video).optString("value"));
//                                String voice=uploadFile(fileurl,jsonObject.optJSONObject(MultiMediaView.media_voice).optString("value"));
//                                HashMap<String,String> mediaparam=new HashMap<>();
//                                mediaparam.put(MultiMediaView.media_image,imagestr);
//                                mediaparam.put(MultiMediaView.media_video,video);
//                                mediaparam.put(MultiMediaView.media_voice,voice);
//                                value=JSON.toJSONString(mediaparam);
//                            } catch (JSONException e) {
//                            }
//                            netparam.put(key,value);
//                            break;
//                         default:
//                             netparam.put(key,value);
//                             break;
//                    }
//                }
//                commitData(netparam);
//            }
//        });
//        thread.start();
//
//
//    }

    private void uploadValue() {
        Set<String> keyset=allInputView.keySet();
        for (String key:keyset) {
            if(!allInputView.get(key).checkUploadValue()){
                return;
            }
        }
        loaddingDialog.showDialog();
        Observable observable=Observable.from(allInputView.entrySet());
        final HashMap<String,String> netparam=new HashMap<>();
        final String fileurl=new UrlManager(InputActivity.this).getFile_url();
        Subscriber subscriber=new Subscriber<Map.Entry<String,InputView>>() {
            @Override
            public void onCompleted() {
                commitData(netparam);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Map.Entry<String,InputView> o) {
                UploadBean uploadBean=o.getValue().getUploadValue();
                String value=uploadBean.getValue();
                String key=o.getKey();
                switch (uploadBean.getType()){
                    case Global.IMAGE:
                        value=uploadFileList(fileurl,value);
                        try {
                            value= URLEncoder.encode(value,"utf-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        netparam.put(key,value);
                        break;
                    case Global.VOICE:
                        value=uploadFile(fileurl,value);
                        try {
                            value=URLEncoder.encode(value,"utf-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        netparam.put(key,value);
                        break;
                    case Global.MULTIMIDEA:
                        try {
                            JSONObject jsonObject=new JSONObject(value);
                            String imagestr=uploadFileList(fileurl,jsonObject.optJSONObject(MultiMediaView.media_image).optString("value"));
                            String video=uploadFile(fileurl,jsonObject.optJSONObject(MultiMediaView.media_video).optString("value"));
                            String voice=uploadFile(fileurl,jsonObject.optJSONObject(MultiMediaView.media_voice).optString("value"));

                            HashMap<String,String> mediaparam=new HashMap<>();
                            mediaparam.put(MultiMediaView.media_image,imagestr);
                            mediaparam.put(MultiMediaView.media_video,video);
                            mediaparam.put(MultiMediaView.media_voice,voice);
                            value=JSON.toJSONString(mediaparam);
                            try {
                                value=URLEncoder.encode(value,"utf-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        } catch (JSONException e) {
                        }
                        netparam.put(key,value);
                        break;
                    default:
                        netparam.put(key,value);
                        break;
                }
            }
        };
        observable.observeOn(Schedulers.newThread()).subscribe(subscriber);

    }

    private void commitData(HashMap<String, String> netparam) {

           OkhttpFactory  networkFactory=OkhttpFactory.getInstance();
           SuccessfulCallback successfulCallback=new SuccessfulCallback() {
               @Override
               public void success(String str) throws JSONException {
                   loaddingDialog.dismissAniDialog();
                     if(new CodeExceptionUtil(InputActivity.this).dealException(str)){
                         Toast.makeText(InputActivity.this,"上传成功",Toast.LENGTH_SHORT).show();
                         finish();
                     }
               }

               @Override
               public void success(InputStream ism, long conentLength) {

               }
           };
           FailCallback failCallback=new FailCallback() {
               @Override
               public void fail(String str) {
                 loaddingDialog.dismissAniDialog();
               }
           };
           UrlManager urlManager=new UrlManager(this);
           String commiturl="";
        if(uploadurl.contains("?")){
            commiturl=new StringBuilder(urlManager.getData_url()).append(uploadurl).append("&").append(urlManager.getExtraStr()).toString();
        }else {
            commiturl=new StringBuilder(urlManager.getData_url()).append(uploadurl).append("?").append(urlManager.getExtraStr()).toString();
        }

        String[] urlparts=commiturl.split("\\?");
        String uploadurl=urlparts[0];
        String[] urlparams=urlparts[1].split("&");
        for(int i=0;i<urlparams.length;i++){
            String[] earchparam=urlparams[i].split("=");
            if(earchparam.length==2){
                netparam.put(earchparam[0],earchparam[1]);
             }
        }
        try {
            networkFactory.postjson(uploadurl,JSON.toJSONString(netparam),successfulCallback,failCallback);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String uploadFileList(String fileurl, String value) {
        if(IsNullOrEmpty.isEmpty(value)||value.equals("[]")){
            return "";
        }
        List<String> filelist=JSON.parseArray(value,String.class);

        List<String> compressPic=new ArrayList<>();
        String compresspath= Environment.getExternalStorageDirectory().getAbsolutePath()+"/xiantecomress/";
        File comfile=new File(compresspath);
        if(!comfile.exists()){
            comfile.mkdirs();
        }
        for(int i=0;i<filelist.size();i++){
            try {
                int size= Utils.getBitmapSize(new File(filelist.get(i)));
                if(size<1024*1024){
                    compressPic.add(filelist.get(i));
                }else {
                    String destpic=compresspath+System.currentTimeMillis()+".jpg";
                    Utils.comress(filelist.get(i),destpic);
                    compressPic.add(destpic);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String result="";
        if(filelist!=null){
            result=OkhttpFactory.getInstance().execute_files(this,fileurl,compressPic,"inputfile");
            if(!IsNullOrEmpty.isEmpty(result)){
                try {
                    result=new JSONObject(result).optString("resultData");
                } catch (JSONException e) {
                    e.printStackTrace();
                    result="";
                }
            }
        }
        return result;
    }

    private String uploadFile(String fileurl, String value) {
        if(IsNullOrEmpty.isEmpty(value)){
            return "";
        }
           String result=OkhttpFactory.getInstance().executeFile(fileurl,value,this);
        if(!IsNullOrEmpty.isEmpty(result)){
            try {
                result=new JSONObject(result).optString("resultData");
            } catch (JSONException e) {
                e.printStackTrace();
                result="";
            }
        }
           return result;
    }

    private void getNetData(){
        inputItemBeanList=new ArrayList<>();

//        List<InputItemBean> kindone=new ArrayList<>();
//        kindone.add(new InputItemBean("time","时间",Global.TEXT,"请输入姓名"));
//
//
//        List<InputItemBean> kindt=new ArrayList<>();
//        kindt.add(new InputItemBean("FEGRR34F","房主信息1",Global.HOUSE_OWNER,"请输入姓名"));
//
//        List<InputItemBean> kindthres=new ArrayList<>();
//        kindthres.add(new InputItemBean("customer","租户信息",Global.RENTER,"请输入姓名"));
//
//                kindone.add(new InputItemBean("content","发布对象",Global.PUBLISH_TYPE,"请输入内容"));
//        kindone.add(new InputItemBean("myiage","",Global.LOCATION,""));
//
////        List<InputItemBean> kindtwo=new ArrayList<>();
////        kindtwo.add(new InputItemBean("nametwo","姓名",Global.TEXT,"请输入姓名"));
////        kindtwo.add(new InputItemBean("personiimage","姓名",Global.VIDEO,"请输入姓名"));
////        kindtwo.add(new InputItemBean("perimage","姓名",Global.IMAGE,"请输入姓名"));
////        kindtwo.add(new InputItemBean("peraddress","发现地址",Global.LOCATION,"请输入姓名"));
//
//        inputItemBeanList.add(kindone);
//        inputItemBeanList.add(kindt);
//        inputItemBeanList.add(kindthres);
//      //  inputItemBeanList.add(kindtwo);
//        inputActivityManager.updateInputView(inputItemBeanList);




        loaddingDialog.showDialog();
        NetworkFactory networkFactory= OkhttpFactory.getInstance();
        SuccessfulCallback successfulCallback=new SuccessfulCallback() {
            @Override
            public void success(String str) throws JSONException {
                if(new CodeExceptionUtil(InputActivity.this).dealException(str)){
                    uploadurl=new JSONObject(str).optJSONObject("resultData").optString("url");
                    JSONArray jsonArray=new JSONObject(str).optJSONObject("resultData").optJSONArray("data");
                    if(jsonArray!=null){
                     for(int i=0;i<jsonArray.length();i++){
                         inputItemBeanList.add(JSON.parseArray(jsonArray.getString(i),InputItemBean.class));
                     }
                 }
                    inputActivityManager.updateInputView(inputItemBeanList);
                }
                loaddingDialog.dismissAniDialog();
            }

            @Override
            public void success(InputStream ism, long conentLength) {
                loaddingDialog.dismissAniDialog();
            }
        };
        FailCallback failCallback=new FailCallback() {
            @Override
            public void fail(String str) {
               loaddingDialog.dismissAniDialog();
            }
        };

        UrlManager urlManager=new UrlManager(this);
        String listurl="";
        String url=clickEntity.getStrUrl();
        if(url.contains("?")){
            listurl=new StringBuilder(urlManager.getData_url()).append(url).append("&").append(urlManager.getExtraStr()).toString();
        }else {
            listurl=new StringBuilder(urlManager.getData_url()).append(url).append("?").append(urlManager.getExtraStr()).toString();
        }
        networkFactory.start(NetworkFactory.METHOD_GET,listurl,null,successfulCallback,failCallback);
}

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void dealInputViewUpdate(UpdateViewMessage updateViewMessage){
        allInputView.get(updateViewMessage.getInputKey()).updateInputView(updateViewMessage.getUpdateStr());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
      EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        CardInfo cardInfo = (CardInfo) data.getSerializableExtra("cardinfo");
        if(cardInfo!=null){
            String allinfor=cardInfo.getAllinfo();
            if(IsNullOrEmpty.isEmpty(allinfor)){
                return;
            }
            JSONObject jsonObject=new JSONObject();
                                try {
                            jsonObject.put("id",InputActivity.selectid);
                            jsonObject.put("value",allinfor);
                            allInputView.get(InputActivity.selectkey).updateInputView(jsonObject.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
