package com.xiante.jingwu.qingbao.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.eallnetwork.framework.FailCallback;
import com.example.eallnetwork.framework.NetworkFactory;
import com.example.eallnetwork.framework.SuccessfulCallback;
import com.example.eallnetwork.workUtils.OkhttpFactory;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.extras.recyclerview.PullToRefreshRecyclerView;
import com.xiante.jingwu.qingbao.Adapter.ScoreAdapter;
import com.xiante.jingwu.qingbao.Bean.Common.ScoreEntity;
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
 * Created by zhong on 2018/4/28.
 */

public class ScoreFragment extends Fragment {
    PullToRefreshRecyclerView recyclerView;
    View rootView;
    String url;
    boolean load = false;
    ScoreAdapter adapter;
    private int pageindex = 1;
    private int pageSize = 20;
    private LoaddingDialog loaddingDialog;
    List<ScoreEntity> scoreEntityList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getArguments().getString("url");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (!load) {

            loaddingDialog = new LoaddingDialog(getActivity());
            rootView = inflater.inflate(R.layout.fragment_list, null);
            recyclerView = rootView.findViewById(R.id.recycleView);
            recyclerView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
            recyclerView.getRefreshableView().setLayoutManager(new LinearLayoutManager(getActivity()));
            scoreEntityList = new ArrayList<>();
            adapter = new ScoreAdapter(getActivity(), scoreEntityList);
            recyclerView.getRefreshableView().setAdapter(adapter);
            recyclerView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
                @Override
                public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                    pageindex = 1;
                    getData();
                }

                @Override
                public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                    pageindex++;
                    getData();
                }
            });
        }

        if (!load && getUserVisibleHint() && rootView != null) {
            load = true;
            getData();
        }
        return rootView;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!load && getUserVisibleHint() && rootView != null) {
            load = true;
            getData();
        }
    }

    public void getData() {
        boolean isSuccess = Utils.isSuccess(getActivity());
        if (!isSuccess) {
            Toast.makeText(getActivity(), getString(R.string.netError), Toast.LENGTH_SHORT).show();
            return;
        }
        loaddingDialog.showDialog();
        NetworkFactory networkFactory = OkhttpFactory.getInstance();
        SuccessfulCallback successfulCallback = new SuccessfulCallback() {
            @Override
            public void success(String str) throws JSONException {
                if(getActivity()==null){
                    return;
                }
                if (new CodeExceptionUtil(getActivity()).dealException(str)) {
                    String resultObject = new JSONObject(str).optString("resultData");
                    List<ScoreEntity> entyList = JSON.parseArray(resultObject, ScoreEntity.class);
                    if (entyList != null) {
                        if (pageindex == 1) {
                            scoreEntityList.clear();
                            scoreEntityList.addAll(entyList);
                        } else {
                            scoreEntityList.addAll(entyList);
                        }
                        adapter.notifyDataSetChanged();
                        if (entyList.size() == 0) {
                            Toast.makeText(getActivity(), "没有数据了", Toast.LENGTH_SHORT).show();
                        }
                    }
                    recyclerView.onRefreshComplete();
                }
                loaddingDialog.dismissAniDialog();
            }

            @Override
            public void success(InputStream ism, long conentLength) {

            }
        };
        FailCallback failCallback = new FailCallback() {
            @Override
            public void fail(String str) {
                if(getActivity()==null){
                    return;
                }
                loaddingDialog.dismissAniDialog();
            }
        };
        UrlManager urlManager = new UrlManager(getActivity());
        String neturl = "";
        if (url.contains("?")) {
            neturl = new StringBuilder(urlManager.getData_url()).append(url).append("&").append(urlManager.getExtraStr()).toString();
        } else {
            neturl = new StringBuilder(urlManager.getData_url()).append(url).append("?").append(urlManager.getExtraStr()).toString();
        }
        HashMap<String, String> param = new HashMap<>();
        param.put("intPageIndex", pageindex + "");
        param.put("intPageSize", pageSize + "");
        String user = getActivity().getSharedPreferences(Global.USER_ACCOUNT, Context.MODE_PRIVATE).getString(Global.USER_ACCOUNT, "");
        param.put("strAccount", user);
        networkFactory.start(NetworkFactory.METHOD_GET, neturl, param, successfulCallback, failCallback);
    }
}
