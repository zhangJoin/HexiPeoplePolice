package com.xiante.jingwu.qingbao.Bean.Common;

import java.io.Serializable;

/**
 * Created by zhong on 2018/5/14.
 */

public class ModeFourEntity implements Serializable {
    private String strGuid="", topText="",
    middleText="", bottomText="", rightImage;

    public ModeFourEntity() {
    }

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

    public String getMiddleText() {
        return middleText;
    }

    public void setMiddleText(String middleText) {
        this.middleText = middleText;
    }

    public String getBottomText() {
        return bottomText;
    }

    public void setBottomText(String bottomText) {
        this.bottomText = bottomText;
    }

    public String getRightImage() {
        return rightImage;
    }

    public void setRightImage(String rightImage) {
        this.rightImage = rightImage;
    }
}
