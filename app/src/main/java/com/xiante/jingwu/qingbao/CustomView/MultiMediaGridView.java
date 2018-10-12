package com.xiante.jingwu.qingbao.CustomView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.xiante.jingwu.qingbao.Activity.BigImageActivity;
import com.xiante.jingwu.qingbao.Bean.Input.InputItemBean;
import com.xiante.jingwu.qingbao.Bean.Input.UploadBean;
import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.DisplayUtil;
import com.xiante.jingwu.qingbao.Util.Global;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhong on 2018/4/26.
 */

public class MultiMediaGridView extends GridLayout implements InputView {
    Context context;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private int margin=10;
    private static final  int columnCount=5;
    int imageWidth=0;
    ArrayList<String> selectImageList;
    private InputItemBean inputItembean;

    public MultiMediaGridView(Context context) {
        super(context);
        init(context);
    }

    public MultiMediaGridView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public MultiMediaGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        imageWidth=(getContext().getResources().getDisplayMetrics().widthPixels-columnCount*margin-margin)/columnCount;
    }

    private void init(Context context){
        this.context=context;
        setColumnCount(columnCount);
        imageLoader = ImageLoader.getInstance();
        margin= DisplayUtil.dip2px(context,margin);
        // 使用DisplayImageOption.Builder()创建DisplayImageOptions
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.ic_launcher) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.ic_launcher) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.ic_launcher) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
                // .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .build(); // 创建配置过的DisplayImageOption对象
        selectImageList=new ArrayList<>();
    }

    @Override
    public UploadBean getUploadValue() {
        String str="";
        if(selectImageList!=null){
            str= JSON.toJSONString(selectImageList);
        }
        return new UploadBean(Global.IMAGE,str);
    }

    @Override
    public boolean checkUploadValue() {
        if(inputItembean.getIsMust().equals("1")){
            if(selectImageList==null||selectImageList.size()==0){
                Toast.makeText(getContext(),"上传图片不能为空",Toast.LENGTH_SHORT).show();
                return false;
            }else {
                return  true;
            }
        }else {
            return true;
        }

    }

    public int getSelectSize(){
        return selectImageList.size();
    }

    @Override
    public void initInputView(InputItemBean inputItemBean) {
          this.inputItembean=inputItemBean;
    }

    @Override
    public void updateInputView(String string) {
        List<String>      tempList= JSON.parseArray(string,String.class);
        selectImageList.addAll(tempList);
        for(int i=0;i<tempList.size();i++){
            LayoutParams params=new LayoutParams();
            params.width=imageWidth;
            params.height=imageWidth;
            params.setMargins(0,0,margin,margin);
            final SelectImageView imageView=new SelectImageView(context,tempList.get(i));
            imageView.setLayoutParams(params);
            Glide.with(context).load(tempList.get(i)).into(imageView.getImageView());
            imageView.getDeleteView().setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectImageList.remove(imageView.getImagePath());
                    ((ViewGroup)(imageView.getParent())).removeView(imageView);
                }
            });
             imageView.setTag(tempList.get(i));
            imageView.getImageView().setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int p=0;
                    for(int i=0;i<selectImageList.size();i++){
                       if(imageView.getTag().toString().equals(selectImageList.get(i))){
                           p=i;
                         break;
                       }
                    }
                    Intent intent=new Intent(context, BigImageActivity.class);
                    intent.putStringArrayListExtra("image",selectImageList);
                    intent.putExtra("position",p);
                    context.startActivity(intent);
                }
            });
            addView(imageView);
        }
    }


}
