package com.it.service.impl;

import cn.hutool.http.HttpUtil;

import com.alibaba.fastjson.JSONObject;
import com.it.entity.User;
import com.it.entity.WXuser;
import com.it.mapper.UserMapper;
import com.it.mapper.WXuserMapper;
import com.it.service.UserService;
import com.it.service.WeiChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
@Transactional
public class WeiChatServiceImpl implements WeiChatService {
    @Autowired
    WXuserMapper wXuserMapper;
    @Autowired
    UserMapper userMapper;

    private String appId="wx9a1d326eff60dea0";
    private String secret="85f0a153019505dfdf432a5cfa344835";

    @Override
    public Map<String,Object> login(String code) {
        String url="https://api.weixin.qq.com/sns/jscode2session?appid="+appId+"&secret="+secret+"&js_code="+code+"&grant_type=authorization_code";
        String result= HttpUtil.get(url);
        System.out.println(result);

        //json解构字符串
        JSONObject jsonObject = JSONObject.parseObject(result);
        //登录成功的返回值
        String openid= (String) jsonObject.get("openid");
        String session_key= (String) jsonObject.get("session_key");
        //登录失败的返回值
        String errcode= (String) jsonObject.get("errcode");
        String errmsg= (String) jsonObject.get("errmsg");
        String rid= (String) jsonObject.get("rid");

        //构建返回值
        Map<String, Object> map = new TreeMap<>();

        map.put("openid",openid);
        map.put("session_key",session_key);
        map.put("errcode",errcode);
        map.put("errmsg",errmsg);
        map.put("rid",rid);

        return map;
    }

    @Override
    public void addWXUser(User user, WXuser wXuser) {
        userMapper.addUser(user);
        wXuserMapper.addNewWXuser(wXuser);
    }

    @Override
    public WXuser findByopenid(String openid) {
        return wXuserMapper.findByopenid(openid);
    }

    @Override
    public void updateWXUserInfo(WXuser wxUser) {
        wXuserMapper.updateWXUserInfo(wxUser);
    }
}
