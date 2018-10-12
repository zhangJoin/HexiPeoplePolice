package com.xiante.jingwu.qingbao.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.xiante.jingwu.qingbao.CustomView.CommonView.CircularImageView;
import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.DisplayUtil;
import com.xiante.jingwu.qingbao.Util.Global;
import com.xiante.jingwu.qingbao.Util.Utils;

/**
 * Created by zhong on 2018/5/10.
 */

public class CodeDialog extends Dialog {

    TextView powerNameTV;
    CircularImageView policeAvatarIV;
    ImageView powercodeIV;
    Bitmap bitmap;
    public CodeDialog(@NonNull final Context context, String powername, String codeStr) {
        super(context, R.style.MyDialog);
        setContentView(R.layout.power_code_dialog);
        powerNameTV=findViewById(R.id.powerNameTV);
        policeAvatarIV=findViewById(R.id.policeAvatarIV);
        powercodeIV=findViewById(R.id.powercodeIV);
        powerNameTV.setText(powername);
        int width=DisplayUtil.dip2px(context,200);
         bitmap= Utils.createQRImage(codeStr, width,width);
        if(bitmap!=null){
            powercodeIV.setImageBitmap(bitmap);
        }
        String avatar=context.getSharedPreferences(Global.ME_AVATAR,Context.MODE_PRIVATE).getString(Global.ME_AVATAR,"");
        Glide.with(context).load(avatar).error(R.drawable.me_default).placeholder(R.drawable.me_default).into(policeAvatarIV);
        powercodeIV.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Utils.saveImageToGallery(context,bitmap);
                Toast.makeText(context,"成功保存到相册",Toast.LENGTH_SHORT).show();
                return false;
            }
        });

    }

}
