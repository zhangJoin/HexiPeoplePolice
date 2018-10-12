package com.xiante.jingwu.qingbao.CustomView;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.xiante.jingwu.qingbao.Activity.AlbumsActivity;
import com.xiante.jingwu.qingbao.Activity.VideoRecordActivity;
import com.xiante.jingwu.qingbao.Bean.Input.InputItemBean;
import com.xiante.jingwu.qingbao.Bean.Input.UploadBean;
import com.xiante.jingwu.qingbao.Manager.DialogManager;
import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.Global;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zhong on 2018/4/26.
 */

public class MultiMediaView extends LinearLayout implements InputView {
    private Context context;
    private View goSelectImageView,goSelectVideoView,goSelectVoiceView;
    private int imageid= R.id.mediaImageDisplay,videoid= R.id.mediaVideoDisplay,voiceid=R.id.mediaVoiceDisplay;
    private InputView mediaImageDisplay,mediaVideoDisplay,mediaVoiceDisplay;
    private InputItemBean inputItemBean;
    public static final  String media_image="media_image",media_video="media_video",media_voice="media_voice";
    public MultiMediaView(Context context) {
        super(context);
        initView(context);
    }

    public MultiMediaView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MultiMediaView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(final Context context){
        View.inflate(context,R.layout.multimedialayout,this);
        this.context=context;
        goSelectImageView=findViewById(R.id.multiMedia_image);
        goSelectVideoView=findViewById(R.id.multiMedia_video);
        goSelectVoiceView=findViewById(R.id.multiMedia_voice);
        mediaImageDisplay=findViewById(imageid);
        mediaVideoDisplay=findViewById(videoid);
        mediaVoiceDisplay=findViewById(voiceid);
        goSelectImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, AlbumsActivity.class);
                intent.putExtra("maxSelect",Integer.parseInt(inputItemBean.getImageNum())-((MultiMediaGridView)mediaImageDisplay).getSelectSize()+"");
                intent.putExtra(Global.INPUTKEY,inputItemBean.getStrField());
                intent.putExtra(Global.VIEW_ID,imageid);
                context.startActivity(intent);
            }
        });
        goSelectVideoView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AndPermission.with(getContext())
                        .permission(Permission.CAMERA,Permission.RECORD_AUDIO)
                        .onGranted(new Action() {
                            @Override
                            public void onAction(List<String> permissions) {
                                Intent intent=new Intent(context, VideoRecordActivity.class);
                                intent.putExtra(Global.INPUTKEY,inputItemBean.getStrField());
                                intent.putExtra(Global.VIEW_ID,videoid);
                                context.startActivity(intent);
                            }
                        })
                        .onDenied(new Action() {
                            @Override
                            public void onAction(List<String> permissions) {
                                Toast.makeText(getContext(),"请开启录制视频权限",Toast.LENGTH_SHORT).show();
                            }
                        }).start();

            }
        });

        goSelectVoiceView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AndPermission.with(getContext())
                        .permission(Permission.CAMERA,Permission.RECORD_AUDIO)
                        .onGranted(new Action() {
                            @Override
                            public void onAction(List<String> permissions) {
                                DialogManager dialogManager=new DialogManager(context);
                                dialogManager.showRecordingDialog(inputItemBean.getStrField(),voiceid+"");
                            }
                        })
                        .onDenied(new Action() {
                            @Override
                            public void onAction(List<String> permissions) {
                                Toast.makeText(getContext(),"请开启录制音频权限",Toast.LENGTH_SHORT).show();
                            }
                        }).start();
            }
        });

    }


    /*
* 这个控件上传的key以下面这个方法中meadiaMap为准，固定死，不要后台传递过来
* 后台传过来的key只当做定位控件用
*
* */
    @Override
    public UploadBean getUploadValue() {
        HashMap<String,UploadBean> meadiaMap=new HashMap<>();
        meadiaMap.put(media_image,mediaImageDisplay.getUploadValue());
        meadiaMap.put(media_video,mediaVideoDisplay.getUploadValue());
        meadiaMap.put(media_voice,mediaVoiceDisplay.getUploadValue());
        String value= JSON.toJSONString(meadiaMap);
        return new UploadBean(Global.MULTIMIDEA,value);
    }

    @Override
    public boolean checkUploadValue() {
        return true;
    }

    @Override
    public void initInputView(InputItemBean inputItemBean) {
        this.inputItemBean=inputItemBean;
        mediaVoiceDisplay.initInputView(inputItemBean);
        mediaVideoDisplay.initInputView(inputItemBean);
        mediaImageDisplay.initInputView(inputItemBean);
    }
    //{"value":filepath,"id":sdf}
    @Override
    public void updateInputView(String string) {
        try {
            JSONObject jsonObject=new JSONObject(string);
            int id=jsonObject.optInt("id");
            String value=jsonObject.optString("value");
            InputView inputView=findViewById(id);
            if(inputView!=null){
                inputView.updateInputView(value);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
