package com.xiante.jingwu.qingbao.MessageEvent;

import com.xiante.jingwu.qingbao.Bean.Common.SecurityEntity;

/**
 * Created by zhong on 2018/9/19.
 *
 */

public class NaviMessage {
    SecurityEntity securityEntity;

    public NaviMessage(SecurityEntity securityEntity) {
        this.securityEntity = securityEntity;
    }

    public SecurityEntity getSecurityEntity() {
        return securityEntity;
    }

    public void setSecurityEntity(SecurityEntity securityEntity) {
        this.securityEntity = securityEntity;
    }
}
