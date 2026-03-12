package com.it.service;

import com.it.entity.User;
import com.it.entity.WXuser;

import java.util.Map;

public interface WeiChatService {
    /**
     * 用户登录，通过code向微信获取 openid和session_key
     * @param code 临时令牌
     * @return
     */
    public Map<String,Object> login(String code);

    /**
     * 查询是否有该微信用户
     * @param openid
     * @return
     */
    WXuser findByopenid(String openid);

    /**
     * 新增微信授权登录的用户
     * @param user 用户信息
     */
    public void addWXUser(User user, WXuser wXuser);

    /**
     * 修改session_key
     */
    void updateWXUserInfo(WXuser wxUser);

}
