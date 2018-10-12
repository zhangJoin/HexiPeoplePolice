package com.xiante.jingwu.qingbao.CustomView;

import com.xiante.jingwu.qingbao.Bean.Input.InputItemBean;
import com.xiante.jingwu.qingbao.Bean.Input.UploadBean;

/**
 * Created by zhong on 2018/4/19.
 */

public interface InputView  {


    public UploadBean getUploadValue();

    public boolean checkUploadValue();

    public void initInputView(InputItemBean inputItemBean);

    public void updateInputView(String string);

}
