package com.xiante.jingwu.qingbao.Bean.Common;

/**
 * Created by zhong on 2018/5/9.
 */

public class ModeTwoEntity {
    private String strGuid="",
    topLeftText="",
    topRightText="",
    middleText="",
    bottomLeftText="",//不传值不显示
    bottomRightText="",
    topRightColor="";
    public ModeTwoEntity() {
    }

    public String getStrGuid() {
        return strGuid;
    }

    public void setStrGuid(String strGuid) {
        this.strGuid = strGuid;
    }

    public String getTopLeftText() {
        return topLeftText;
    }

    public void setTopLeftText(String topLeftText) {
        this.topLeftText = topLeftText;
    }

    public String getTopRightText() {
        return topRightText;
    }

    public void setTopRightText(String topRightText) {
        this.topRightText = topRightText;
    }

    public String getTopRightColor() {
        return topRightColor;
    }

    public void setTopRightColor(String topRightColor) {
        this.topRightColor = topRightColor;
    }

    public String getMiddleText() {
        return middleText;
    }

    public void setMiddleText(String middleText) {
        this.middleText = middleText;
    }

    public String getBottomLeftText() {
        return bottomLeftText;
    }

    public void setBottomLeftText(String bottomLeftText) {
        this.bottomLeftText = bottomLeftText;
    }

    public String getBottomRightText() {
        return bottomRightText;
    }

    public void setBottomRightText(String bottomRightText) {
        this.bottomRightText = bottomRightText;
    }
}
