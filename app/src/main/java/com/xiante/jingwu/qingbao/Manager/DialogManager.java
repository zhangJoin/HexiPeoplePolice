package com.xiante.jingwu.qingbao.Manager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.xiante.jingwu.qingbao.CustomView.CommonView.WaveView;
import com.xiante.jingwu.qingbao.MessageEvent.UpdateViewMessage;
import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.AudioRecorder2Mp3Util;
import com.xiante.jingwu.qingbao.Util.PermissionHelper;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import pl.droidsonroids.gif.GifImageView;


public class DialogManager implements AudioManager.AudioStageListener {

    private Dialog mDialog;
    private GifImageView imgBg;
    private Context mContext;
    //private AudioManager mAudioManager;
    private PermissionHelper mHelper;
    private WaveView voiceLineView;
    //当前录音时长
    private long mTime = 0;
    // 是否触发了onlongclick
    private boolean mReady;
    //标记是否强制终止
    private boolean isOverTime = false;
    //最大录音时长（单位:s）。def:60s
    private int mMaxRecordTime = 60;
    //三个对话框的状态常量
    private static final int STATE_NORMAL = 1;
    private static final int STATE_RECORDING = 2;
    private static final int STATE_WANT_TO_CANCEL = 3;

    //取消录音的状态值
    private static final int MSG_VOICE_STOP = 4;
    //当前状态
    private int mCurrentState = STATE_NORMAL;
    // 正在录音标记
    private boolean isRecording = false;
    // 状态
    private static final int MSG_AUDIO_PREPARED = 0X110;
    private static final int MSG_VOICE_CHANGE = 0X111;
    private static final int VOICE_CHAGE=0X869;
    public  static String path="";
    private String inputkey="";
    private String id="";
     AudioRecorder2Mp3Util recorder2Mp3Util;

     Timer recordTimer;

    @SuppressLint("HandlerLeak")
    private Handler mStateHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_AUDIO_PREPARED:
                    // 显示应该是在audio end prepare之后回调
                    if (mDialog != null) {
                        mDialog.show();
                        isRecording = true;
                        if (mDialog != null && mDialog.isShowing()) {
                                imgBg.setImageResource( R.drawable.voice1 );
                        }
                        new Thread(mGetVoiceLevelRunnable).start();
                    }
                    break;
                case MSG_VOICE_CHANGE:
                    showRemainedTime();
                    break;
                case MSG_VOICE_STOP:
                    isOverTime = true;//超时
                    dimissDialog();
                    reset();// 恢复标志位
                    break;
                case VOICE_CHAGE:
                    voiceLineView.setWaveChange(msg.arg1);
                    break;
            }
        }

    };

    public String getRecordPath(){
       return recorder2Mp3Util.getMp3Path();
    }


    public DialogManager(Context context) {
        mContext = context;
    }

    @SuppressLint("ClickableViewAccessibility")
    public void showRecordingDialog(String inputkey,String id) {
        this.inputkey=inputkey;
        this.id=id;
        mDialog = new Dialog(mContext, R.style.Theme_audioDialog);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.dialog_manager, null);
        mDialog.setContentView(view);
        imgBg = view.findViewById(R.id.dm_rl_bg);
        voiceLineView=view.findViewById(R.id.voiceLineView);
        mHelper=new PermissionHelper(mContext);
        //实例化录音核心类
        recorder2Mp3Util=new AudioRecorder2Mp3Util(mContext);
        recorder2Mp3Util.setVolumeListener(new AudioRecorder2Mp3Util.VolumeListener() {
            @Override
            public void onVolumeChange(double volume) {
                if(volume==Double.POSITIVE_INFINITY||volume==Double.NEGATIVE_INFINITY){
                    return;
                }
                double percent=volume/100;
                voiceLineView.setWaveChange((int) (voiceLineView.getHeight()*percent));
            }
        });
        mDialog.show();
        imgBg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        mHelper.requestPermissions("请授予[录音]，[读写]权限，否则无法录音", new PermissionHelper.PermissionListener() {
                            @Override
                            public void doAfterGrand(String... permission) {
                                recorder2Mp3Util.startRecording();
                                mTime=System.currentTimeMillis();
                                imgBg.setImageResource( R.drawable.voice1 );
                                startCountTime();
                            }
                            @Override
                            public void doAfterDenied(String... permission) {
                                Toast.makeText(mContext, "请授权,否则无法录音", Toast.LENGTH_SHORT).show();
                            }
                        }, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        changeState(STATE_RECORDING);
                        break;
                    case MotionEvent.ACTION_UP:
                        closeCountTime();
                      long   duration=System.currentTimeMillis()-mTime;
                        if ( duration < 1000) {
                            imgBg.setImageResource( R.drawable.image_voice );
                            Toast.makeText(mContext, "时间太短,请重新录制", Toast.LENGTH_SHORT).show();
                            mDialog.dismiss();
                            recorder2Mp3Util.stopRecord();
                        } else {
                            //正常录制结束
                            dimissDialog();
                        }
                        break;
                }
                return true;
            }
        });
    }

    private void startCountTime(){
        recordTimer=new Timer();
        recordTimer.schedule(new TimerTask() {
            @Override
            public void run() {
               Message message=mStateHandler.obtainMessage();
               message.what=MSG_VOICE_CHANGE;
               mStateHandler.sendMessage(message);
            }
        },1000,1000);
    }

    private void closeCountTime(){
        recordTimer.cancel();
        recordTimer=null;
    }

    // 隐藏dialog
    public void dimissDialog() {
           recorder2Mp3Util.stopRecordingAndConvertFile();
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("value",recorder2Mp3Util.getMp3Path());
            jsonObject.put("id",id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        UpdateViewMessage updateViewMessage=new UpdateViewMessage(inputkey,jsonObject.toString());
        EventBus.getDefault().post(updateViewMessage);
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }

    }



    @Override
    public void wellPrepared() {
        mStateHandler.sendEmptyMessage(MSG_AUDIO_PREPARED);
    }

    // 获取音量大小的runnable
    private Runnable mGetVoiceLevelRunnable = new Runnable() {

        @Override
        public void run() {
            while (isRecording) {
                try {
                    //最长mMaxRecordTimes
                    if (mTime > mMaxRecordTime) {
                        mStateHandler.sendEmptyMessage(MSG_VOICE_STOP);
                        return;
                    }
                    Thread.sleep(100);
                    mTime += 0.1f;
                    mStateHandler.sendEmptyMessage(MSG_VOICE_CHANGE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    private void showRemainedTime() {
       long recordTime=System.currentTimeMillis()-mTime;
       if(recordTime>mMaxRecordTime*1000){
           closeCountTime();
           dimissDialog();
       }
    }

    /*
     * 想设置震动大小可以通过改变pattern来设定，如果开启时间太短，震动效果可能感觉不到
     * */
    private void doShock() {
        Vibrator mVibrator;
        mVibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {100, 400, 100, 400};   // 停止 开启 停止 开启
        assert mVibrator != null;
        mVibrator.vibrate(pattern, -1);           //重复两次上面的pattern 如果只想震动一次，index设为-1
    }

    /**
     * 回复标志位以及状态
     */
    private void reset() {
        isRecording = false;
        changeState(STATE_NORMAL);
        mReady = false;
        mTime = 0;
        isOverTime = false;
    }

    private void changeState(int state) {
        if (mCurrentState != state) {
            mCurrentState = state;
        }
    }

}
