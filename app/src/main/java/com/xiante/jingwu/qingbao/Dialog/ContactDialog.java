package com.xiante.jingwu.qingbao.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.xiante.jingwu.qingbao.R;

/**
 * Created by zhong on 2018/5/10.
 */

public class ContactDialog extends Dialog {

    TextView numberShowTV,cancelTV,sendTV;
    String telNumber;
    public ContactDialog(@NonNull final Context context, final String telNumber) {
        super(context, R.style.MyDialog);
        setContentView(R.layout.contact_dialog);
        this.telNumber=telNumber;
        numberShowTV=findViewById(R.id.numberShowTV);
        cancelTV=findViewById(R.id.cancelTV);
        sendTV=findViewById(R.id.sendTV);
        numberShowTV.setText(telNumber);
        sendTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + telNumber);
                intent.setData(data);
                context.startActivity(intent);
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
