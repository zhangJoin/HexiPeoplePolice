package com.xiante.jingwu.qingbao.CustomView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.xiante.jingwu.qingbao.R;

/**
 * Created by zhong on 2018/5/22.
 */

public class SelectImageView extends FrameLayout {
    private ImageView imageView;
    private View deleteView;
    private String imagePath;
    public SelectImageView(@NonNull Context context,String imagepath) {
        super(context);
        this.imagePath=imagepath;
        init(context);
    }

    public SelectImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SelectImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        View.inflate(context, R.layout.select_image,this);
        imageView=findViewById(R.id.imageV);
        deleteView=findViewById(R.id.deleteImage);
    }

    public ImageView getImageView() {
        return imageView;
    }

    public View getDeleteView() {
        return deleteView;
    }

    public String getImagePath() {
        return imagePath;
    }
}
