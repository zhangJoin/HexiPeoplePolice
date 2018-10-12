package com.xiante.jingwu.qingbao.Bean.Common;

import com.mcxtzhang.indexlib.IndexBar.bean.BaseIndexPinyinBean;

import java.io.Serializable;

/**
 * @author 郑靖廷 Yoda_T
 * @email 17600203706@163.com
 * @time $date$ $time$
 */
public class InforOfficeBean extends BaseIndexPinyinBean implements Serializable {
    private String strGuid="";
    private String strName="";//姓名
    private String strPortrait="";//头像路径
    private String strLabelName="";//标签汉字名称串用顿号分割
    private String strLabel="";//标签guid多个用逗号分割
    private String strTradeName="";//信息员行业类别名称
    private String strTrade="";//行业类别guid

    public String getStrGuid() {
        return strGuid;
    }

    public void setStrGuid(String strGuid) {
        this.strGuid = strGuid;
    }

    public String getStrName() {
        return strName.trim();
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public String getStrPortrait() {
        return strPortrait;
    }

    public void setStrPortrait(String strPortrait) {
        this.strPortrait = strPortrait;
    }

    public String getStrLabelName() {
        return strLabelName;
    }

    public void setStrLabelName(String strLabelName) {
        this.strLabelName = strLabelName;
    }

    public String getStrLabel() {
        return strLabel;
    }

    public void setStrLabel(String strLabel) {
        this.strLabel = strLabel;
    }

    public String getStrTradeName() {
        return strTradeName;
    }

    public void setStrTradeName(String strTradeName) {
        this.strTradeName = strTradeName;
    }

    public String getStrTrade() {
        return strTrade;
    }

    public void setStrTrade(String strTrade) {
        this.strTrade = strTrade;
    }

    @Override
    public String toString() {
        return "InforOfficeBean{" +
                "strGuid='" + strGuid + '\'' +
                ", strName='" + strName + '\'' +
                ", strPortrait='" + strPortrait + '\'' +
                ", strLabelName='" + strLabelName + '\'' +
                ", strLabel='" + strLabel + '\'' +
                ", strTradeName='" + strTradeName + '\'' +
                ", strTrade='" + strTrade + '\'' +
                '}';
    }

    @Override
    public String getTarget() {
        return strName.trim();
    }
}
