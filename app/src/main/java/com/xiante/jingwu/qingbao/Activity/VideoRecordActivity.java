package com.xiante.jingwu.qingbao.Activity;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xiante.jingwu.qingbao.CustomView.CommonView.BothWayProgressBar;
import com.xiante.jingwu.qingbao.MessageEvent.UpdateViewMessage;
import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.Global;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class VideoRecordActivity extends Activity implements SurfaceHolder.Callback, View.OnTouchListener, BothWayProgressBar.OnProgressEndListener {
    private static final String TAG = VideoRecordActivity.class.getCanonicalName();
    private static final int LISTENER_START = 200;
    private static final int MAX_RECORD_TIME=30*1000;
    //预览SurfaceView
    private SurfaceView mSurfaceView;
    private Camera mCamera;
    //底部"按住拍"按钮
    private View mStartButton;
    //进度条
    private BothWayProgressBar mProgressBar;
    //录制视频
    private MediaRecorder mMediaRecorder;
    private SurfaceHolder mSurfaceHolder;
    //屏幕分辨率
    private int videoWidth, videoHeight;
    //判断是否正在录制
    //段视频保存的目录
    private File mTargetFile;
    private String inputkey;
    private int view_id=0;
    @Override
    public void setFinishOnTouchOutside(boolean finish) {
        super.setFinishOnTouchOutside(finish);
    }

    //当前进度/时间
    private int mProgress;
    //录制最大时间
    public static final int MAX_TIME = 10;
    //是否上滑取消
    private boolean isCancel;
    //手势处理, 主要用于变焦 (双击放大缩小)
    private GestureDetector mDetector;
    //是否放大
    private boolean isZoomIn = false;

    public final static int HANDLER_PROGRESS = 1000;
    public final static int HANDLER_VIDEO_RECORD = 1001;
    private TextView mTvTip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_view_videorecorder);
        initView(this);
    }


    private void initView(Context context) {
        videoWidth = 640;
        videoHeight = 480;
        inputkey=getIntent().getStringExtra(Global.INPUTKEY);
        view_id=getIntent().getIntExtra(Global.VIEW_ID,0);
        mSurfaceView = findViewById(R.id.main_surface_view);

        mDetector = new GestureDetector(VideoRecordActivity.this, new ZoomGestureListener());
        /**
         * 单独处理mSurfaceView的双击事件
         */
        mSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mDetector.onTouchEvent(event);
                return true;
            }
        });

        mSurfaceHolder = mSurfaceView.getHolder();
        //设置屏幕分辨率
        mSurfaceHolder.setFixedSize(videoWidth, videoHeight);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSurfaceHolder.addCallback(VideoRecordActivity.this);
        mStartButton = findViewById(R.id.main_press_control);
        mTvTip = findViewById(R.id.main_tv_tip);

        mStartButton.setOnTouchListener(this);
        mProgressBar = findViewById(R.id.main_progress_bar);
        mProgressBar.setOnProgressEndListener(VideoRecordActivity.this);
        mProgressBar.setMaxProgresTime(MAX_RECORD_TIME);
        mMediaRecorder = new MediaRecorder();
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mSurfaceHolder = holder;
        startPreView(holder);
    }

    /**
     * 开启预览
     *
     * @param holder
     */
    private void startPreView(SurfaceHolder holder) {

        if (mCamera == null) {
            mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        }
        if (mMediaRecorder == null) {
            mMediaRecorder = new MediaRecorder();
        }
        if (mCamera != null) {
            mCamera.setDisplayOrientation(90);
            try {
                mCamera.setPreviewDisplay(holder);
                Camera.Parameters parameters = mCamera.getParameters();
                //实现Camera自动对焦
                List<String> focusModes = parameters.getSupportedFocusModes();
                if (focusModes != null) {
                    for (String mode : focusModes) {
                        mode.contains("continuous-video");
                        parameters.setFocusMode("continuous-video");
                    }
                }
                mCamera.setParameters(parameters);
                mCamera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }



    /**
     * 进度条结束后的回调方法
     */
    @Override
    public void onProgressEndListener() {
        //视频停止录制
        stopRecordSave();
    }

    /**
     * 开始录制
     */
    private void startRecord() {
        if (mMediaRecorder != null) {
            try {
                mMediaRecorder.reset();
                mCamera.unlock();
                mMediaRecorder.setCamera(mCamera);
                //从相机采集视频
                mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                // 从麦克采集音频信息
                mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
                // TODO: 2016/10/20  设置视频格式
                mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                CamcorderProfile profile= CamcorderProfile.get(CamcorderProfile.QUALITY_720P);
                mMediaRecorder.setVideoSize(profile.videoFrameWidth, profile.videoFrameHeight);
                //每秒的帧数
                mMediaRecorder.setVideoFrameRate(profile.videoFrameRate);
                //编码格式
                mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
                mMediaRecorder.setAudioEncoder(profile.audioCodec);
                // 设置帧频率，然后就清晰了
                // 设置帧频率，然后就清晰了
                mMediaRecorder.setVideoEncodingBitRate(900*1024);
                // TODO: 2016/10/20 临时写个文件地址, 稍候该!!!
                String fileName = Environment.getExternalStorageDirectory()+"/xiantevideo/";
                File tempfile=new File(fileName);
                if(!tempfile.exists()){
                    tempfile.mkdirs();
                }
                String filepathName = Environment.getExternalStorageDirectory()+"/xiantevideo/"+ System.currentTimeMillis()+".mp4";
                Log.d(TAG, "filepathName:" + filepathName);
                mTargetFile = new File(filepathName);

                mMediaRecorder.setOutputFile(mTargetFile.getAbsolutePath());
                mMediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
                //解决录制视频, 播放器横向问题
                mMediaRecorder.setOrientationHint(90);

                mMediaRecorder.prepare();
                //正式录制
                mMediaRecorder.start();
                mProgressBar.startCaculateProgress();
            } catch (Exception e) {
                Log.d(TAG, "VideoRecord:error" );
                e.printStackTrace();
            }

        }
    }

    /**
     * 停止录制 并且保存
     */
    private void stopRecordSave() {
            try {
                mMediaRecorder.setOnErrorListener(null);
                mMediaRecorder.setOnInfoListener(null);
                mMediaRecorder.setPreviewDisplay(null);
                mMediaRecorder.stop();
                mMediaRecorder.release();
            } catch (IllegalStateException e) {
            }catch (RuntimeException e) {
            }catch (Exception e) {
            }

            mMediaRecorder = null;
        JSONObject jsonObject=new JSONObject();
        if(mProgressBar.getCurrentTime()<1000){
            Toast.makeText(this,"录制时间太短，请重新录制",Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        try {
            jsonObject.put("id",view_id);
            jsonObject.put("value",mTargetFile.getAbsoluteFile());
        } catch (JSONException e) {
            e.printStackTrace();
        }

            EventBus.getDefault().post(new UpdateViewMessage(inputkey,jsonObject.toString()));
            finish();
    }

    /**
     * 停止录制, 不保存
     */
    private void stopRecordUnSave() {
            try {
                mMediaRecorder.setOnErrorListener(null);
                mMediaRecorder.setOnInfoListener(null);
                mMediaRecorder.setPreviewDisplay(null);
                mMediaRecorder.stop();
            } catch (IllegalStateException e) {
            }catch (RuntimeException e) {
            }catch (Exception e) {
            }

            mMediaRecorder.release();
            mMediaRecorder = null;
            if (mTargetFile.exists()) {
                //不保存直接删掉
                mTargetFile.delete();
            }
        finish();
    }

    /**
     * 相机变焦
     *
     * @param zoomValue
     */
    public void setZoom(int zoomValue) {
        if (mCamera != null) {
            Camera.Parameters parameters = mCamera.getParameters();
            if (parameters.isZoomSupported()) {//判断是否支持
                int maxZoom = parameters.getMaxZoom();
                if (maxZoom == 0) {
                    return;
                }
                if (zoomValue > maxZoom) {
                    zoomValue = maxZoom;
                }
                parameters.setZoom(zoomValue);
                mCamera.setParameters(parameters);
            }
        }

    }


    /**
     * 触摸事件的触发
     * @param v
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        boolean ret = false;
        int action = event.getAction();
        float ey = event.getY();
        float ex = event.getX();
        //只监听中间的按钮处
        int vW = v.getWidth();
        int left = LISTENER_START;
        int right = vW - LISTENER_START;

        isCancel = false;
        float downY = 0;

        switch (v.getId()) {
            case R.id.main_press_control: {
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        if (ex > left && ex < right) {
                            mProgressBar.setCancel(false);
//                            显示上滑取消
                            mTvTip.setVisibility(View.VISIBLE);
                            mTvTip.setText("上滑取消录制");
//                            记录按下的Y坐标
                            mProgressBar.setVisibility(View.VISIBLE);
                            startRecord();
                            ret = true;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (ey>0) {
                            mTvTip.setVisibility(View.INVISIBLE);
                            mProgressBar.setVisibility(View.INVISIBLE);
                            stopRecordSave();
                            ret = false;
                        }else {
                            stopRecordUnSave();
                        }
                        mProgressBar.stopCaculateProgress();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (ey<0) {
                            mProgressBar.setCancel(true);
                        }else {
                            mProgressBar.setCancel(false);
                        }
                        break;
                }
                break;
            }

        }
        return ret;
    }


    /**
     * 变焦手势处理类
     */
    class ZoomGestureListener extends GestureDetector.SimpleOnGestureListener {
        //双击手势事件
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            super.onDoubleTap(e);
            if (mMediaRecorder != null) {
                if (!isZoomIn) {
                    setZoom(20);
                    isZoomIn = true;
                } else {
                    setZoom(0);
                    isZoomIn = false;
                }
            }
            return true;
        }
    }

    @Override
    protected void onDestroy() {
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
        super.onDestroy();
    }
}
