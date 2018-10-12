package com.xiante.jingwu.qingbao.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eallnetwork.framework.FailCallback;
import com.example.eallnetwork.framework.NetworkFactory;
import com.example.eallnetwork.framework.SuccessfulCallback;
import com.example.eallnetwork.workUtils.OkhttpFactory;
import com.xiante.jingwu.qingbao.NetWork.UrlManager;
import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.CodeExceptionUtil;
import com.xiante.jingwu.qingbao.Util.Global;
import com.xiante.jingwu.qingbao.Util.Utils;

import org.json.JSONException;

import java.io.InputStream;
import java.util.HashMap;

public class EditorDialog extends Dialog {
    private Context context;
    private EditText etEditor;
    private Button btEditorComform;
    private String strUnityGuid;

    public EditorDialog(@NonNull Context context) {
        super(context, R.style.loadingdialog);
        init(context);
    }

    public EditorDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init(context);
    }

    public EditorDialog(@NonNull Context context, boolean cancelable, @Nullable DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    public EditorDialog(Context context, String strGuid) {
        this(context);
        this.strUnityGuid = strGuid;
        init(context);
    }

    private void init(final Context context) {
        this.context = context;
        setContentView(R.layout.editordialoglayout);
        btEditorComform = findViewById(R.id.bt_editor_conform);
        etEditor = findViewById(R.id.et_editor);
        btEditorComform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strPowerName = etEditor.getText().toString().trim();
                if (TextUtils.isEmpty(strPowerName)) {
                    Toast.makeText(context, context.getString(R.string.editorHint), Toast.LENGTH_SHORT).show();
                    return;
                }
                btEditorComform.setEnabled(false);
                commitName(strPowerName);
            }
        });
    }

    private void commitName(final String strPowerName) {
        boolean success = Utils.isSuccess(context);
        if (!success) {
            Toast.makeText(context, context.getString(R.string.netError), Toast.LENGTH_SHORT).show();
            return;
        }
        NetworkFactory networkFactory = OkhttpFactory.getInstance();
        SuccessfulCallback successfulCallback = new SuccessfulCallback() {
            @Override
            public void success(String str) throws JSONException {
                CodeExceptionUtil codeExceptionUtil = new CodeExceptionUtil(context);
                if (codeExceptionUtil.dealException(str)) {
                    if (mListener != null) {
                        mListener.updateName(strPowerName);
                    }
                    dismiss();
                }
            }

            @Override
            public void success(InputStream ism, long conentLength) {

            }
        };
        FailCallback failCallback = new FailCallback() {
            @Override
            public void fail(String str) {
                dismiss();
                Log.i("urlfail", str);
            }
        };
        String account = context.getSharedPreferences(Global.USER_ACCOUNT, Context.MODE_PRIVATE).getString(Global.USER_ACCOUNT, "");
        HashMap<String, String> param = new HashMap<>();
        param.put("strAccount", account);
        param.put("strUnityName", strPowerName);
        param.put("strUnitGuid", strUnityGuid);
        UrlManager urlManager = new UrlManager(context);
        networkFactory.start(NetworkFactory.METHOD_GET, urlManager.getUpdatePowerUnityUrl(), param, successfulCallback, failCallback);
    }

    public interface OnEditorListener {
        void updateName(String name);
    }

    protected OnEditorListener mListener;

    public void setOnListener(OnEditorListener onListener) {
        this.mListener = onListener;
    }
}
