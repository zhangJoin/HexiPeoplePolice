package com.xiante.jingwu.qingbao.Dialog;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xiante.jingwu.qingbao.Manager.MediaManager;
import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.PermissionHelper;

import pl.droidsonroids.gif.GifImageView;

/**
 * @author 郑靖廷 Yoda_T
 * @email 17600203706@163.com
 * @time $date$ $time$
 */
public class PlayRecodeDialog extends Dialog{
    private PlayRecodeDialog mPlayRecodeDialog;
    private GifImageView mGifImageView;
    private TextView mPlay;
    private PermissionHelper mHelper;
    private String mPath="";
    private Context context;
    private boolean isPlay = false;
    public PlayRecodeDialog(@NonNull Context context) {
        super(context,R.style.Theme_audioDialog);
        init(context);
    }

    public PlayRecodeDialog(@NonNull Context context, String playPath) {
        super(context,R.style.Theme_audioDialog);
        this.context=context;
        this.mPath=playPath;
        init(context);
    }

    public PlayRecodeDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init(context);
    }

    private void init(final Context context) {
          setContentView(R.layout.playrecode_dialog);
          mGifImageView=findViewById(R.id.iv_bg);
          mPlay=findViewById(R.id.tv_play);
          mHelper=new PermissionHelper(context);
          mGifImageView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  if(isPlay){
                      isPlay=false;
                      mGifImageView.setImageResource(R.drawable.image_voice);
                      MediaManager.pause();
                      return;
                  }else{
                      isPlay=true;
                  }
                  if (!TextUtils.isEmpty(mPath)){
                     isPlay=true;
                     MediaManager.release();
                     mHelper.requestPermissions("[读写]权限，否则无法播放录音", new PermissionHelper.PermissionListener() {
                         @Override
                         public void doAfterGrand(String... permission) {
                             mGifImageView.setImageResource(R.drawable.voice1);
                             MediaManager.playSound(mPath, new MediaPlayer.OnCompletionListener() {
                                 @Override
                                 public void onCompletion(MediaPlayer mp) {
                                     isPlay = false;
                                     dismiss();
                                 }
                             });
                         }

                         @Override
                         public void doAfterDenied(String... permission) {
                             Toast.makeText(context, "请授权,否则无法播放录音", Toast.LENGTH_SHORT).show();
                         }
                     }, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE);
                  }
              }
          });

          setOnCancelListener(new OnCancelListener() {
              @Override
              public void onCancel(DialogInterface dialog) {
                  MediaManager.release();
              }
          });
    }
}
