package com.xiante.jingwu.qingbao.Activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.MediaController;

import com.xiante.jingwu.qingbao.CustomView.VideoViewFullScreen;
import com.xiante.jingwu.qingbao.R;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.util.List;

/**
 * @author 郑靖廷 Yoda_T
 * @email 17600203706@163.com
 * @time $date$ $time$
 */
public class VideoPlayActivity extends BaseActivity {
    private VideoViewFullScreen mVideoView;
    private String mPath = "";
    private int currentPosition=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_layout);
        initView();
        initData();
    }

    @Override
    public void initView() {
        this.initTitlebar("视频播放", "", "");
        mVideoView = findViewById(R.id.view_video);
    }

    @Override
    public void initData() {
        //Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video_20180205_155023);
        mPath=getIntent().getStringExtra("videopath");
        mVideoView.setMediaController(new MediaController(this,false));
        mVideoView.setVideoPath(mPath);
        //mVideoView.setVideoURI(uri);
        mVideoView.requestFocus();
        AndPermission.with(VideoPlayActivity.this)
                .permission(Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE)
                .onGranted(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        mVideoView.start();
                    }
                })
                .onDenied(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                    }
                }).start();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(currentPosition!=0){
            mVideoView.seekTo(currentPosition);
            mVideoView.start();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        currentPosition=mVideoView.getCurrentPosition();
    }

    @Override
    public void initListener() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVideoView.isPlaying()) {
            currentPosition=0;
            mVideoView.stopPlayback();
        }
    }
}
