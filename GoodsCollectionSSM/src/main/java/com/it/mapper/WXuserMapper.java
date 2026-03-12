package com.it.mapper;

import com.it.entity.WXuser;

public interface WXuserMapper {
    //查询是否有该微信用户
    WXuser findByopenid(String openid);

    //新增微信用户
    void addNewWXuser(WXuser wxUser);

    //修改session_key
    void updateWXUserInfo(WXuser wxUser);
}
