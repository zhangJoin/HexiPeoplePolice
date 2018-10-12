package com.xiante.jingwu.qingbao.Bean.Common;

/**
 * Created by zhong on 2018/4/28.
 */

public class ModelOneEntity {

   public ModelOneEntity(){}

    public String getStrGuid() {
        return strGuid;
    }

    public void setStrGuid(String strGuid) {
        this.strGuid = strGuid;
    }

    public String getTopText() {
        return topText;
    }

    public void setTopText(String topText) {
        this.topText = topText;
    }

    public String getMiddlePic() {
        return middlePic;
    }
    public void setMiddlePic(String middlePic) {
        this.middlePic = middlePic;
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

    public String getTopTextColor() {
        return topTextColor;
    }

    public void setTopTextColor(String topTextColor) {
        this.topTextColor = topTextColor;
    }

    private String strGuid="",topText="",middlePic="",bottomLeftText="",bottomRightText="",topTextColor="";
}
