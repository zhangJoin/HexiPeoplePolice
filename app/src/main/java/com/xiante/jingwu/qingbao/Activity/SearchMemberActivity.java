package com.xiante.jingwu.qingbao.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.eallnetwork.framework.FailCallback;
import com.example.eallnetwork.framework.NetworkFactory;
import com.example.eallnetwork.framework.SuccessfulCallback;
import com.example.eallnetwork.workUtils.OkhttpFactory;
import com.xiante.jingwu.qingbao.Adapter.HistoryRecordAdapter;
import com.xiante.jingwu.qingbao.Adapter.SearchResultAdapter;
import com.xiante.jingwu.qingbao.Bean.Common.SearchResultBean;
import com.xiante.jingwu.qingbao.Dialog.LoaddingDialog;
import com.xiante.jingwu.qingbao.NetWork.UrlManager;
import com.xiante.jingwu.qingbao.R;
import com.xiante.jingwu.qingbao.Util.CodeExceptionUtil;
import com.xiante.jingwu.qingbao.Util.Global;
import com.xiante.jingwu.qingbao.Util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 添加成员列表
 */
public class SearchMemberActivity extends BaseActivity {
    private EditText etSearch;
    private Button btcancel;
    public static TextView mTextViewHistory;
    private ListView mListViewHistory, mListViewResult;
    private LoaddingDialog loaddingDialog;
    private HistoryRecordAdapter mHistoryAdapter;
    private SearchResultAdapter mSearchResultAdapter;
    private SharedPreferences mSharePreference;
    private List<String> listDatas ;
    private View view;
    private List<SearchResultBean> list ;
    private ImageView mSearchDelete;
    String unityGuid="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initListener();
    }

    @Override
    public void initView() {
        unityGuid=getIntent().getStringExtra("id");
        list = new ArrayList<>();
        view = LayoutInflater.from(this).inflate(R.layout.searchmemberlayout, null);
        setContentView(view);
        etSearch = findViewById(R.id.et_search_member);
        btcancel = findViewById(R.id.bt_search_member_cancel);
        mListViewHistory = findViewById(R.id.lv_search_member);
        mTextViewHistory = findViewById(R.id.tv_search_member);
        loaddingDialog = new LoaddingDialog(this);
        mListViewResult = findViewById(R.id.lv_search_result);
        mSharePreference = getSharedPreferences("history", Context.MODE_PRIVATE);
        mSearchDelete=findViewById(R.id.iv_search_delete);
    }

    @Override
    public void initData() {
        String historyStr = mSharePreference.getString("history", "");
        if (!TextUtils.isEmpty(historyStr)) {
            mListViewHistory.setVisibility(View.VISIBLE);
            mTextViewHistory.setEnabled(true);
            String[] split = historyStr.split(",");
            listDatas= new ArrayList<>();
            for (int i = 0; i < split.length; i++) {
                listDatas.add(split[i]);
            }
            mHistoryAdapter = new HistoryRecordAdapter(listDatas, this);
            mListViewHistory.setAdapter(mHistoryAdapter);
            mTextViewHistory.setText("清除搜索记录");
            mTextViewHistory.setTextColor(getResources().getColor(R.color.textBlue));
        }

    }

    @Override
    public void initListener() {
        mListViewHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hideInputMethod();
                mListViewHistory.setVisibility(View.GONE);
                mListViewResult.setVisibility(View.VISIBLE);
                mTextViewHistory.setVisibility(View.GONE);
                goSearch(listDatas.get(position));
            }
        });
        btcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!TextUtils.isEmpty(etSearch.getText().toString().trim())) {
                        saveDatas(etSearch.getText().toString().trim());
                        hideInputMethod();
                        goSearch(etSearch.getText().toString().trim());
                        mListViewHistory.setVisibility(View.GONE);
                        mListViewResult.setVisibility(View.VISIBLE);
                        mTextViewHistory.setVisibility(View.GONE);

                    }
                    return true;
                }
                return false;
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString().trim())) {
                    initData();
                    mListViewResult.setVisibility(View.GONE);
                    mTextViewHistory.setVisibility(View.VISIBLE);
                    mSearchDelete.setVisibility(View.GONE);
                }else{
                    mSearchDelete.setVisibility(View.VISIBLE);
                }
            }
        });
        mTextViewHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSharePreference.edit().remove("history").commit();
                listDatas.clear();
                mHistoryAdapter.notifyDataSetChanged();
                mTextViewHistory.setText("-暂无历史搜索-");
                mTextViewHistory.setTextColor(getResources().getColor(R.color.textnormalcolor));
            }
        });
        mSearchDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSearch.setText("");
                initData();
                mListViewResult.setVisibility(View.GONE);
                mTextViewHistory.setVisibility(View.VISIBLE);
                mSearchDelete.setVisibility(View.GONE);
            }
        });
    }

    private void hideInputMethod() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();
        if (isOpen) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void saveDatas(String inputStr) {
        String oldText = mSharePreference.getString("history", "");
        StringBuilder builder = new StringBuilder(inputStr);
        builder.append("," + oldText);
        if (!TextUtils.isEmpty(inputStr) && !oldText.contains(inputStr)) {
            SharedPreferences.Editor editor = mSharePreference.edit();
            editor.putString("history", builder.toString());
            editor.apply();
        }
    }

    private void goSearch(String name) {
        boolean isSuccess = Utils.isSuccess(this);
        if (!isSuccess) {
            Toast.makeText(this, getString(R.string.netError), Toast.LENGTH_SHORT).show();
            return;
        }
        NetworkFactory mNetworkFactory = OkhttpFactory.getInstance();
        SuccessfulCallback mSuccessfulCallback = new SuccessfulCallback() {
            @Override
            public void success(String str) throws JSONException {
                if (new CodeExceptionUtil(SearchMemberActivity.this).dealException(str)) {
                    List<SearchResultBean> list= JSON.parseArray(new JSONObject(str).optString("resultData"), SearchResultBean.class);
                    mSearchResultAdapter = new SearchResultAdapter(list, SearchMemberActivity.this,unityGuid);
                    mListViewResult.setAdapter(mSearchResultAdapter);
                }
                loaddingDialog.dismissAniDialog();
            }

            @Override
            public void success(InputStream ism, long conentLength) {

            }
        };
        FailCallback mFailCallback = new FailCallback() {
            @Override
            public void fail(String str) {
               loaddingDialog.dismissAniDialog();
            }
        };
        HashMap<String, String> param = new HashMap<>();
        param.put("strName",name);
        param.put("isAll", "2");
        String useraccount=getSharedPreferences(Global.USER_ACCOUNT,MODE_PRIVATE).getString(Global.USER_ACCOUNT,"");
        param.put("strAccount",useraccount);
        UrlManager urlManager = new UrlManager(this);
        mNetworkFactory.start(NetworkFactory.METHOD_GET, urlManager.getPoliceInformer(), param, mSuccessfulCallback
                , mFailCallback);
    }
}
