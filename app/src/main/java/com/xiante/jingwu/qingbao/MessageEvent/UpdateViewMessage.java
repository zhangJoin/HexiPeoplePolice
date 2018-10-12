package com.xiante.jingwu.qingbao.MessageEvent;

import java.util.List;

/**
 * Created by zhong on 2018/4/23.
 */

public class UpdateViewMessage {
    private String inputKey="";
    private String updateStr="";

    public UpdateViewMessage(String inputKey, String updateStr) {
        this.inputKey = inputKey;
        this.updateStr = updateStr;
    }

    public String getInputKey() {
        return inputKey;
    }

    public void setInputKey(String inputKey) {
        this.inputKey = inputKey;
    }

    public String getUpdateStr() {
        return updateStr;
    }

    public void setUpdateStr(String updateStr) {
        this.updateStr = updateStr;
    }
}
