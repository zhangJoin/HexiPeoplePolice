package com.xiante.jingwu.qingbao.Activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.xiante.jingwu.qingbao.R;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.io.IOException;
import java.util.List;

/**
 * @author 郑靖廷 Yoda_T
 * @email 17600203706@163.com
 * @time $date$ $time$
 */
public class VideoActivity extends BaseActivity  {
    private SurfaceView mSurfaceView;
    private MediaPlayer mediaPlayer;
    SurfaceHolder mSurfaceHolder;
   // private SeekBar seekBar;
    Handler handler=new Handler();
    private String videopath="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.surfacelayout);
        initView();
        initData();
        initListener();
    }

    @Override
    public void initView() {
        videopath=getIntent().getStringExtra("videopath");
        mSurfaceView=findViewById(R.id.surfaceView);
        //进度条
     //   seekBar = findViewById(R.id.seekBar);
        mSurfaceHolder = mSurfaceView.getHolder();
        mediaPlayer=new MediaPlayer();
      //  seekBar.setMax(mediaPlayer.getDuration());
      //  seekBar.setProgress(0);
//        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                if (fromUser)
//                    mediaPlayer.seekTo(progress);
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//                 mediaPlayer.pause();
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                mediaPlayer.start();
//            }
//        });

        mSurfaceHolder=mSurfaceView.getHolder();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                AndPermission.with(VideoActivity.this)
                        .permission(Permission.CAMERA, Permission.READ_EXTERNAL_STORAGE)
                        .onGranted(new Action() {
                            @Override
                            public void onAction(List<String> permissions) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mediaPlayer.start();
                                      //  handler.post(updatesb);
                                    }
                                });
                            }
                        })
                        .onDenied(new Action() {
                            @Override
                            public void onAction(List<String> permissions) {
                                Uri packageURI = Uri.parse("package:" + getPackageName());
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                startActivity(intent);
                                Toast.makeText(VideoActivity.this, "没有权限无法扫描呦", Toast.LENGTH_LONG).show();
                            }
                        }).start();
            }
        });
    }
//    Runnable updatesb=new Runnable() {
//        @Override
//        public void run() {
//           // seekBar.setProgress(mediaPlayer.getCurrentPosition());
//            handler.postDelayed(updatesb, 1000);
//        }
//    };
    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            //当SurfaceView中Surface创建时回掉
            //该方法表示Surface已经创建完成，可以在该方法中进行绘图操作
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mediaPlayer.reset();
                try {
                    //设置视屏文件图像的显示参数
                    mediaPlayer.setDisplay(holder);

                    //file.getAbsolutePath()本地视频
                    //uri 网络视频
                    mediaPlayer.setDataSource(videopath);
//                    mediaPlayer.setDataSource(VideoActivity.this, Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video_20180205_155023));
                    //prepare();表示准备工作同步进行，（准备工作在UI线程中进行）
                    //当播放网络视频时，如果网络不要 会报ARN 所以不采用该方法
                    //mediaPlayer.prepare();
                    //异步准备 准备工作在子线程中进行 当播放网络视频时候一般采用此方法
                    mediaPlayer.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //当SurfaceView的大小发生改变时候触发该方法
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }
            //Surface销毁时回掉
            //当Surface销毁时候，同时把MediaPlayer也销毁
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
//                if (mediaPlayer!=null) {
//                    mediaPlayer.stop();
//                    //释放资源
//                    mediaPlayer.release();
//                }
            }
        });
        //设置 surfaceView点击监听
        mSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.pause();
                        } else {
                            mediaPlayer.start();
                        }
                        break;
                }
                //返回True代表事件已经处理了
                return true;
            }
        });

    }
   public void onClick(View v){
        switch (v.getId()){
            case R.id.play_bt:
                AndPermission.with(VideoActivity.this)
                        .permission(Permission.CAMERA, Permission.READ_EXTERNAL_STORAGE)
                        .onGranted(new Action() {
                            @Override
                            public void onAction(List<String> permissions) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mediaPlayer.start();
                                       // handler.post(updatesb);
                                    }
                                });
                            }
                        })
                        .onDenied(new Action() {
                            @Override
                            public void onAction(List<String> permissions) {
                                Uri packageURI = Uri.parse("package:" + getPackageName());
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                startActivity(intent);
                                Toast.makeText(VideoActivity.this, "没有权限无法扫描呦", Toast.LENGTH_LONG).show();
                            }
                        }).start();
                break;
//            case R.id.pause_bt:
//                if (mediaPlayer.isPlaying()){
//                    mediaPlayer.pause();
//                }
//                break;
        }
   }

    @Override
    protected void onStop() {

        if (mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        super.onStop();
   }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
