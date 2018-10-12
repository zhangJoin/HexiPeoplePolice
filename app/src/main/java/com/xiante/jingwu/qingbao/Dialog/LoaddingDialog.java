package com.xiante.jingwu.qingbao.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageView;

import com.xiante.jingwu.qingbao.R;

/**
 * Created by zhong on 2018/5/10.
 */

public class LoaddingDialog extends Dialog {
       ImageView imageView;
       AnimationDrawable animDrawable;
    public LoaddingDialog( Context context) {
        super(context,R.style.loadingdialog);
         init(context);
    }

    public LoaddingDialog( Context context, int themeResId) {
        super(context, themeResId);
       init(context);
    }

    protected LoaddingDialog( Context context, boolean cancelable,  OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    private void init(Context context){
        setContentView(R.layout.loading_dialog);
        //animDrawable= (AnimationDrawable) context.getResources().getDrawable(R.drawable.loading);
        imageView=findViewById(R.id.imageview);
        imageView.setBackgroundResource(R.drawable.loading_dialogbg);
        animDrawable = (AnimationDrawable) imageView.getBackground();
    }

    public void showDialog(){
        animDrawable.start();
        show();
    }
    public void dismissAniDialog(){
        animDrawable.stop();
        dismiss();
    }

//
//    public void dismissDialog() {
//        animDrawable.stop();
//        dismiss();
//    }
}
