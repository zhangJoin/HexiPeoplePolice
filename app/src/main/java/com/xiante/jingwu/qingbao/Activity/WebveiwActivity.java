package com.xiante.jingwu.qingbao.Activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.idcard.CardInfo;
import com.idcard.TRECAPIImpl;
import com.idcard.TStatus;
import com.idcard.TengineID;
import com.turui.bank.ocr.CaptureActivity;
import com.xiante.jingwu.qingbao.Bean.Common.ClickEntity;
import com.xiante.jingwu.qingbao.Dialog.LoaddingDialog;
import com.xiante.jingwu.qingbao.NetWork.UrlManager;
import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.Global;
import com.xiante.jingwu.qingbao.Util.IsNullOrEmpty;
import com.xiante.jingwu.qingbao.Util.Utils;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.List;

import cn.sharesdk.onekeyshare.OnekeyShare;

import static android.view.KeyEvent.KEYCODE_BACK;

/**
 * Created by zhong on 2018/4/24.
 */

@SuppressWarnings("ALL")
public class WebveiwActivity extends BaseActivity  {
    LinearLayout rootview;
    ProgressBar progressBar;
    WebView webView;
    String url;
    ClickEntity clickEntity;
    private TRECAPIImpl engineDemo;
    String guid;
    LoaddingDialog loaddingDialog;
    private String icon;
    TextView webCloseTV;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webviewlayout);
        initView();
        initData();
        initListener();
    }

    @Override
    public void initView() {
        rootview = findViewById(R.id.rootview);
        webView = findViewById(R.id.webview);
        progressBar=findViewById(R.id.progressbar);
        webCloseTV=findViewById(R.id.deletewebTV);
        loaddingDialog=new LoaddingDialog(this);
        progressBar.setMax(100);
        initTitlebar("", "", "");
        url = getIntent().getStringExtra("url");
        guid=getIntent().getStringExtra("id");
        clickEntity = (ClickEntity) getIntent().getSerializableExtra(Global.CLICK_ACTION);
        if (clickEntity != null&&!IsNullOrEmpty.isEmpty(clickEntity.getStrType())) {
            if(!IsNullOrEmpty.isEmpty(clickEntity.getStrText())){
                rightTextView.setText(clickEntity.getStrText());
                rightTextView.setVisibility(View.VISIBLE);
                rightImageView.setVisibility(View.GONE);
            }
            if(!IsNullOrEmpty.isEmpty(clickEntity.getStrIco())){
                Glide.with(this).load(clickEntity.getStrIco()).into(rightImageView);
                rightImageView.setVisibility(View.VISIBLE);
                rightTextView.setVisibility(View.GONE);
            }
        }
        //首页上报信息类按钮传来只有clickentity
        if (url == null && clickEntity != null) {
            UrlManager urlManager = new UrlManager(this);
            if (clickEntity.getStrUrl().contains("?")) {
                url = new StringBuilder(urlManager.getData_url()).append(clickEntity.getStrUrl()).append("&")
                        .append(urlManager.getExtraStr()).toString();
            } else {
                url = new StringBuilder(urlManager.getData_url()).append(clickEntity.getStrUrl()).append("?")
                        .append(urlManager.getExtraStr()).toString();
            }

        }
    }

    @Override
    public void initData() {
        //列表页会传一个完整的url过来
        WebSettings webSettings = webView.getSettings();
        // 设置与Js交互的权限
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);// 设置webview推荐使用的窗口
        webSettings.setLoadWithOverviewMode(true);// 设置webview加载的页面的模式
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        // 设置允许JS弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webView.setWebViewClient(new MyWebViewClient());
        webView.addJavascriptInterface(this,"appjs");
        rightImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSuccess = Utils.isSuccess(WebveiwActivity.this);
                if (!isSuccess) {
                    Toast.makeText(WebveiwActivity.this, getString(R.string.netError), Toast.LENGTH_SHORT).show();
                }
                if(clickEntity==null){
                    return;
                }
                if(clickEntity.getStrType().equals("share")){
                    showShare();
                }
            }
        });
        rightTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSuccess = Utils.isSuccess(WebveiwActivity.this);
                if (!isSuccess) {
                    Toast.makeText(WebveiwActivity.this, getString(R.string.netError), Toast.LENGTH_SHORT).show();
                }
                if(clickEntity==null){
                    return;
                }
                if(clickEntity.getStrType().equals("share")){
                    showShare();
                }
            }
        });



        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
                if(newProgress>=100){
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder b = new AlertDialog.Builder(WebveiwActivity.this);
                b.setTitle("");
                b.setMessage(message);
                b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
                b.setCancelable(false);
                b.create().show();
                return true;
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                     titleTextView.setText(title);
            }
        });

        webView.loadUrl(url);
       //webView.loadUrl("http://192.168.100.130:8989/JinMen/views/h5/test/index.html");
        engineDemo = new TRECAPIImpl();
        TStatus tStatus = engineDemo.TR_StartUP(this,engineDemo.TR_GetEngineTimeKey());
        if (tStatus == TStatus.TR_TIME_OUT ) {
            Toast.makeText(this, "引擎过期", Toast.LENGTH_SHORT).show();
        }
        else  if (tStatus == TStatus.TR_FAIL) {
            Toast.makeText(this, "引擎初始化失败", Toast.LENGTH_SHORT).show();
        }
    }



    private void showShare() {
        saveico();
        OnekeyShare oks = new OnekeyShare();
        // 构造一个图标
        Bitmap enableLogo = BitmapFactory.decodeResource(WebveiwActivity.this.getResources(), R.mipmap.ssdk_oks_classic_line);
        String label = "复制链接";
        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", url);
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
            }
        };
        oks.setCustomerLogo(enableLogo, label, listener);
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，微信、QQ和QQ空间等平台使用
        oks.setTitle(webView.getTitle());
        // titleUrl QQ和QQ空间跳转链接
        oks.setTitleUrl(url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText("新闻摘要");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath(icon);//确保SDcard下面存在此张图片
        // url在微信、微博，Facebook等平台中使用
        oks.setUrl(url);
//        String iconurl=new StringBuilder(new UrlManager(this).getData_url()).append("views/image/hxmj.png").toString();
//        oks.setImageUrl(iconurl);
        // comment是我对这条分享的评论，仅在人人网使用
//        oks.setComment("我是测试评论文本");
        // 启动分享GUI
        oks.show(this);
    }

    @Override
    public void initListener() {
        webCloseTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( webView.canGoBack()) {
                    webCloseTV.setVisibility(View.GONE);
                    webView.goBack();
                }else {
                    finish();
                }
            }
        });
    }
    public void saveico(){
        Bitmap b=BitmapFactory.decodeResource(getResources(),R.drawable.appicon);
        File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/app/");
        if(!file.exists()){
            file.mkdirs();
        }
        icon=file.getAbsolutePath()+"/hxmjapp.png";
        File appicon=new File(icon);
        if(appicon.exists()){
            return;
        }
        try {
            b.compress(Bitmap.CompressFormat.PNG,100,new FileOutputStream(icon));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {//网页页面开始加载的时候
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {//网页加载结束的时候
            super.onPageFinished(view, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) { //网页加载时的连接的网址
            webCloseTV.setVisibility(View.VISIBLE);
            view.loadUrl(url);
            return true;
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            WebResourceResponse response = null;
            if(url.startsWith("apppjs")){
              //  interceptWebviewUrl(url);
                return null;
            }
            return response;
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            WebResourceResponse response = null;
            if(request.getUrl().toString().startsWith("appjs")){
               // interceptWebviewUrl(request.getUrl().toString());
                return null;
            }
            return response;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KEYCODE_BACK) && webView.canGoBack()) {
            webCloseTV.setVisibility(View.GONE);
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
        webView.resumeTimers();
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
        webView.pauseTimers();
    }


    @Override
    protected void onStop() {
        super.onStop();
        webView.loadUrl("javascript:stopPageAV()");
    }

    public void interceptWebviewUrl(String interceptUrl){
        String[] parsts=interceptUrl.split("\\?");
        String name=parsts[0].split(":")[1];
        if(name.equals("startScan")){
            startScan();
        }else if(name.equals("setPageTitle")){
            String title=parsts[1].split("=")[1];
            setPageTitle(title);
        }else if(name.equals("finishActivity")){
            finishActivity();
        }
    }



    @JavascriptInterface
    public void openAlbum(){

    };

    @JavascriptInterface
    public void showToast(final String infor){
       //弹出消息提示
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(WebveiwActivity.this,infor,Toast.LENGTH_SHORT).show();
            }
        });

    };

    @JavascriptInterface
    public void showLoadingDialog(){
      //弹出加载内容的dialog
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loaddingDialog.showDialog();
            }
        });

    };

    @JavascriptInterface
    public void closeLoadingDialog(){
//关闭加载内容的dialog
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loaddingDialog.dismissAniDialog();
            }
        });

    };

    @JavascriptInterface
    public void goFormPage(String infor){
//跳到表单页面
        if(!IsNullOrEmpty.isEmpty(infor)){
            ClickEntity clickEntity= JSON.parseObject(infor,ClickEntity.class);
            Intent intent=new Intent(this,InputActivity.class);
            intent.putExtra(Global.CLICK_ACTION,clickEntity);
            startActivity(intent);
        }
    };



    @JavascriptInterface
    public void finishActivity(){
        finish();
    };

    @JavascriptInterface
    public void setPageTitle(final String title){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                titleTextView.setText(title);
            }
        });

    };

    public static final int SCAN_JS=798;
    @JavascriptInterface
    public void startScan(){

        AndPermission.with(this)
                .permission(Permission.CAMERA)
                .onGranted(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        InputActivity.selectkey="dsfs";
                        InputActivity.selectid="sdfdsf";
                        CaptureActivity.tengineID = TengineID.TIDCARD2;
                        CaptureActivity.ShowCopyRightTxt = "Esint";
                        Intent intent = new Intent(WebveiwActivity.this, CaptureActivity.class);
                        intent.putExtra("engine", engineDemo);
                        startActivityForResult(intent, SCAN_JS);
                    }
                })
                .onDenied(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        Toast.makeText(WebveiwActivity.this,"请开启相机权限",Toast.LENGTH_SHORT).show();
                    }
                }).start();

    };

    public void setJsScanResult(String idcard){

        // Android版本变量
       // final int version = Build.VERSION.SDK_INT;
// 因为该方法在 Android 4.4 版本才可使用，所以使用时需进行版本判断
     //   if (version < 18) {
            webView.loadUrl("javascript:setScanResult('"+idcard+"')");
//        } else {
//            webView.evaluateJavascript("javascript:setScanResult('"+idcard+"')", new ValueCallback<String>() {
//                @Override
//                public void onReceiveValue(String value) {
//                    //此处为 js 返回的结果
//                }
//            });
//        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();

            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(SCAN_JS==requestCode&&resultCode==3){
            if (data == null) {
                return;
            }
            CardInfo cardInfo = (CardInfo) data.getSerializableExtra("cardinfo");
            if(cardInfo!=null){
                String allinfor=cardInfo.getAllinfo();
                String[] inforpart=allinfor.split("\n");
                String namePart=inforpart[0].split(":")[1];
                String sexPart=inforpart[1].split(":")[1];
                String nationPart=inforpart[2].split(":")[1];
                String birthdayPart=inforpart[3].split(":")[1];
                String addresspart=inforpart[4].split(":")[1];
                String numpart=inforpart[5].split(":")[1];
                JSONObject jsonObject=new JSONObject();
                try {
                    //name,sex,folk,birthday,address,num
                    jsonObject.put("name",namePart.trim());
                    jsonObject.put("sex",sexPart.trim());
                    jsonObject.put("folk",nationPart.trim());
                    jsonObject.put("birthday",birthdayPart.trim());
                    jsonObject.put("address",addresspart.trim());
                    jsonObject.put("num",numpart.trim());
                    setJsScanResult(jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
