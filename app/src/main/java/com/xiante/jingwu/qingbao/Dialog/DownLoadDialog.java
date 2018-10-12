package com.xiante.jingwu.qingbao.Dialog;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eallnetwork.framework.FailCallback;
import com.example.eallnetwork.framework.SuccessfulCallback;
import com.example.eallnetwork.workUtils.OkhttpFactory;
import com.xiante.jingwu.qingbao.CustomView.CustomProgressBar;
import com.xiante.jingwu.qingbao.Manager.ActivityManager;
import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.Global;
import com.xiante.jingwu.qingbao.Util.Utils;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 下载弹框显示
 */
public class DownLoadDialog extends Dialog {
    private CustomProgressBar pbDownload;
    private TextView tvProgressShow;
    private Button btDownCancel;
    private Button btDownConform;
    private InputStream inputStream;
    private long mLength;
    private int progress = 0;
    private Context context;
    private boolean cancelUpdate = false;
    public final int DOWNLOAD = 01;
    public final int DOWNLOAD_FINISH = 02;
    public String mPath = "";
     public static final  String APK_FILE="TEMPLE_IMAGE";
     public static final int INSTALL_REQUEST_CODE=0x68;
    private String apkfilepath;
    private String updateMust;
    public DownLoadDialog(@NonNull Context context) {
        super(context, R.style.loadingdialog);
        init(context);
    }

    public DownLoadDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init(context);
    }

    public DownLoadDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    public DownLoadDialog(Context context, InputStream ism, long conentLength) {
        super(context);
        this.mLength = conentLength;
        this.inputStream = ism;
        this.context = context;
        init(context);
    }

    private void init(final Context context) {
        this.context=context;
        setContentView(R.layout.downdialoglayout);
        mPath = context.getSharedPreferences(Global.APP_URL, Context.MODE_PRIVATE).getString(Global.APP_URL, "");
        updateMust = context.getSharedPreferences(Global.UPDATE_MUSE, Context.MODE_PRIVATE).getString(Global.UPDATE_MUSE, "");
        pbDownload = findViewById(R.id.pb_download);
        tvProgressShow = findViewById(R.id.tv_progress_show);
        btDownCancel = findViewById(R.id.bt_down_cancel);
        btDownConform = findViewById(R.id.bt_down_conform);
        btDownCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelUpdate = false;
                if ("1".equals(updateMust)) {
                    ActivityManager.getInstance().finishAll();
                } else {
                    dismiss();
                }
            }
        });
        btDownConform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btDownConform.setVisibility(View.GONE);
                boolean isSuccess = Utils.isSuccess(context);
                if (!isSuccess) {
                    Toast.makeText(context, context.getString(R.string.netError), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(mPath!=null){
                    getFile();
                }
//                Message message=mHandler.obtainMessage();
//                message.obj=Environment.getExternalStorageDirectory()+"/TEMPLE_IMAGE/xxy.apk";
//                message.what=DOWNLOAD_FINISH;
//                mHandler.sendMessage(message);
            }
        });
    }

    private void getFile() {
        boolean isSuccess = Utils.isSuccess(getContext());
        if (!isSuccess) {
            Toast.makeText(getContext(), getContext().getString(R.string.netError), Toast.LENGTH_SHORT).show();
            return;
        }
        OkhttpFactory mNetworkFactory = OkhttpFactory.getInstance();
        SuccessfulCallback mSuccessfulCallback = new SuccessfulCallback() {
            @Override
            public void success(String str) throws JSONException {

            }

            @Override
            public void success(final InputStream ism, final long conentLength) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        saveFile(ism, conentLength);
                    }
                }).start();
            }
        };
        FailCallback mFailCallback = new FailCallback() {
            @Override
            public void fail(String str) {
                Log.i("urlfail", str);
            }
        };
        mNetworkFactory.downloadFile(mPath, mSuccessfulCallback
                , mFailCallback);
    }

    private String mSavePath;

    private void saveFile(InputStream inputStream, long mLength) {
        String sdpath = Environment.getExternalStorageDirectory() + "/";
        mSavePath = sdpath + "TEMPLE_IMAGE";
        File mFile = new File(mSavePath);
        if (!mFile.exists()) {
            mFile.mkdirs();
        }
        FileOutputStream fos = null;
        apkfilepath=mFile.getAbsolutePath() + "/hxmj.apk";
        try {
            fos = new FileOutputStream(apkfilepath);
            int count = 0;
            // 缓存
            byte buf[] = new byte[1024 * 50];
            do {
                int numRead = 0;
                try {
                    numRead = inputStream.read(buf);
                    count += numRead;
                    // 计算进度条位置
                    progress = (int) (((float) count / mLength) * 100);
                    // 更新进度
                    mHandler.sendEmptyMessage(DOWNLOAD);
                    if (numRead <= 0) {
                        // 下载完成
                        Message message=mHandler.obtainMessage();
                        message.obj=apkfilepath;
                        message.what=DOWNLOAD_FINISH;
                        mHandler.sendMessage(message);
                        break;
                    }
                    // 写入文件
                    fos.write(buf, 0, numRead);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } while (!cancelUpdate);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DOWNLOAD:
                    pbDownload.setProgress(progress * 1.0f / 100);
                    tvProgressShow.setText(String.valueOf(progress + "%"));
                    break;
                case DOWNLOAD_FINISH:
                    dismiss();
                    //安装apk
                    installationApk();
                    break;
            }
        }
    };

    private void installationApk() {
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Utils.getFileUri(context,apkfilepath,"com.esint.pahx.messenger.hHXMJFileProvider"), "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (Build.VERSION.SDK_INT >= 26 && context.getPackageManager().canRequestPackageInstalls()) {
                            context.startActivity(intent);
        }
        else {
                ((Activity) context).startActivityForResult(intent, INSTALL_REQUEST_CODE);
        }
    }
    public void requestInstall(){
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Utils.getFileUri(context,apkfilepath,"com.esint.pahx.messenger.hHXMJFileProvider"), "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if( Build.VERSION.SDK_INT >= 26&&context.getPackageManager().canRequestPackageInstalls()){
            context.startActivity(intent);
        } else {
            ((Activity)context).startActivityForResult(intent, INSTALL_REQUEST_CODE);
        }
    }

}
