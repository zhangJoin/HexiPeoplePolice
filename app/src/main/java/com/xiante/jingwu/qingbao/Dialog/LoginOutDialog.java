package com.xiante.jingwu.qingbao.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.xiante.jingwu.qingbao.Activity.LoginActivity;
import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.Global;

/**
 * Created by zhong on 2018/5/10.
 */

public class LoginOutDialog extends Dialog {

    TextView  cancelTV, sendTV;

    public LoginOutDialog(@NonNull final Context context) {
        super(context, R.style.MyDialog);
        setContentView(R.layout.loginout_dialog);
        cancelTV = findViewById(R.id.cancelTV);
        sendTV = findViewById(R.id.sendTV);
        sendTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.getSharedPreferences(Global.USER_ACCOUNT,Context.MODE_PRIVATE).edit().putString(Global.USER_ACCOUNT,"").apply();
                context.getSharedPreferences(Global.USER_PASSWORD,Context.MODE_PRIVATE).edit().putString(Global.USER_PASSWORD,"").apply();
                Intent intent=new Intent(context, LoginActivity.class);
                context.startActivity(intent);
                dismiss();
            }
        });
        cancelTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}
